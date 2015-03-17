/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Ballot.java
 *
 */


package FinalProject;


import java.io.Serializable;

import FinalProject.persons.Candidate;
import FinalProject.persons.Voter;

public class Ballot implements Serializable{

	//added according to Nate's UML
	public String district;
	public Voter voter;
	public Candidate candidate;
}
