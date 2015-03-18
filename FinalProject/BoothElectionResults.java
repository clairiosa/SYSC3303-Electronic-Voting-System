package FinalProject;

import java.util.Date;

public class BoothElectionResults implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BoothElectionResult[] results;
	public int totalVotes;
	public Date generated;
	
	public BoothElectionResults(BoothElectionResult[] results, int totalVotes){
		this.results = results;
		this.totalVotes = totalVotes;
		this.generated = new Date();
	}
}
