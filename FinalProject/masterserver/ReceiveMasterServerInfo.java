/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	ReceiveMasterServerInfo.java
 *
 */

package FinalProject.masterserver;



import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import FinalProject.communication.Comm;
import FinalProject.persons.Candidate;
import FinalProject.persons.Voter;


public class ReceiveMasterServerInfo extends Thread {

		private ConcurrentHashMap<String, Candidate> candidates = new ConcurrentHashMap<String, Candidate>();
		private ConcurrentHashMap<String, Voter> voters = new ConcurrentHashMap<String, Voter>();
		private StringBuffer OfficialRecord = new StringBuffer();
		public int votes=0; 
		private boolean electionDone=false;
		private Comm comm;
	    private int districtCount; 
	    
	    public ReceiveMasterServerInfo(Comm comm){
	    	this.comm=comm;
	    	districtCount=0;
	    }
	    
	    public void run() {
	    	while(true){
	    	   Object info;
			try {
				info = comm.getMessageBlocking();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
	    	   if(info instanceof MasterServerInformation){
	    		   MasterServerInformation mInfo = (MasterServerInformation)info;
	    		   System.out.printf("District %d Information Received.\n",mInfo.getDistrictID());
	    		   ConcurrentHashMap<String, Candidate> tempCandidates = mInfo.getCandidates();
	    		   addInformation(tempCandidates);
	    		   districtCount++;
	    	   }
               //voters = mInfo.getVoters();
               //tallyVotes();
               
               if(electionDone==true){
            	   System.out.println("Election is completed");
            	   System.exit(1);
               }
               
  	
	    	}
	    }
	    
	    public int getDistrictCount(){
	    	return districtCount;
	    }
	    public void addInformation (ConcurrentHashMap<String, Candidate> tempCandidates){
	    	Enumeration<Candidate> it = tempCandidates.elements();
	     	while(it.hasMoreElements()) {
	     		Candidate c = it.nextElement();
	     		candidates.get(c.getName()).addVotes(c.getVoteCount());
	     	}
	    }
	    public void setElectionDone (boolean a){
	    	electionDone=a; 	
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

		public ConcurrentHashMap<String, Candidate> getCandidates() {
			return candidates;
		}
	    
}


