/* 
 * Author: Damian Polan
 */

package FinalProject.districtserver;

import java.net.InetAddress;

import FinalProject.Ballot;
import FinalProject.communication.Comm;
import FinalProject.masterserver.ElectionResults;
import FinalProject.masterserver.MasterServerInformation;
import FinalProject.persons.Person;

public class DistrictServer implements Runnable {

	public static void main(String args[]) {

	}

	Comm districtComm;

	String masterAddress;
	int masterPort;
	int port;
	int uniqueDistrictId;

	// election data table. The table contains the profile of each voter and the
	// list of candidates.
	MasterServerInformation masterServerInfo;

	// Election results. pulled from master server
	ElectionResults results;

	public DistrictServer(int port, String masterAddress, int masterPort,
			int uniqueDistrictId) {
		this.port = port;
		this.masterAddress = masterAddress;
		this.masterPort = masterPort;
		this.uniqueDistrictId = uniqueDistrictId;
	}

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
		try {
			// listen for incoming messages. messages can come from booths or
			// master server
			Object recievedMessage = districtComm.getMessageBlocking();

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
				this.masterServerInfo = (MasterServerInformation) recievedMessage;
			} else if (recievedMessage instanceof ElectionResults) {
				
			} else if (recievedMessage instanceof Person) {
				
			} else if (recievedMessage instanceof Ballot) {
				
			} else if (recievedMessage instanceof String) {
				if (recievedMessage.equals("status")) {
					//send back the ElectionResults to booth
					districtComm.sendMessageReply(results);
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
