/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *  @Author David Bews
 *
 *	communication.Comm.java
 *
 * Implementation of CommInterface.
 *
 * Responsible for all network traffic within the Electronic Voting System.  Simulates TCP/IP through UDP (Uses
 * ACKs, Checksums and requests retransmission of corrupted packets).  Each server/client implements an instance
 * of this class.
 *
 */


package FinalProject.communication;


import FinalProject.communication.communicationobjects.Connect;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Comm implements CommInterface {

    private ListenThread listener;
    private Thread listenThread;
    private Connection parentConnection = null;
    private Connection replyConnection = null;
    private Semaphore maximumConnections;

    private BlockingQueue<CommTuple> receivedObjectQueue = new LinkedBlockingQueue<>();

    /**
     * Constructor for the Comm objects, initializes the listener threads.
     *
     * @param port Port to listen on for packets.
     * @throws SocketException
     */
    public Comm(int port) throws SocketException {
        maximumConnections = new Semaphore(20);
        listener = new ListenThread(port, receivedObjectQueue, maximumConnections);
        listenThread = new Thread(listener);
        listenThread.start();
    }


    /**
     * Constructor for the Comm objects, initializes the listener threads.
     *
     * @param port Port to listen on for packets.
     * @throws SocketException
     */
    public Comm(int port, int maxConnections) throws SocketException {
        maximumConnections = new Semaphore(maxConnections);
        listener = new ListenThread(port, receivedObjectQueue, maximumConnections);
        listenThread = new Thread(listener);
        listenThread.start();
    }


    /**
     * Initializes a connection to the parent server of this particular server/client.  Should only be called once.
     *
     * @param parentServer The IP Address of the parent server.
     * @param port         The port of the parent server.
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public int connectToParent(InetAddress parentServer, int port) throws IOException, InterruptedException {
        parentConnection = listener.createConnection(parentServer, port);
        DatagramPacket outgoingPacket = Packets.craftPacket(new Connect(), parentConnection.getAddress(), parentConnection.getPort());
        parentConnection.putOutgoingBlocking(outgoingPacket);

        synchronized (parentConnection.waitAckSync) {
            if (!parentConnection.isAckResultReady()) parentConnection.waitAckSync.wait();
            int result = parentConnection.getAckResult();
            if (result != 0) {
                parentConnection = null;
                return result;
            }
            parentConnection.setAckResultReady(false);
        }
        return 0;
    }


    /**
     * Closes all threads and connections gracefully in preparation for shutting down the application.
     *
     * @throws InterruptedException
     */
    @Override
    public void shutdown() throws InterruptedException {
        listener.disconnect();
        listenThread.join();
    }


    /**
     * Sends the object to each client connected to this server.
     *
     * @param obj Object to send to each client.
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public int sendMessageClient(Object obj) throws IOException, InterruptedException {
        DatagramPacket outgoingPacket;

        if (listener.connectionHashMap.size() == 0) return CommError.ERROR_NO_CLIENTS;

        for (Connection connection : listener.connectionHashMap.values()) {
            if (!connection.equals(parentConnection)) {
                outgoingPacket = Packets.craftPacket(obj, connection.getAddress(), connection.getPort());
                connection.putOutgoingBlocking(outgoingPacket);
            }
        }

        for (Connection connection : listener.connectionHashMap.values()) {
            if (!connection.equals(parentConnection)) {
                synchronized (connection.waitAckSync) {
                    if (!connection.isAckResultReady()) connection.waitAckSync.wait();
                    int result = connection.getAckResult();
                    if (result != 0) return result;
                    connection.setAckResultReady(false);
                }
            }
            if (connection.equals(parentConnection) && listener.connectionHashMap.size() == 1){
                return CommError.ERROR_NO_CLIENTS;
            }
        }
        return 0;
    }


    /**
     * Sends the object to the connected parent server.
     *
     * @param obj Object to send to the parent.
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public int sendMessageParent(Object obj) throws IOException, InterruptedException {
        if (parentConnection != null) {
            DatagramPacket outgoingPacket = Packets.craftPacket(obj, parentConnection.getAddress(), parentConnection.getPort());
            parentConnection.putOutgoingBlocking(outgoingPacket);

            synchronized (parentConnection.waitAckSync) {
                if (!parentConnection.isAckResultReady()) parentConnection.waitAckSync.wait();
                int result = parentConnection.getAckResult();
                if (result != 0) {
                    return result;
                }
                parentConnection.setAckResultReady(false);
            }
            return 0;
        }
        return CommError.ERROR_NO_PARENT;
    }


    /**
     * Sends the object to every connection present.
     *
     * @param obj Object to broadcast to every connection.
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public int sendMessageBroadcast(Object obj) throws IOException, InterruptedException {
        int status;
        status = sendMessageParent(obj);
        if (status == 0) status = sendMessageClient(obj);
        return status;
    }


    /**
     * Replies to the last message taking off the queue.
     *
     * @param obj Object to reply with.
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public int sendMessageReply(Object obj) throws IOException, InterruptedException {
        DatagramPacket outgoingPacket = Packets.craftPacket(obj, replyConnection.getAddress(), replyConnection.getPort());
        replyConnection.putOutgoingBlocking(outgoingPacket);

        synchronized (replyConnection.waitAckSync) {
            if (!replyConnection.isAckResultReady()) replyConnection.waitAckSync.wait();
            int result = replyConnection.getAckResult();
            if (result != 0) {
                return result;
            }
            replyConnection.setAckResultReady(false);
        }
        return 0;
    }


    /**
     * Retrieves the first object available in the queue, null if no objects are available.
     * Received objects from the network are placed in a queue for the class instancing Comm to retrieve.  It is
     * that classes responsibility to determine what the object is.  (Use instanceof).
     *
     * @return The first object in the queue.
     */
    @Override
    public Object getMessageNonBlocking() {
        CommTuple receivedCommTuple = receivedObjectQueue.poll();
        if (replyConnection == null) return null;
        replyConnection = receivedCommTuple.getConnection();
        return receivedCommTuple.getObj();
    }


    /**
     * Waits to retrieve the first object in the queue, blocks operation.
     * Received objects from the network are placed in a queue for the class instancing Comm to retrieve.  It is
     * that classes responsibility to determine what the object is.  (Use instanceof).
     *
     * @return The first object in the queue.
     * @throws InterruptedException
     */
    @Override
    public Object getMessageBlocking() throws InterruptedException {
        CommTuple receivedCommTuple = receivedObjectQueue.take();
        replyConnection = receivedCommTuple.getConnection();
        return receivedCommTuple.getObj();
    }


    /**
     * Waits for a specified duration for an object to appear in the queue, blocks operation until then.
     * Received objects from the network are placed in a queue for the class instancing Comm to retrieve.  It is
     * that classes responsibility to determine what the object is.  (Use instanceof).
     *
     * @param timeDuration The time to spend waiting on an object.
     * @param timeUnit     The unit of time for the aforementioned.
     * @return The first object in the queue.
     * @throws InterruptedException
     */
    @Override
    public Object getMessageBlocking(long timeDuration, TimeUnit timeUnit) throws InterruptedException {
        CommTuple receivedCommTuple = receivedObjectQueue.poll(timeDuration, timeUnit);
        if (replyConnection == null) return null;
        replyConnection = receivedCommTuple.getConnection();
        return receivedCommTuple.getObj();
    }
}
