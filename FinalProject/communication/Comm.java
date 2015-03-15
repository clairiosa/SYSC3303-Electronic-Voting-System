/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Comm.java
 *
 */


package FinalProject.communication;


import FinalProject.Ballot;
import FinalProject.persons.Candidate;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Comm implements CommInterface {

    DatagramSocket socket;

    public Comm() {
    }

    /**
     *
     * @return
     */
    @Override
    public int connect() {
        return -1;
    }

    /**
     *
     * @param parentServer
     * @param port
     * @return
     */
    @Override
    public int connect(InetAddress parentServer, int port) {
        return -1;
    }

    /**
     *
     * @return
     */
    @Override
    public int disconnect() {
        return -1;
    }

    /**
     *
     * @param ballot
     * @return
     */
    @Override
    public int sendMessage(Ballot ballot) {
        return 0;
    }

    /**
     *
     * @param candidate
     * @return
     */
    @Override
    public int sendMessage(Candidate candidate) {
        return 0;
    }




    // Master server stuff.

    /*
    @Override
    public MasterServerInformation receiveMasterInfo() {
        return null;
    }

    @Override
    public void sendElectionResults(ElectionResults results) {

    }

    @Override
    public void StartMasterServer(int serverPort, int destinationPort) {

    }

    @Override
    public void CloseMasterServer() {

    }
    */

    @Override
    public <T> T receiveMessage() {
        return null;
    }
}
