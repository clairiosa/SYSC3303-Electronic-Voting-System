/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Voter.java
 *
 */



package FinalProject.persons;



/**
 * Holds voter information and related functions.
 *
 * Extends Person as animals can't vote.
 *
 */
public class Voter extends Person {

	boolean registered;
	boolean vote;


	/**
	 * Empty constructor.
	 */
	public Voter(){
		super();
		registered = false;
		vote = false;
	}

	public void setRegistered(){}
    public void setVoteStatus(){}

	public boolean getVoteStatus() { return vote; }
    public boolean getRegistered() { return vote; }
}