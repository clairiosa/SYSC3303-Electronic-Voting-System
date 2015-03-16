/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Comm.java
 *
 * Implementation of CommInterface.
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

    private Semaphore queueSemaphore;

    private BlockingQueue<Object> receivedObjectQueue = new LinkedBlockingQueue<Object>();


    /**
     * Sole constructor for the Comm objects, initializes the listener threads.
     *
     * @param port          Port to listen on for packets.
     * @throws SocketException
     */
    public Comm(int port) throws SocketException {
        queueSemaphore = new Semaphore(1);
        listener = new ListenThread(port, receivedObjectQueue, queueSemaphore);
        listenThread = new Thread(listener);
        listenThread.start();
    }


    /**
     * Initializes a connection to the parent server of this particular server/client.  Should only be called once.
     *
     * @param parentServer      The IP Address of the parent server.
     * @param port              The port of the parent server.
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void connectToParent(InetAddress parentServer, int port) throws IOException, InterruptedException {
        parentConnection = listener.createConnection(parentServer, port);
        DatagramPacket outgoingPacket = Packets.craftPacket(new Connect(port), parentConnection.getAddress(), parentConnection.getPort());
        parentConnection.outgoingPacketQueue.put(outgoingPacket);

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
     * @param obj               Object to send to each client.
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void sendMessageClient(Object obj) throws IOException, InterruptedException {
        DatagramPacket outgoingPacket;
        for (Connection connection : listener.connectionHashMap.values()) {
            if (!connection.equals(parentConnection)) {
                outgoingPacket = Packets.craftPacket(obj, connection.getAddress(), connection.getPort());
                connection.outgoingPacketQueue.put(outgoingPacket);
            }
        }
    }


    /**
     * Sends the object to the connected parent server.
     *
     * @param obj               Object to send to the parent.
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void sendMessageParent(Object obj) throws IOException, InterruptedException {
        if (parentConnection == null){
            //TODO-Dave Custom Exception.
        } else {
            DatagramPacket outgoingPacket = Packets.craftPacket(obj, parentConnection.getAddress(), parentConnection.getPort());
            parentConnection.outgoingPacketQueue.put(outgoingPacket);
        }
    }


    /**
     * Sends the object to every connection present.
     *
     * @param obj               Object to broadcast to every connection.
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void broadcastMessage(Object obj) throws IOException, InterruptedException {
        for (Connection connection : listener.connectionHashMap.values()) {
            DatagramPacket outgoingPacket = Packets.craftPacket(obj, connection.getAddress(), connection.getPort());
            connection.outgoingPacketQueue.put(outgoingPacket);
        }
    }


    /**
     * Retrieves the first object available in the queue, null if no objects are available.
     * Received objects from the network are placed in a queue for the class instancing Comm to retrieve.  It is
     * that classes responsibility to determine what the object is.  (Use instanceof).
     *
     * @param string            String to send to every connection.
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void sendTestMessage(String string) throws IOException, InterruptedException {
        for (Connection connection : listener.connectionHashMap.values()) {
            if (!connection.equals(parentConnection)) {
                DatagramPacket outgoingPacket = Packets.craftPacket(string, connection.getAddress(), connection.getPort());
                connection.outgoingPacketQueue.put(outgoingPacket);
            }
        }
        DatagramPacket outgoingPacket = Packets.craftPacket("Parent: " + string, parentConnection.getAddress(), parentConnection.getPort());
        parentConnection.outgoingPacketQueue.put(outgoingPacket);
    }


    /**
     * Retrieves the first object available in the queue, null if no objects are available.
     * Received objects from the network are placed in a queue for the class instancing Comm to retrieve.  It is
     * that classes responsibility to determine what the object is.  (Use instanceof).
     *
     * @return      The first object in the queue.
     */
    @Override
    public Object getMessageNonBlocking() {
        return receivedObjectQueue.poll();
    }


    /**
     * Waits to retrieve the first object in the queue, blocks operation.
     * Received objects from the network are placed in a queue for the class instancing Comm to retrieve.  It is
     * that classes responsibility to determine what the object is.  (Use instanceof).
     *
     * @return                  The first object in the queue.
     * @throws InterruptedException
     */
    @Override
    public Object getMessageBlocking() throws InterruptedException {
        return receivedObjectQueue.take();
    }


    /**
     * Waits for a specified duration for an object to appear in the queue, blocks operation until then.
     * Received objects from the network are placed in a queue for the class instancing Comm to retrieve.  It is
     * that classes responsibility to determine what the object is.  (Use instanceof).
     *
     * @param timeDuration      The time to spend waiting on an object.
     * @param timeUnit          The unit of time for the aforementioned.
     * @return                  The first object in the queue.
     * @throws InterruptedException
     */
    @Override
    public Object getMessageBlocking(long timeDuration, TimeUnit timeUnit) throws InterruptedException {
        return receivedObjectQueue.poll(timeDuration, timeUnit);
    }

}
