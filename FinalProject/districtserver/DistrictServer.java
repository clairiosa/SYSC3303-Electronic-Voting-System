/* 
 * Author: Damian Polan
 */

package FinalProject.districtserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Date;
import java.util.Enumeration;

import FinalProject.Ballot;
import FinalProject.BoothElectionResult;
import FinalProject.BoothElectionResults;
import FinalProject.Credential;
import FinalProject.communication.Comm;
import FinalProject.masterserver.ElectionResults;
import FinalProject.masterserver.MasterServerInformation;
import FinalProject.persons.Candidate;
import FinalProject.persons.Person;
import FinalProject.persons.Voter;

/**
 * Acts as an intermediate between Booth and MasterServer
 * 
 * @author damianpolan
 * 
 */
public class DistrictServer implements Runnable {

	public static void main(String args[]) {
		if (args.length >= 4) {
			DistrictServer district = new DistrictServer(
					Integer.parseInt(args[0]), args[1],
					Integer.parseInt(args[2]), args[3]);
			if (args.length >= 6)
				district.fakeData(args[4], args[5]);
			district.start();
		} else {
			System.out
					.println("Arguments[4]: port, masterAddress, masterPort, uniqueDistrictId, (optional: candidatePath, votersPath)");
		}
	}

	// communication portal for this district server
	Comm districtComm;

	// connection information
	String masterAddress;
	int masterPort;
	int port;
	String uniqueDistrictId;

	// election data table. The table contains the profile of each voter and the
	// list of candidates.
	MasterServerInformation masterServerInfo; // TEMPORARY until MasterServer
												// rework

	// Election results. pulled from master server
	ElectionResults results;

	/**
	 * Constructor
	 * 
	 * @param port
	 * @param masterAddress
	 * @param masterPort
	 * @param uniqueDistrictId
	 */
	public DistrictServer(int port, String masterAddress, int masterPort,
			String uniqueDistrictId) {
		this.port = port;
		this.masterAddress = masterAddress;
		this.masterPort = masterPort;
		this.uniqueDistrictId = uniqueDistrictId;
	}

	boolean fake = false;

	private BoothElectionResults electionResults;

