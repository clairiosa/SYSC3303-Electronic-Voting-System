/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Candidate.java
 *
 */



package FinalProject.persons;


public class Candidate extends Voter {
	private int voteCount;
	private double votingPercentage;
	private static int totalVotes;
	
	Candidate(String name, String party) {
		super(name, party);
		voteCount = 0;
		votingPercentage=0.0;
	}
	

	public int getVoteCount(){
		return voteCount;
	}
	
	public void addVote(){
		voteCount++; 
		totalVotes++;
	}
	public double getVotingPercentage(){
		return votingPercentage;
	}
	public boolean setVotingPercentage(double amt){
		if (amt >=0 && amt<=100){
			votingPercentage=amt; 
			return true; 
		}
		return false;
	}
	
	public int getTotalVotes(){
		return totalVotes;
	}
	
   }
