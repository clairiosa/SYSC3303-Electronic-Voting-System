/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Ballot.java
 *
 */

package FinalProject.electionobjects;

import java.io.Serializable;

import FinalProject.persons.Candidate;
import FinalProject.persons.Voter;

public class Ballot implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// added according to Nate's UML
	private String district;
	private Voter voter;
	private Candidate candidate;

	public Ballot(String district, Voter voter, Candidate candidate) {
		super();
		this.district = district;
		this.voter = voter;
		this.candidate = candidate;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public Voter getVoter() {
		return voter;
	}

	public void setVoter(Voter voter) {
		this.voter = voter;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

}
