/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	MasterServer.java
 *
 */


package FinalProject.masterserver;



//get data from each district saved in a file 
//get the file chunk by chunk
//read file and and add results to master table 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;

import FinalProject.communication.Comm;
import FinalProject.persons.Candidate;
import FinalProject.persons.Voter;

public class MasterServer {

	public MasterServer() {

	}

	public static void main(String args[]) {
		System.out.println("MasterServer Started\n");
		ConcurrentHashMap<String, Candidate> candidates;
		int port=4323;
		final Comm comm = new Comm(port);

		if (args.length < 4) {
			System.out.println("java MasterServer <voterFilename> <CandidatesFilename> <refreshrate> <Number of Districts>");
			System.out.println("java MasterServer voters.txt candidates.txt 10000");
			System.exit(1);
		}

		try {
			int refreshRate = Integer.valueOf(args[2]);
			int numDistricts = Integer.valueOf(args[3]);
			MasterServerInformation  lists = new MasterServerInformation();
			File votersFile = new File ("./"+args[0]);
			File candidatesFile = new File ("./"+args[1]);
			try{
				FileInputStream fis1 = new FileInputStream(votersFile);
			 
				//Construct BufferedReader from InputStreamReader
				BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1));
				String line=null;
				String voter = null;
				int district; 
				while ((line = br1.readLine()) != null) {
					district=Integer.valueOf(line);
					voter=br1.readLine();
					lists.addVoter(new Voter(voter, "", district + ""));
				}
				br1.close();
			}catch(Exception e){
				System.out.println("Error reading voters file.");
				System.exit(-1);
			}
			
			
			String candidate = null;
			String party = null; 
			try{
				FileInputStream fis2 = new FileInputStream(candidatesFile);
				//Construct BufferedReader from InputStreamReader
				BufferedReader br2 = new BufferedReader(new InputStreamReader(fis2));
				while ((party = br2.readLine()) != null) {
					candidate=br2.readLine();
					lists.addCandidate(new Candidate(candidate, party));	
				}
		 
				br2.close();
			}catch(Exception e){
				System.out.println("Error reading Candidates file.");
				System.exit(-1);
			}
			comm.StartMasterServer();
			comm.sendMessageClient((Object) lists);
			
			

			/*************************************/
			/* Receiving District Server info */
			/*************************************/
			ReceiveMasterServerInfo receiveThread = new ReceiveMasterServerInfo(comm);
			receiveThread.start();
			Thread.sleep(10000); // make sure at least some results are in
			candidates = receiveThread.getCandidates();

			// periodically update displayed results and send preliminary
			// election results
			ElectionResults electionUpdateThread = new ElectionResults(candidates, refreshRate, comm);
			electionUpdateThread.start();

			while (numDistricts!=receiveThread.getDistrictCount()) {
				Thread.sleep(1000);
			}
			electionUpdateThread.outputResults();
			electionUpdateThread.setElectionDone(true);
			receiveThread.setElectionDone(true);

		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			comm.shutdown();
		}
	}

}

