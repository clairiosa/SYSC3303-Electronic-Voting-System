package FinalProject.resultdata;

import FinalProject.persons.Candidate;

public class ResultItem implements java.io.Serializable {
	public Candidate candidate;
	public int count; 
	
	public ResultItem(Candidate candidate, int count){
		this.candidate = candidate;
		this.count = count;
	}
}
