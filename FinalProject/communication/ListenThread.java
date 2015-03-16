/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	ListenThread.java
 *
 *  Thread for listening for packets, and handing them off to a worker thread specific to each connection.
 *
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

    private static final int MAX_PACKET_SIZE = 8192;


    HashMap<String, Connection> connectionHashMap = new HashMap<String, Connection>();
    private DatagramSocket listenSocket;
    private BlockingQueue<Object> receivedObjectQueue;
    private Semaphore queueSemaphore;


    /**
     * Sole constructor for the ListenThread object.  Only one is expected to exist for each implementation of Comm.
     *
     * @param listenPort            Port to open the listen socket on.
     * @param receivedObjectQueue   The queue to place received objects into for access from the class using Comm.
     * @param queueSemaphore        A semaphore preventing multiple workers from writing to the queue at once.
     *                              Potentially unnecessary, added as a precaution while bug testing.
     * @throws SocketException
     */
    public ListenThread(int listenPort, BlockingQueue<Object> receivedObjectQueue, Semaphore queueSemaphore) throws SocketException {
        listenSocket = new DatagramSocket(listenPort);
        this.receivedObjectQueue = receivedObjectQueue;
        this.queueSemaphore = queueSemaphore;
    }


    /**
     * Thread run, grabs packets off the socket and sends them to their respective worker threads
     */
    @Override
    public void run() {

        while (true) {
            // Grab the packet.
            byte[] buffer = new byte[MAX_PACKET_SIZE];
            DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
            try {
                listenSocket.receive(incomingPacket);
            } catch (IOException e) {
                break;
            }

            // Grab the associated open connection.
            String connectionKey = incomingPacket.getAddress().toString() + ":" + incomingPacket.getPort();
            Connection connection = connectionHashMap.get(connectionKey);

            // If we do not have a connection open.
            if (connection == null) try {
                connection = createConnection(incomingPacket.getAddress(), incomingPacket.getPort());
            } catch (SocketException e) {
                break;
            }

            // Pass the packet to the connections worker thread.
            try {
                connection.putIncomingBlocking(incomingPacket);
            } catch (InterruptedException e) {
                break;
            }

        }

        // In case of not closing gracefully.
        // Sending an interrupt to each worker thread then waiting for them to join.
        for (Connection connection : connectionHashMap.values()) {
            connection.getWorkerThread().interrupt();
            try {
                connection.getWorkerThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * Creates a new connection object, adds it to the hashMap keeping track of them,
     * and then returns the connection object.
     *
     * @param address       Address of server/client to open the connection with.
     * @param port          Port of server/client to open the connection with.
     * @return              Connection object.
     * @throws SocketException
     */
    Connection createConnection(InetAddress address, int port) throws SocketException {
        Connection newConnection = new Connection(address, port);
        WorkerThread worker = new WorkerThread(newConnection, listenSocket, receivedObjectQueue, queueSemaphore);
        Thread workerThread = new Thread(worker);
        newConnection.setWorkerThread(workerThread);

        connectionHashMap.put(address.toString() + ":" + port, newConnection);

        workerThread.start();
        return newConnection;
    }

    /**
     * Closes the worker threads and then closes the socket, causing an exception and closing this thread.
     *
     * @throws InterruptedException
     */
    void disconnect() throws InterruptedException {
        for (Connection connection : connectionHashMap.values()) {
            connection.getWorkerThread().interrupt();
            connection.getWorkerThread().join();
        }
        listenSocket.close();
    }


}
