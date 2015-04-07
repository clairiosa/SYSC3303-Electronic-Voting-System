/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	MasterServerInformation.java
 *
 */


package FinalProject.masterserver;




import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import FinalProject.persons.Candidate;
import FinalProject.persons.Voter;

//information object passed between districts and the MasterServer 

/**
 * This class is the information object passed from the MasterServer to all the districts  
 **/

public class MasterServerInformation implements Serializable {

	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	protected ConcurrentHashMap<String, Candidate> candidates;  //candidates information 
	protected ConcurrentHashMap<String, Voter> voters;  //voter information 

	//constructor 
	public MasterServerInformation() {
		candidates = new ConcurrentHashMap<String, Candidate>();
		voters = new ConcurrentHashMap<String, Voter>();
	}

	//constructor 
	public MasterServerInformation(ConcurrentHashMap<String, Candidate> candidates,ConcurrentHashMap<String, Voter> voters) {
		this.candidates = candidates;
		this.voters = voters;
	}

	public ConcurrentHashMap<String, Voter> getVoters() {
		return voters;
	}

	public ConcurrentHashMap<String, Candidate> getCandidates() {
		return candidates;
	}

	
	//add a candidate 
	public boolean addCandidate(Candidate c) {
		candidates.put(c.getName(), c);
		return true;
	}

	
	//add voter 
	public boolean addVoter(Voter v) {
		voters.put(v.getName(), v);
		return true;
	}

	/**
	 * Retrieve a voter object
	 **/
	public Voter getVoter(String voter) {
		Voter v = voters.get(voter.trim());
		if (v == null) {
			System.out.println("Not a voter.");
			return null;
		}
		return v;
	}

	/**
	 * Retrieve a candidate object
	 **/
	public Candidate getCandidate(String name) {
		Candidate c = candidates.get(name);
		if (c == null) {
			System.out.println("Not a candidate.");
			return null;
		}
		return c;
	}

}

