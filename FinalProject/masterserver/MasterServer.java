/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	MasterServer.java
 *
 */

package FinalProject.masterserver;


import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import FinalProject.communication.Comm;
import FinalProject.persons.Candidate;
import FinalProject.persons.Voter;

/**
 * This class represents the logic of the master server   
 **/

public class MasterServer {
	public static boolean electionDone = false;

	public MasterServer() {

	}

	static Comm comm;

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
							//send message to districts notifying them the election is done 
							comm.sendMessageClient("end");
							comm.shutdown(); 
							//launch the Graphical User Interface with the election results 
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										framer1 frame = new framer1(lists.candidates);
										frame.setVisible(true);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
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
			int refreshRate = Integer.valueOf(args[3]);
			File votersFile = new File(args[1]);
			File candidatesFile = new File(args[2]);
			
			//get the voter information into a hashmap by reading a file containing the information of all the voters  
			try {
				FileInputStream fis1 = new FileInputStream(votersFile);
				BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1));
				String line = null;
				String voter = null;
				int district;
				while ((line = br1.readLine()) != null) {  //read the entire file 
					district = Integer.valueOf(line);
					voter = br1.readLine();
					lists.addVoter(new Voter(voter, "", district + ""));
				}
				br1.close();
			} catch (Exception e) {  //notify user there was an error reading the voter file 
				System.out.println("Error reading voters file.");
				e.printStackTrace();
				System.exit(-1);
			}

			String candidate = null;
			String party = null;
			//get the candidate information into a hashmap by reading a file containing the information of all the candidates  
			try {
				FileInputStream fis2 = new FileInputStream(candidatesFile);
				BufferedReader br2 = new BufferedReader(new InputStreamReader(fis2));
				while ((party = br2.readLine()) != null) { //read the entire file 
					candidate = br2.readLine();
					lists.addCandidate(new Candidate(candidate, party));
				}

				br2.close();
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

			while (electionDone == false) { //sleep until the election is done 
				Thread.sleep(1000);
			}
			electionUpdateThread.outputResults();  //write election results to a file 
			electionUpdateThread.setElectionDone(true);  //notify thread the election is done 
			receiveThread.setElectionDone(true); //notify thread the election is done 

		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		} finally { //shutdown comm when everything is finished 
			try {
				comm.shutdown();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
