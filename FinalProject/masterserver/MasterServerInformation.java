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

public class MasterServerInformation implements Serializable {

	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	protected ConcurrentHashMap<String, Candidate> candidates;
	protected ConcurrentHashMap<String, Voter> voters;
	private int districtID;

	public MasterServerInformation() {
		candidates = new ConcurrentHashMap<String, Candidate>();
		voters = new ConcurrentHashMap<String, Voter>();
	}

	public MasterServerInformation(ConcurrentHashMap<String, Candidate> candidates,
			ConcurrentHashMap<String, Voter> voters) {
		this.candidates = candidates;
		this.voters = voters;
	}

	public ConcurrentHashMap<String, Voter> getVoters() {
		return voters;
	}

	public ConcurrentHashMap<String, Candidate> getCandidates() {
		return candidates;
	}


	public int getDistrictID() {
		return districtID;
	}

	public boolean addCandidate(Candidate c) {
		candidates.put(c.getName(), c);
		return true;
	}

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

