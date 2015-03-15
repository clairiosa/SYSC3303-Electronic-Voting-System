/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	ListenThread.java
 *
 *  Thread for listening for packets, and handing them off to a worker thread specific to each connection.
 *
 *  TODO-Dave Add comments.  Add disconnect functions.  Add timeout thread + functions if you want to be excessively thorough.
 *
 */

package FinalProject.communication;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

class ListenThread implements Runnable {

    private static final int MAX_PACKET_SIZE = 512;


    HashMap<String, Connection> connectionHashMap = new HashMap<String, Connection>();
    int listenPort;
    DatagramSocket listenSocket;


    /**
     *
     * @param listenPort
     * @throws SocketException
     */
    public ListenThread(int listenPort) throws SocketException {
        this.listenPort = listenPort;
        listenSocket = new DatagramSocket(listenPort);
    }


    /**
     *
     */
    @Override
    public void run() {
        byte[] buffer = new byte[MAX_PACKET_SIZE];
        DatagramPacket incomingPacket;

        while (true) {
                incomingPacket = new DatagramPacket(buffer, buffer.length);
            try {
                listenSocket.receive(incomingPacket);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }

            String connectionKey = incomingPacket.getAddress().toString() + ":" + incomingPacket.getPort();
            Connection connection = connectionHashMap.get(connectionKey);
            if (connection == null) connection = createConnection(incomingPacket.getAddress(), incomingPacket.getPort());

            try {
                connection.packetQueue.put(incomingPacket);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     *
     * @param address
     * @param port
     * @return
     */
    private Connection createConnection(InetAddress address, int port) {
        Connection newConnection = new Connection(address, port);
        WorkerThread worker = new WorkerThread(newConnection);
        newConnection.setThreadId(worker.getThreadId());

        Thread workerThread = new Thread(worker);
        workerThread.start();
        return newConnection;
    }


}
