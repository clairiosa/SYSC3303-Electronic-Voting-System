package FinalProject.persons;

public class ElectionResult implements java.io.Serializable {
	public Candidate candidate;
	public int count; 
	
	public ElectionResult(Candidate candidate, int count){
		this.candidate = candidate;
		this.count = count;
	}
}
