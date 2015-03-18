package FinalProject;

import FinalProject.persons.Candidate;

public class BoothElectionResult implements java.io.Serializable {
	public Candidate candidate;
	public int count; 
	
	public BoothElectionResult(Candidate candidate, int count){
		this.candidate = candidate;
		this.count = count;
	}
}
