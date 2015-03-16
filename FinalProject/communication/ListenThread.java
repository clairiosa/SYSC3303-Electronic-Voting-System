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
import java.lang.Thread;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

class ListenThread implements Runnable {

    private static final int MAX_PACKET_SIZE = 512;


    HashMap<String, Connection> connectionHashMap = new HashMap<String, Connection>();
    int listenPort;
    DatagramSocket listenSocket;
    BlockingQueue<Object> receivedObjectQueue;
    Semaphore queueSemaphore;



    public ListenThread(int listenPort, BlockingQueue<Object> receivedObjectQueue, Semaphore queueSemaphore) throws SocketException {
        this.listenPort = listenPort;
        listenSocket = new DatagramSocket(listenPort);
        this.receivedObjectQueue = receivedObjectQueue;
        this.queueSemaphore = queueSemaphore;
    }



    @Override
    public void run() {
        byte[] buffer = new byte[MAX_PACKET_SIZE];

        while (true) {
            DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
            try {
                listenSocket.receive(incomingPacket);
            } catch (IOException e) {
                break;
            }

            String connectionKey = incomingPacket.getAddress().toString() + ":" + incomingPacket.getPort();
            System.out.println("Message received from " + connectionKey);
            Connection connection = connectionHashMap.get(connectionKey);
            if (connection == null) try {
                connection = createConnection(incomingPacket.getAddress(), incomingPacket.getPort());
            } catch (SocketException e) {
                break;
            }


            System.out.println(connection.getPort());

            try {
                System.out.println("~~~~~~~!~!~!~!~Listener " + Packets.decodePacket(incomingPacket));
                connection.incomingPacketQueue.put(incomingPacket);
                System.out.println("################ PLACED IN " + connection.getPort());
            } catch (InterruptedException e) {
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        for (Connection connection : connectionHashMap.values()) {
            connection.getWorkerThread().interrupt();
            try {
                connection.getWorkerThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    Connection createConnection(InetAddress address, int port) throws SocketException {
        Connection newConnection = new Connection(address, port);
        WorkerThread worker = new WorkerThread(newConnection, listenSocket, receivedObjectQueue, queueSemaphore);
        Thread workerThread = new Thread(worker);
        newConnection.setWorkerThread(workerThread);

        connectionHashMap.put(address.toString() + ":" + port, newConnection);

        workerThread.start();
        return newConnection;
    }

    void disconnect() throws InterruptedException {
        for (Connection connection : connectionHashMap.values()) {
            connection.getWorkerThread().interrupt();
            connection.getWorkerThread().join();
        }
        listenSocket.close();
    }


}
