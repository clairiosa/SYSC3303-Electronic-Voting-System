/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Candidate.java
 *
 */



package FinalProject.persons;



/**
 * Holds candidate information and related functions.
 *
 * Extends Voter as candidates are allowed to cast a ballot.
 *
 */
public class Candidate extends Voter{

	String party;


	/**
	 * Empty constructor.
	 */
	public Candidate(){
		super();
		party = "";
	}

	public void setParty() {}
	public String getParty() { return party; }
}