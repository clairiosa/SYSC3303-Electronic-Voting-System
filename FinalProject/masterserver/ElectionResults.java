/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	ElectionResults.java
 *
 */


package FinalProject.masterserver;

import java.util.Enumeration;
import java.util.Hashtable;


public class ElectionResults extends Thread {
	private Hashtable<String, Candidate> candidates = new Hashtable<String, Candidate>();
	private StringBuffer OfficialRecord = new StringBuffer();
	private int refreshRate;
	private Comm comm; 
	
	ElectionResults(Hashtable<String, Candidate> candidates, int refreshRate, Comm comm){
		this.candidates=candidates;
		this.refreshRate=refreshRate;
		this.comm=comm; 
	}
	
	public void run(){
		while(true){
			displayResults(); 
			comm.sendElectionResults(this);
			try {
				Thread.sleep(refreshRate);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void displayResults(){
        /** Report the vote count for each candidate **/
     	log("Tally");
     	Enumeration<Candidate> it = candidates.elements();
     	while(it.hasMoreElements()) {
     		Candidate c = (Candidate) it.nextElement();
     		double votingPercentage = (c.getVoteCount()/c.getTotalVotes())*100;
     		log("  " + c.getName() + " (" + c.getParty() + ") " + c.getVoteCount() + " "+votingPercentage);
     	}
     	
     	
	}
	
    public void log(String s) {	
        OfficialRecord.append(s + '\n');
        System.out.println(s);
     }
}
