/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	MasterServer.java
 *
 */

package FinalProject.masterserver;


import java.awt.EventQueue;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import FinalProject.filereaders.CandidateReader;
import FinalProject.filereaders.VoterReader;
import FinalProject.communication.Comm;
import FinalProject.persons.Candidate;
import FinalProject.persons.Voter;

/**
 * This class represents the logic of the master server   
 **/

public class MasterServer {
	public static boolean electionDone = false;
	public static int refreshRate;
	public MasterServer() {

	}

	static Comm comm;

	public static void shutdown() throws Exception {
		//send message to districts notifying them the election is done
		electionDone=true;
	}

	public static void reset(){
		electionDone = false;
		comm = null;
	}

	public static void main(String args[]) {

		System.out.println("MasterServer Started\n");
		final MasterServerInformation lists = new MasterServerInformation();  //election information to be passed to districts 
		ConcurrentHashMap<String, Candidate> candidates;

		//ensure all the needed arguments have been received 
		if (args.length < 4) {
			System.out
					.println("java MasterServer <port> <voterFilename> <CandidatesFilename> <refreshrate> ");
			System.out
					.println("java MasterServer voters.txt candidates.txt 10000");
			System.exit(1);
		}

		int port = Integer.valueOf(args[0]);
		//start Comm to be used for network communication 
		try {
			comm = new Comm(port);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//create a thread to wait for the conclusion of the election via Console Input 
		try {

			new Thread() {
				public void run() {
					System.out.println("Console Input Thread");
					String s;
					//Scan a string, if the string is "done" then end the election  
					try {
						Scanner scanner = new Scanner(System.in);
						s = scanner.nextLine();
						if (s.trim().equalsIgnoreCase("done")) {
							electionDone = true;
							//launch the Graphical User Interface with the election results 
//							EventQueue.invokeLater(new Runnable() {
//								public void run() {
//									try {
//										framer1 frame = new framer1(lists.candidates);
//										frame.setVisible(true);
//									} catch (Exception e) {
//										e.printStackTrace();
//									}
//								}
//							});
							// System.exit(1);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();

		} catch (Exception e) { //catch a general exception 
			e.printStackTrace();
		}


		//initialize all the election information in a MasterServerInformation object and send to all districts 
		try {
			//get Command Line information 
			refreshRate = Integer.valueOf(args[3]);
			String votersFile = new String(args[1]);
			String candidatesFile = new String(args[2]);

			//get the voter information into a hashmap by reading a file containing the information of all the voters  
			try {
				VoterReader vReader = new VoterReader(votersFile);
				vReader.parse();
				ArrayList<Voter> vList = vReader.voters;
				for (Voter v : vList) {
					lists.addVoter(v);
				}
			} catch (Exception e) {  //notify user there was an error reading the voter file 
				System.out.println("Error reading voters file.");
				e.printStackTrace();
				System.exit(-1);
			}


			//get the candidate information into a hashmap by reading a file containing the information of all the candidates  
			try {
				CandidateReader cReader = new CandidateReader(candidatesFile);
				cReader.parse();
				ArrayList<Candidate> cList = cReader.candidates;
				for (Candidate c : cList) {
					lists.addCandidate(c);
				}
			} catch (Exception e) {  //notify user there was an error reading the candidate file 
				System.out.println("Error reading Candidates file.");
				e.printStackTrace();
				System.exit(-1);
			}
			try {
				Thread.sleep(1000);
				comm.sendMessageClient(lists); //send districts the election information 
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			/*************************************/
			/* Receiving District Server info */
			/*************************************/
			ReceiveMasterServerInfo receiveThread = new ReceiveMasterServerInfo(comm, lists.candidates); //create and start the thread to receive district updates 
			receiveThread.start();
			// Thread.sleep(10000); // make sure at least some results are in
			candidates = receiveThread.getCandidates();

			// periodically update displayed results and send preliminary
			ElectionResults electionUpdateThread = new ElectionResults(candidates, refreshRate, comm);
			electionUpdateThread.start();


			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						framer1 frame = new framer1(lists.candidates);
						frame.setVisible(true);
//						while(true){
						Thread.sleep(refreshRate);
						frame.setCandidates(lists.candidates);
//						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			while (electionDone == false) { //sleep until the election is done 
				Thread.sleep(1000);
			}

			electionUpdateThread.outputResults();  //write election results to a file 
			electionUpdateThread.setElectionDone(true);  //notify thread the election is done 
			receiveThread.setElectionDone(true); //notify thread the election is done

			electionUpdateThread.join();
			receiveThread.join();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally { //shutdown comm when everything is finished 
			try {
				comm.shutdown();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
	}
}
