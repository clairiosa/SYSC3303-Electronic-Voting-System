/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	MasterServerInformation.java
 *
 */


package FinalProject.masterserver;




import FinalProject.persons.Candidate;
import FinalProject.persons.Voter;
import java.net.DatagramSocket;
import java.util.*;

public class MasterServerInformation {
	//private ArrayList<Voter> voterList;
	//private ArrayList<Candidate> candidateList;
	protected Hashtable<String, Candidate> candidates;
	protected Hashtable<String, Voter> voters;
	private DatagramSocket districtServer;
	private int districtID;
	
	public MasterServerInformation(){
		candidates = new Hashtable<String, Candidate>();
		voters = new Hashtable<String, Voter>();
	}
	
	public MasterServerInformation(Hashtable<String, Candidate> candidates, Hashtable<String, Voter> voters){
		this.candidates = candidates; 
		this.voters = voters; 
	}
	
	public Hashtable<String, Voter> getVoters(){
		return voters; 
	}
	
	public Hashtable<String, Candidate> getCandidates(){
		return candidates; 
	}
	
	public DatagramSocket getDistrictServer(){
		return districtServer; 
	}
	public int getDistrictID(){
		return districtID; 
	}
	
	public boolean addCandidate(Candidate c){
		candidates.put(c.getName(), c);
		return true; 
	}
	
	public boolean addVoter(Voter v){
		if(v.hasVoted()){
			voters.put(v.getName(), v);
			return true;
		}
		System.out.printf("Voter %s has already voted.");
		return false;

	}
	
	   /** Retrieve a voter object
	    **/
	public Voter getVoter(String voter)  {
	      Voter v = voters.get(voter);
	      if (v == null) {
	    	  System.out.println("Not a voter.");
	    	  return null;
	      }
	      return v;
	   }
	   
	   /** Retrieve a candidate object
	   **/
	public Candidate getCandidate(String name)  {
	      Candidate c = candidates.get(name);
	      if (c == null) {
	    	  System.out.println("Not a candidate.");
	    	  return null;
	      }
	      return c;
	   }
	
	
}
