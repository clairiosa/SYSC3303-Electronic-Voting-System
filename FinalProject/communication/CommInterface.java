/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	CommInterface.java
 *
 */



package FinalProject.communication;



import FinalProject.Ballot;
import FinalProject.persons.Candidate;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.TimeoutException;

interface CommInterface {
    public int connect() throws TimeoutException, SocketException;
	public int connect(InetAddress parentServer, int port) throws TimeoutException;
	public int disconnect();

    /**
     * Sends the object specified to the target.  Returns 0 if success, otherwise returns
     * an error code.
     *
     * @param ballot
     * @return
     */
    //TODO-dave Add way to specify target (String?)
    public int sendMessage(Ballot ballot);
    public int sendMessage(Candidate candidate);



    // Master server stuff.
    /*
    public MasterServerInformation receiveMasterInfo(){
		
	}
	//not sure about params, feel free to remove them
	public void StartMasterServer(int serverPort, int destinationPort){
		
	}
	
	public void CloseMasterServer(){
		
	}
	
	public void sendElectionResults(ElectionResults results){
		
	}
	*/

    /**
     * Returns the message received.  The class using this function needs to determine
     * the type of message being received using instanceof or getClass().
     *
     * @return      The object
     */
	public <T> T receiveMessage();

    //TODO-dave Add other methods of receiving message (ie only receive ballot)
}
