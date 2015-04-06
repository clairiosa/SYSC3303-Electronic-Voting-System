package FinalProject.resultdata;

import java.util.Date;

public class BoothElectionResults implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResultItem[] results;
	public int totalVotes;
	public Date generated;

	public BoothElectionResults(ResultItem[] results, int totalVotes) {
		this.results = results;
		this.totalVotes = totalVotes;
		this.generated = new Date();
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < results.length; i++) {
			s += results[i].candidate.getName() + ", Votes: "
					+ results[i].count + "\n";
		}

		s += "Total Votes: " + totalVotes;

		return s;
	}
}
