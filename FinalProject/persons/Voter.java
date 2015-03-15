/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Voter.java
 *
 */



package FinalProject.persons;


public class Voter extends Person {
	private String party;
	private Candidate votedFor;
	private boolean voted;
	private boolean registered;

	
	Voter(String name, String party) {
		super(name);
		this.party = party;
		voted = false;
		votedFor = null;
	}
	

	public String getParty(){
		return party;
	}
	
	public void setParty(String party){
		this.party=party;
	}
	
	public boolean hasVoted(){
		return voted; 
	}
	
	public void setVoted(boolean v){
		voted=v; 
	}
	
	
	public void setCandidate(Candidate c){
		votedFor=c;
	}
	
	public Candidate getCandidate(){
		return votedFor;
	}
	
	public void setRegistered(boolean reg){registered=reg;}
	public boolean getRegistered() { return registered; }
	
   }
