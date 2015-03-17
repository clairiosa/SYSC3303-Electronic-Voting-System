package persons;

import java.util.Date;

public class ElectionResults implements java.io.Serializable {
	public ElectionResult[] results;
	public int totalVotes;
	public Date generated;
	
	ElectionResults(ElectionResult[] results, int totalVotes){
		this.results = results;
		this.totalVotes = totalVotes;
		this.generated = new Date();
	}
}
