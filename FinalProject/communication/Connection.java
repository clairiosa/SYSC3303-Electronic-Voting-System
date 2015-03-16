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
import java.util.concurrent.TimeUnit;

class Connection implements Serializable{
    private int port;
    private InetAddress address;
    private Thread workerThread;

    private BlockingQueue<DatagramPacket> incomingPacketQueue;
    BlockingQueue<DatagramPacket> outgoingPacketQueue;


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
    public int getPort() {
        return port;
    }


    /**
     *
     * @return  The ip address of the destination
     */
    public InetAddress getAddress() {
        return address;
    }


    /**
     *
     * @return  The thread object currently working on this connection.
     */
    public Thread getWorkerThread() {
        return workerThread;
    }


    /**
     *
     * @param workerThread  The thread object intended to work on this connection
     */
    public void setWorkerThread(Thread workerThread) {
        this.workerThread = workerThread;
    }


    /**
     *
     * @return              The first object in the queue.
     * @throws InterruptedException
     */
    public DatagramPacket getIncomingBlocking() throws InterruptedException {
        return incomingPacketQueue.take();
    }


    /**
     *
     * @param timeDuration      The time to spend waiting on an object.
     * @param timeUnit          The unit of time for the aforementioned.
     * @return                  The first object in the queue.
     * @throws InterruptedException
     */
    public DatagramPacket getIncomingBlocking(long timeDuration, TimeUnit timeUnit) throws InterruptedException {
        return incomingPacketQueue.poll(timeDuration, timeUnit);
    }


    /**
     *
     * @return                  The first object in the queue.
     * @throws InterruptedException
     */
    public DatagramPacket getIncomingNonBlocking() throws InterruptedException {
        return incomingPacketQueue.poll();
    }


    /**
     *
     * @param packet            Packet to place at the tail of the queue.
     * @throws InterruptedException
     */
    public void putIncomingBlocking(DatagramPacket packet) throws InterruptedException {
        incomingPacketQueue.put(packet);
    }
}