	public void fakeData(String candidatePath, String voterPath) {

		// from MasterServer.java

		MasterServerInformation lists = new MasterServerInformation();
		File votersFile = new File(voterPath);
		File candidatesFile = new File(candidatePath);

		try {
			FileInputStream fis1 = new FileInputStream(votersFile);

			// Construct BufferedReader from InputStreamReader
			BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1));
			String line = null;
			String voter = null;
			int district;
			while ((line = br1.readLine()) != null) {
				district = Integer.valueOf(line);
				voter = br1.readLine();
				lists.addVoter(new Voter(voter, "", district + ""));
			}
			br1.close();
		} catch (Exception e) {
			System.out.println("Error reading voters file.");
			System.exit(-1);
		}

		String candidate = null;
		String party = null;
		try {
			FileInputStream fis2 = new FileInputStream(candidatesFile);
			// Construct BufferedReader from InputStreamReader
			BufferedReader br2 = new BufferedReader(new InputStreamReader(fis2));
			while ((party = br2.readLine()) != null) {
				candidate = br2.readLine();
				lists.addCandidate(new Candidate(candidate, party));
			}

			br2.close();
		} catch (Exception e) {
			System.out.println("Error reading Candidates file.");
			System.exit(-1);
		}
		
		masterServerInfo = lists;

		// masterServerInfo = new MasterServerInformation();
		// masterServerInfo.addCandidate(new Candidate("Nate", "Liberal"));
		// masterServerInfo.addCandidate(new Candidate("David", "NDP"));
		//
		// masterServerInfo.addVoter(new Voter("Damian", "", "", "", "", "0",
		// "Damian", "0", "", null, false, false));
		// masterServerInfo.addVoter(new Voter("Nate", "", "", "", "", "0",
		// "Nate", "9", "", null, false, false));
		// masterServerInfo.addVoter(new Voter("Nate Bosscher", "", "", "", "",
		// "0",
		// "Nate Bosscher", "9", "", null, false, false));
		//
		// masterServerInfo.addVoter(new Voter("Jon", "", "", "", "", "1",
		// "Jon",
		// "0", "", null, false, false));
		//
		// Enumeration<Candidate> c =
		// masterServerInfo.getCandidates().elements();
		// int i = 0;
		// while(c.hasMoreElements()){
		// c.nextElement();
		// i++;
		// }
		//
		// BoothElectionResult[] r = new BoothElectionResult[i];
		// BoothElectionResult r1;
		// c = masterServerInfo.getCandidates().elements();
		// i = 0;
		// while(c.hasMoreElements()){
		// r[i++] = new BoothElectionResult(c.nextElement(), 0);
		//
		// }
		//
		// electionResults = new BoothElectionResults(r, 0);

		fake = true;
	}

	/**
	 * Starts the server
	 */
	public void start() {
		System.out.println("DistrictServer Started\n");
		try {
			districtComm = new Comm(port);
			districtComm.connectToParent(InetAddress.getByName(masterAddress),
					masterPort);
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// a single thread is needed to listen for all incoming / send outgoing
		this.run();
	}

	@Override
	public void run() {
		while (true) {
			try {
				// listen for incoming messages. messages can come from booths
				// or
				// master server
				Object recievedMessage = districtComm.getMessageBlocking();
				System.out.println(recievedMessage);
				// from master server:
				// Receive MasterServerInformation containing all candidate and
				// voters
				// periodically receive ElectionResults
				// periodically send MasterServerInformation to master server

				// from client:
				// "status" -> ElectionResults
				// Person -> "true" or "false" registration confirmed or not
				// Ballot -> "true" or "false" vote valid (must be registered)

				if (recievedMessage instanceof MasterServerInformation) {
					if (!fake)
						this.masterServerInfo = (MasterServerInformation) recievedMessage;

				} else if (recievedMessage instanceof ElectionResults) {
					// this.results = (BoothElectionResults) recievedMessage;

				} else if (recievedMessage instanceof Person) { // register the
					// person
					Voter localVoter = this.masterServerInfo
							.getVoter(((Person) recievedMessage).getName());

					if (localVoter != null
							&& localVoter.getDistrictId().equals(
									uniqueDistrictId)
							&& localVoter.getRegistered() == false) {
						localVoter.setRegistered(true);
						districtComm.sendMessageReply("true");
					} else
						districtComm.sendMessageReply("false");

				} else if (recievedMessage instanceof Ballot) { // vote with
																// this
					// person
					Ballot voteBallot = (Ballot) recievedMessage;

					// district must be matching to vote AND must be registered
					Voter v = voteBallot.getVoter();

					if (masterServerInfo.getVoter(v.getName()).getRegistered()) {
						Voter localVoter = this.masterServerInfo
								.getVoter(voteBallot.getVoter().getName());

						// make the vote
						localVoter.setCandidate(voteBallot.getCandidate());
						localVoter.setVoted(true);

						for (int i = 0; i < electionResults.results.length; i++)
							if (electionResults.results[i].candidate
									.equals(voteBallot.getCandidate())) {
								electionResults.results[i].count++;
							}

						electionResults.totalVotes++;
						electionResults.generated = new Date();

						districtComm.sendMessageReply("true");

						// TEMPORARY - until structure rework with Jon.
						if (!fake)
							districtComm.sendMessageParent(masterServerInfo);

					} else {
						districtComm.sendMessageReply("false");
					}

				} else if (recievedMessage instanceof Credential) {
					Credential creds = (Credential) recievedMessage;
					// check user match
					if (masterServerInfo.getVoter(creds.getUser()).getUser()
							.equals(creds.getUser())
							&& masterServerInfo.getVoter(creds.getUser())
									.getPin().equals(creds.getPin())) {

						districtComm.sendMessageReply("true");
					} else {
						districtComm.sendMessageReply("false");
					}

				} else if (recievedMessage instanceof String) {
					if (recievedMessage.equals("status")) {
						// send back the ElectionResults to booth

						districtComm.sendMessageReply(electionResults);
					} else if (recievedMessage.equals("candidates")) {

						Candidate[] c = new Candidate[masterServerInfo
								.getCandidates().size()];

						int i = 0;

						Enumeration<String> str = masterServerInfo
								.getCandidates().keys();
						String s;
						while (str.hasMoreElements()) {
							s = str.nextElement();
							c[i] = masterServerInfo.getCandidates().get(s);
							i++;
						}
						districtComm.sendMessageReply(c);

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
