/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	ElectionResults.java
 *
 */


package FinalProject.masterserver;

import FinalProject.BoothElectionResult;
import FinalProject.BoothElectionResults;
import FinalProject.persons.*;
import FinalProject.communication.Comm;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import FinalProject.communication.Comm;
import FinalProject.persons.Candidate;


public class ElectionResults extends Thread implements Serializable {
	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	private ConcurrentHashMap<String, Candidate> candidates = new ConcurrentHashMap<String, Candidate>();
	private StringBuffer OfficialRecord = new StringBuffer();
	private int refreshRate;
	private Comm comm; 
	private boolean electionDone;
	
	public ElectionResults(ConcurrentHashMap<String, Candidate> candidates, int refreshRate, Comm comm){
		this.candidates=candidates;
		this.refreshRate=refreshRate;
		electionDone=false;
		this.comm=comm; 
	}
	
	public void run(){
		while(true){
			displayResults(); 

			try {
				comm.sendMessageClient((Object) this); //send periodic results
				Thread.sleep(refreshRate);
	            if(electionDone==true){
	            	comm.sendMessageClient((Object) this); //send final results
	            	System.exit(1);
	            }
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	public BoothElectionResults toBoothResults(){
		BoothElectionResult[] ber = new BoothElectionResult[candidates.size()];
		Date d = new Date();
		int vCount = 0;
		
		int i = 0;
		Enumeration<Candidate> it = candidates.elements();
     	while(it.hasMoreElements()) {
     		Candidate c = (Candidate) it.nextElement();
     		vCount += c.getVoteCount();
     		ber[i] = new BoothElectionResult(c, c.getVoteCount());
     		i++;
     	}
		
		return new BoothElectionResults(ber, vCount);
		
	}
	
    public void setElectionDone (boolean a){
    	electionDone=a; 	
    }
	
	public void outputResults(){
		BufferedWriter writer = null;

		try {
		    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Results.txt"), "utf-8"));
		    writer.write("\t\tElection Results");
		    writer.newLine();
	     	Enumeration<Candidate> it = candidates.elements();
	     	double votingPercentage;
	     	while(it.hasMoreElements()) {
	     		Candidate c = it.nextElement();
	     		votingPercentage = (c.getVoteCount()/c.getTotalVotes())*100;
	     		writer.write("  " + c.getName() + " (" + c.getParty() + ") " + c.getVoteCount() + " "+votingPercentage);
	     		writer.newLine();
	     	}
		} catch (IOException ex) {
		  System.out.println("Error writing results to file.");
		  ex.getMessage();
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}
		
	}
	public void displayResults(){
        /** Report the vote count for each candidate **/
     	log("Tally");
     	double votingPercentage;
     	Enumeration<Candidate> it = candidates.elements();
     	while(it.hasMoreElements()) {
     		Candidate c = (Candidate) it.nextElement();
     		votingPercentage = (c.getVoteCount()/c.getTotalVotes())*100;
     		System.out.println("  " + c.getName() + " (" + c.getParty() + ") " + c.getVoteCount() + " "+votingPercentage);
     	}
     	
     	
	}
	
    public void log(String s) {	
        OfficialRecord.append(s + '\n');
        System.out.println(s);
     }
}


