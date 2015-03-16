/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Connection.java
 *
 * Object that handles the open connections for the worker threads of the Comm instance.
 *
 */

package FinalProject.communication;


import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.TimeUnit; // Functions associated with this are unused.

class Connection implements Serializable{
    private int port;
    private InetAddress address;
    private Thread workerThread;

    private BlockingQueue<DatagramPacket> incomingPacketQueue;
    private BlockingQueue<DatagramPacket> outgoingPacketQueue;


    /**
     * Sole constructor for the Connection class.
     *
     * @param address   The ip address of the destination.
     * @param port      The port number of the destination.
     */
    Connection(InetAddress address, int port){
        this.port = port;
        this.address = address;

        incomingPacketQueue = new LinkedBlockingQueue<DatagramPacket>();
        outgoingPacketQueue = new LinkedBlockingQueue<DatagramPacket>();
    }


    /**
     *
     * @return  The port number of the destination.
     */
    int getPort() {
        return port;
    }


    /**
     *
     * @return  The ip address of the destination
     */
    InetAddress getAddress() {
        return address;
    }


    /**
     *
     * @return  The thread object currently working on this connection.
     */
    Thread getWorkerThread() {
        return workerThread;
    }


    /**
     *
     * @param workerThread  The thread object intended to work on this connection
     */
    void setWorkerThread(Thread workerThread) {
        this.workerThread = workerThread;
    }



    //*** incomingPacketQueue Methods ***\\\


    /**
     * UNUSED.
     * Blocks until there's an object in the incoming queue, then returns it.
     *
     * @return              The first object in the queue.
     * @throws InterruptedException
     */
    /*
    DatagramPacket getIncomingBlocking() throws InterruptedException {
        return incomingPacketQueue.take();
    }
    */


    /**
     * UNUSED.
     * Blocks for the specified duration or until there's an object in the incoming queue, then returns it.
     *
     * @param timeDuration      The time to spend waiting on an object.
     * @param timeUnit          The unit of time for the aforementioned.
     * @return                  The first object in the queue.
     * @throws InterruptedException
     */
    /*
    DatagramPacket getIncomingBlocking(long timeDuration, TimeUnit timeUnit) throws InterruptedException {
        return incomingPacketQueue.poll(timeDuration, timeUnit);
    }
    */


    /**
     *
     * @return                  The first object in the queue.
     * @throws InterruptedException
     */
    DatagramPacket getIncomingNonBlocking() throws InterruptedException {
        return incomingPacketQueue.poll();
    }


    /**
     *
     * @param packet            Packet to place at the tail of the queue.
     * @throws InterruptedException
     */
    void putIncomingBlocking(DatagramPacket packet) throws InterruptedException {
        incomingPacketQueue.put(packet);
    }



    //*** outgoingPacketQueue Methods ***\\\


    /**
     * UNUSED.
     * Blocks until there's an object in the outgoing queue, then returns it.
     *
     * @return              The first object in the queue.
     * @throws InterruptedException
     */
    /*
    DatagramPacket getOutgoingBlocking() throws InterruptedException {
        return outgoingPacketQueue.take();
    }
    */


    /**
     * UNUSED.
     * Blocks for the specified duration or until there's an object in the outgoing queue, then returns it.
     *
     * @param timeDuration      The time to spend waiting on an object.
     * @param timeUnit          The unit of time for the aforementioned.
     * @return                  The first object in the queue.
     * @throws InterruptedException
     */
    /*
    DatagramPacket getOutgoingBlocking(long timeDuration, TimeUnit timeUnit) throws InterruptedException {
        return outgoingPacketQueue.poll(timeDuration, timeUnit);
    }
    */


    /**
     *
     * @return                  The first object in the queue.
     * @throws InterruptedException
     */
    DatagramPacket getOutgoingNonBlocking() throws InterruptedException {
        return outgoingPacketQueue.poll();
    }


    /**
     *
     * @param packet            Packet to place at the tail of the queue.
     * @throws InterruptedException
     */
    void putOutgoingBlocking(DatagramPacket packet) throws InterruptedException {
        outgoingPacketQueue.put(packet);
    }
}
