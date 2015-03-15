/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	ReceiveMasterServerInfo.java
 *
 */

package FinalProject.masterserver;



import FinalProject.communication.Comm;
import FinalProject.persons.Candidate;
import FinalProject.persons.Voter;

import java.util.Enumeration;
import java.util.Hashtable;


public class ReceiveMasterServerInfo extends Thread {

		private Hashtable<String, Candidate> candidates = new Hashtable<String, Candidate>();
		private Hashtable<String, Voter> voters = new Hashtable<String, Voter>();
		private StringBuffer OfficialRecord = new StringBuffer();
		public int votes=0; 
		private Comm comm;
	    
	    public ReceiveMasterServerInfo(Comm comm){
	    	this.comm=comm;
	    }
	    
	    public void run() {
	    	while(true){
           	MasterServerInformation mInfo = comm.receiveMasterInfo();
               System.out.printf("District %d Information Received.\n",mInfo.getDistrictID());
               candidates = mInfo.getCandidates();
               voters = mInfo.getVoters();
               
               tallyVotes();
               
               

            	
            	

	    	}
	    }
	    public void tallyVotes(){
           /** tally votes from district **/
        	log("Tally");
        	Enumeration<Voter> itV = voters.elements();
        	while(itV.hasMoreElements()) {
        		Voter v = itV.nextElement();
        		log("  " + v.getName() );
        		if(v.hasVoted()){
        			vote(v.getCandidate().getName(),v.getName());
        			votes++;
        		}
        	}
	    }	
        	
	    public void log(String s) {	
	        OfficialRecord.append(s + '\n');
	        System.out.println(s);
	     }
	    
	    public void verify(String candidate, String voter){
	        Voter v = getVoter(voter);
	        if (v.hasVoted()) { // check that voter hasn't already voted
	          System.out.println(voter+" has already voted.");
	        }
	    }
	    /**  Record a vote **/
	    public void vote(String cand, String voter) {
	       verify(cand, voter);
	       Candidate c = getCandidate(cand);
	       c.addVote();
	       Voter v = getVoter(voter);
	       v.setVoted(true);
	       log(voter + " voted for " + cand);
	    }
	    
	    /** Retrieve a voter object
	     **/
	    public Voter getVoter(String voter) {
	       Voter v = (Voter) voters.get(voter);
	       if (v == null) {
	    	   System.out.println(voter +" has already voted.");
	    	   return null;
	       }
	       return v;
	    }
	    
	    /** Retrieve a candidate object
	     **/
	     public Candidate getCandidate(String name) {
	        Candidate c = (Candidate) candidates.get(name);
	        if (c == null) {
	        	System.out.println(name +" is not a candidate.");
	        	return null;
	        }
	        return c;
	     }

		public Hashtable<String, Candidate> getCandidates() {
			return candidates;
		}
	    
}


