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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import FinalProject.communication.Comm;
import FinalProject.persons.Candidate;
import FinalProject.persons.Voter;

public class MasterServer {
	public static boolean electionDone=false;
	public MasterServer() {

	}

	public static void main(String args[]) {
		
		System.out.println("MasterServer Started\n");
		ConcurrentHashMap<String, Candidate> candidates;
		Comm comm=null;


		if (args.length < 4) {
			System.out.println("java MasterServer <port> <voterFilename> <CandidatesFilename> <refreshrate> ");
			System.out.println("java MasterServer voters.txt candidates.txt 10000");
			System.exit(1);
		}
		
		try{
			int port=Integer.valueOf(args[0]);
			comm = new Comm(port);
		}catch(SocketException e){
			e.printStackTrace();
		}
		try{
			
			new Thread()
	        {
	            public void run() {
	                System.out.println("Console Input Thread");
	    			String s; 
		    		try{
	    				Scanner scanner = new Scanner(System.in);
		    			s=scanner.nextLine();
		    			if(s.trim().equalsIgnoreCase("done")){
		    				electionDone=true;
		    				System.exit(1);
		    			}
		    		}catch(Exception e){
		    			e.printStackTrace();
		    		}
	            }
	        }.start();

			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}

		try {
			int refreshRate = Integer.valueOf(args[3]);
			MasterServerInformation  lists = new MasterServerInformation();
			File votersFile = new File (args[1]);
			File candidatesFile = new File (args[2]);
			
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
					lists.addVoter(new Voter(voter,"",district+""));
				}
				br1.close();
			}catch(Exception e){
				System.out.println("Error reading voters file.");
				e.printStackTrace();
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
					lists.addCandidate(new Candidate(candidate,party));	
				}
		 
				br2.close();
			}catch(Exception e){
				System.out.println("Error reading Candidates file.");
				e.printStackTrace();
				System.exit(-1);
			}
			try{
				comm.sendMessageClient((Object) lists);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			

			/*************************************/
			/* Receiving District Server info */
			/*************************************/
			ReceiveMasterServerInfo receiveThread = new ReceiveMasterServerInfo(comm);
			receiveThread.start();
			//Thread.sleep(10000); // make sure at least some results are in
			candidates = receiveThread.getCandidates();

			// periodically update displayed results and send preliminary
			// election results
			ElectionResults electionUpdateThread = new ElectionResults(candidates, refreshRate, comm);
			electionUpdateThread.start();

			while (electionDone==false) {
				Thread.sleep(1000);
			}
			electionUpdateThread.outputResults();
			electionUpdateThread.setElectionDone(true);
			receiveThread.setElectionDone(true);

		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				comm.shutdown();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

