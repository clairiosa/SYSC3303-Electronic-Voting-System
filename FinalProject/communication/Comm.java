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

import java.net.SocketException;
import java.util.concurrent.TimeoutException;

public class Comm implements CommInterface {

    DatagramSocket socket;

    public Comm() {
    }

    /**
     *
     * @return
     */
    @Override
    public int connect() throws TimeoutException, SocketException {
        return 0;
    }

    /**
     *
     * @param parentServer
     * @param port
     * @return
     */
    @Override
    public int connect(InetAddress parentServer, int port) throws TimeoutException {
        return 0;
    }

    /**
     *
     * @return
     */
    @Override
    public int disconnect() {
        return 0;
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
