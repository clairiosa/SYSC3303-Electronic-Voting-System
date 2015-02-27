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

interface CommInterface {
	public void connect(InetAddress parentServer, int port);
	public void disconnect();

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

    /**
     * Returns the message received.  The class using this function needs to determine
     * the type of message being received using instanceof or getClass().
     *
     * @return      The object
     */
	public <T> T receiveMessage();

    //TODO-dave Add other methods of receiving message (ie only receive ballot)
}