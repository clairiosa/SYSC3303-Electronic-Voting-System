/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Voter.java
 *
 */

package FinalProject.persons;

public class Voter extends Person {
	private static final long serialVersionUID = 1L;

	private String party;
	private Candidate votedFor;
	private boolean voted;
	private boolean registered;

	public Voter(String name, String address, String postal, String city,
			String province, String districtId, String user, String pin,
			String party, Candidate votedFor, boolean voted, boolean registered) {
		super(name, address, postal, city, province, districtId, user, pin);
		this.party = party;
		this.votedFor = votedFor;
		this.voted = voted;
		this.registered = registered;
	}

	public Voter(String name, String party) {
		super(name);
		this.party = party;
		voted = false;
		votedFor = null;
	}

	public Voter(String name, String party, String districtId) {
		super(name);
		this.party = party;
		this.districtId = districtId;
		voted = false;
		votedFor = null;
	}
	
	public boolean equals(Voter v){
		return (v.party.equals(this.party) && v.name.equals(this.name));
	}

	public String getParty() {
		return party;
	}

	public void setParty(String party) {
		this.party = party;
	}

	public boolean hasVoted() {
		return voted;
	}

	public void setVoted(boolean v) {
		voted = v;
	}

	public void setCandidate(Candidate c) {
		votedFor = c;
	}

	public Candidate getCandidate() {
		return votedFor;
	}

	public void setRegistered(boolean reg) {
		registered = reg;
	}

	public boolean getRegistered() {
		return registered;
	}

}
