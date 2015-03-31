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

/**
 * This class receives election information from the districts and adds the information to the master hashmap  
 **/

public class ReceiveMasterServerInfo extends Thread {

	private ConcurrentHashMap<String, Candidate> candidates;
	private ConcurrentHashMap<String, Voter> voters = new ConcurrentHashMap<String, Voter>();
	private StringBuffer OfficialRecord = new StringBuffer();
	public int votes = 0;
	private boolean electionDone = false;
	private Comm comm;

	public ReceiveMasterServerInfo(Comm comm, ConcurrentHashMap<String, Candidate> candidates) {
		this.comm = comm;
		this.candidates = candidates;
	}

	//thread run method 
	public void run() {
		while (true) {
			try {
				Object info = comm.getMessageBlocking(); //wait to receive a voter object 
				//if a voter object is received add the vote information 
				if (info instanceof Voter) {
					Voter v = (Voter) info;
					System.out.println("District " + v.getName()+ " Information Received.\n");
					addInformationVoter(v);
				}
				//kill thread if the election is done 
				if (electionDone == true) {
					System.out.println("Election is completed");
					System.exit(1);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	private void addInformationVoter(Voter v) {
		Candidate c = candidates.get(v.getCandidate().getName());
		c.addVote();
	}



	//set the election as done 
	public void setElectionDone(boolean a) {
		electionDone = a;
	}

	//log data 
	public void log(String s) {
		OfficialRecord.append(s + '\n');
		System.out.println(s);
	}

	/**
	 * Retrieve a voter object
	 **/
	public Voter getVoter(String voter) {
		Voter v = (Voter) voters.get(voter);
		if (v == null) {
			System.out.println(voter + " has already voted.");
			return null;
		}
		return v;
	}

	/**
	 * Retrieve a candidate object
	 **/
	public Candidate getCandidate(String name) {
		Candidate c = (Candidate) candidates.get(name);
		if (c == null) {
			System.out.println(name + " is not a candidate.");
			return null;
		}
		return c;
	}

	//get the candidates hashmap 
	public ConcurrentHashMap<String, Candidate> getCandidates() {
		return candidates;
	}

}
