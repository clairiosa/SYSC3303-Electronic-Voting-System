/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Candidate.java
 *
 */

package FinalProject.persons;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class Candidate extends Voter {
	private int voteCount;
	private double votingPercentage;
	private static int totalVotes;

	public Candidate(String name, String party) {
		super(name, party);
		voteCount = 0;
		votingPercentage = 0.0;
	}

	public void addVotes(int votes) {
		voteCount += votes;
		totalVotes += votes;
	}

	public int getVoteCount() {
		return voteCount;
	}

	public void addVote() {
		voteCount++;
		totalVotes++;
	}

	public double getVotingPercentage() {
		return 100 * ((double) voteCount / (double) totalVotes);
	}

	public boolean setVotingPercentage(double amt) {
		if (amt >= 0 && amt <= 100) {
			votingPercentage = amt;
			return true;
		}
		return false;
	}

	public int getTotalVotes() {
		return totalVotes;
	}

	public void setVoteCount(int vCount) {
		voteCount = vCount;
	}

	public void setTotalVotes(int tVotes) {
		totalVotes = tVotes;
	}

	public int getFinalTotalVotes(
			ConcurrentHashMap<String, Candidate> candidates) {
		Enumeration<Candidate> it = candidates.elements();
		int tVotes = 0;
		while (it.hasMoreElements()) {
			Candidate c = (Candidate) it.nextElement();
			tVotes += c.getVoteCount();
		}
		totalVotes = tVotes;
		return totalVotes;
	}

}
