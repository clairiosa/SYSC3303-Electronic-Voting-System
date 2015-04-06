/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	ElectionResults.java
 *
 */

package FinalProject.masterserver;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import FinalProject.resultdata.ResultItem;
import FinalProject.resultdata.BoothElectionResults;
import FinalProject.communication.Comm;
import FinalProject.persons.Candidate;

/**
 * This class is responsible to send peridic election updates    
 **/

public class ElectionResults extends Thread implements Serializable {

	private ConcurrentHashMap<String, Candidate> candidates = new ConcurrentHashMap<String, Candidate>();
	private StringBuffer OfficialRecord = new StringBuffer();
	private int refreshRate;
	private Comm comm;
	private boolean electionDone;

	public ElectionResults(ConcurrentHashMap<String, Candidate> candidates,
			int refreshRate, Comm comm) {
		this.candidates = candidates;
		this.refreshRate = refreshRate;
		electionDone = false;
		this.comm = comm;
	}

	public void run() {
		while (true) {
			try {

				comm.sendMessageClient(toBoothResults()); // send periodic
															// results
				Thread.sleep(refreshRate);
				if (electionDone == true) {
					comm.sendMessageClient(toBoothResults()); // send final results
					System.exit(1);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
	
	//convert an ElectionResults object to a BoothelectionResults object 
	public BoothElectionResults toBoothResults() {
		ResultItem[] ber = new ResultItem[candidates.size()];
		Date d = new Date();
		int vCount = 0;

		int i = 0;
		Enumeration<Candidate> it = candidates.elements();
		while (it.hasMoreElements()) {
			Candidate c = (Candidate) it.nextElement();
			vCount += c.getVoteCount();
			ber[i] = new ResultItem(c, c.getVoteCount());
			i++;
		}

		return new BoothElectionResults(ber, vCount);

	}

	//set the election as done 
	public void setElectionDone(boolean a) {
		electionDone = a;
	}

	
	//write the election results to a file 
	public void outputResults() {
		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("Results.txt"), "utf-8"));
			writer.write("\t\tElection Results");
			writer.newLine();
			Enumeration<Candidate> it = candidates.elements();
			double votingPercentage;
			while (it.hasMoreElements()) {
				Candidate c = it.nextElement();
				votingPercentage = (c.getVoteCount() / c.getTotalVotes()) * 100;
				writer.write("  " + c.getName() + " (" + c.getParty() + ") "
						+ c.getVoteCount() + " " + votingPercentage);
				writer.newLine();
			}
		} catch (IOException ex) {
			System.out.println("Error writing results to file.");
			ex.getMessage();
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
			}
		}

	}

	//display the results on the console 
	public void displayResults() {
		/** Report the vote count for each candidate **/
		log("Tally");
		double votingPercentage;
		Enumeration<Candidate> it = candidates.elements();
		while (it.hasMoreElements()) {
			Candidate c = (Candidate) it.nextElement();

			if (c.getTotalVotes() > 0) {
				votingPercentage = (c.getVoteCount() / c.getTotalVotes()) * 100;
				System.out.println("  " + c.getName() + " (" + c.getParty()
						+ ") " + c.getVoteCount() + " " + votingPercentage);
			}
		}

	}

	//log data 
	public void log(String s) {
		OfficialRecord.append(s + '\n');
		System.out.println(s);
	}
}
