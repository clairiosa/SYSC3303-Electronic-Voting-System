/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	CommInterface.java
 *
 */



package FinalProject.communication;



import FinalProject.Ballot;
import java.net.InetAddress;

interface CommInterface {
	public void connect(InetAddress parentServer, int port);
	public void disconnect();

	public void submitVote(Ballot ballot);
	public void receiveVote(Ballot ballot);
}