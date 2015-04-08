/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *  @Author David Bews
 *
 *	communication.WorkerThread.java
 *
 *  Does all the 'work' for each connection.  Decodes packets, sends them to the received queue, and encodes packets
 *  and sends them to the connected server.
 *
 */

package FinalProject.communication;

import FinalProject.communication.communicationobjects.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

class CommWorker implements Runnable{
    //Constants
    long threadId;
    Connection connection;
    boolean waitingOnReply;
    boolean lastPacketSentAck;
    DatagramPacket lastPacketSent;
    DatagramPacket lastPacketReceived;
    DatagramSocket socket;
    int maxTimeouts;
    private Semaphore maximumConnections;
    long lastSentPacketTime;

    BlockingQueue<CommTuple> receivedObjectQueue;

    /**
     * Constructor for the worker thread.
     *
     * @param connection            Connection object associated with the worker thread.
     * @param socket                Socket to send messages out on.
     * @param receivedObjectQueue   Queue to put received messages on.
     * @param maximumConnections    The maximum number of child connections possible.
     * @throws SocketException
     */
    public CommWorker(Connection connection, DatagramSocket socket, BlockingQueue<CommTuple> receivedObjectQueue, Semaphore maximumConnections) throws SocketException {
        threadId = Thread.currentThread().getId();
        this.connection = connection;

        this.socket = socket;
        this.receivedObjectQueue = receivedObjectQueue;

        this.maximumConnections = maximumConnections;

        maxTimeouts = 5;

    }


    /**
     * Main loop for the worker thread.  Two main functions, process the received packets for this connection and
     * send the packets intended for the other half of the connection.
     */
    @Override
    public void run() {

        DatagramPacket outgoingPacket;
        Object obj;
        long lastPacketReceivedChecksum = 0;
        boolean cleanDisconnect = false;
        waitingOnReply = false;
        lastPacketSentAck = false;
        lastSentPacketTime = 0;
        int currentTimeouts = 0;

        while (true) {

            //                          //
            // Handle received packets. //
            //                          //
            DatagramPacket incomingPacket;
            try {
                incomingPacket = connection.getIncomingNonBlocking();
                if (incomingPacket == null) {

                    //Timeout handling
                    if (timeoutOnLastPacket(lastSentPacketTime) && waitingOnReply) {
                        currentTimeouts++;
                        if (currentTimeouts == maxTimeouts) {
                            lastSentPacketTime = 0;
                            synchronized(connection.waitAckSync) {
                                connection.setAckResultReady(true);
                                connection.setAckResult(CommError.ERROR_TIMEOUT);
                                connection.waitAckSync.notify();
                            }
                        } else {
                            lastSentPacketTime = sendPacket(lastPacketSent, true);
                            lastPacketSentAck = false;
                        }
                    }

                } else {
                    currentTimeouts = 0;

                    // Checksum validating
                    if (Packets.validateChecksum(incomingPacket.getData(), incomingPacket.getLength())) {
                        obj = Packets.decodePacket(incomingPacket);

                        // Duplicate packet checking.
                        if ((lastPacketReceivedChecksum == Packets.calculateChecksum(incomingPacket.getData(), incomingPacket.getLength())) && !(obj instanceof Ack)) { //Duplicate
                            sendAck(false);
                        } else { // Not duplicate.

                            if (!handleObject(obj)) {
                                cleanDisconnect = true;
                                break;
                            }

                            lastPacketReceived = incomingPacket;
                            lastPacketReceivedChecksum = Packets.calculateChecksum(lastPacketReceived.getData(), lastPacketReceived.getLength());
                        }
                    } else {
                        sendAck(true);
                    }
                }
            } catch (IOException e) {
                break;
            } catch (InterruptedException e) {
                break;
            } catch (ClassNotFoundException e) {
                break;
            }


            //                          //
            // Handle sending packets.  //
            //                          //
            try {
                outgoingPacket = connection.getOutgoingNonBlocking();
                if (outgoingPacket != null) {
                    lastSentPacketTime = sendPacket(outgoingPacket, true);
                    lastPacketSentAck = false;
                    lastPacketSent = outgoingPacket;
                }

                // To prevent using all available CPU cycles.
                if (outgoingPacket == null && incomingPacket == null) {
                    Thread.sleep(5);
                }

                if (Thread.interrupted()) {
                    break;
                }

            } catch (IOException e) {
                break;
            } catch (InterruptedException e) {
                break;
            }
        }

        // Exiting.
        maximumConnections.release();
        if (!cleanDisconnect) {
            try {
                socket.send(Packets.craftPacket(new Disconnect(), connection.getAddress(), connection.getPort()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        synchronized(connection.waitAckSync) {
            connection.setAckResultReady(true);
            connection.setAckResult(CommError.ERROR_CONNECTION_CLOSED);
            connection.waitAckSync.notify();
        }
    }


    /**
     * Does different actions depending on the object received.  Basically a function to hide away the ugly
     * instanceof if tree.
     *
     * @param obj           The object to inspect.
     * @return              False if disconnecting.
     * @throws IOException
     * @throws InterruptedException
     */
    private boolean handleObject(Object obj) throws IOException, InterruptedException {
        if (obj instanceof Ack) {
            if (waitingOnReply) {
                waitingOnReply = false;
                Ack receivedAck = (Ack) obj;
                if (receivedAck.isRejectConnection()) {
                    synchronized(connection.waitAckSync) {
                        connection.setAckResultReady(true);
                        connection.setAckResult(CommError.ERROR_REJECT_CONNECTION);
                        connection.waitAckSync.notify();
                    }
                } else if (receivedAck.isCorrupted()) {
                    lastSentPacketTime = sendPacket(lastPacketSent, !lastPacketSentAck);
                } else {
                    synchronized(connection.waitAckSync) {
                        connection.setAckResultReady(true);
                        connection.setAckResult(0);
                        connection.waitAckSync.notify();
                    }
                }
            }
        } else if (obj instanceof Connect) {
            sendAck(false);
        } else if (obj instanceof Disconnect) {
            return false;
        } else {
            receivedObjectQueue.put(new CommTuple(obj, connection));
            sendAck(false);
        }
        return true;
    }


    /**
     *
     * @param lastSentPacketTime    System time the last message was sent.
     * @return                      True if timeout, false if not.
     */
    private boolean timeoutOnLastPacket(long lastSentPacketTime) {
        return lastSentPacketTime > 0 && (System.currentTimeMillis() - lastSentPacketTime) > 1000;
    }


    /**
     * Sends a packet
     *
     * @param packet        Packet to send.
     * @param waiting       Set if the packet requires a timeout.
     * @return              Time the packet is sent.
     * @throws IOException
     */
    private long sendPacket(DatagramPacket packet, boolean waiting) throws IOException {
        socket.send(packet);
        waitingOnReply = waiting;
        return System.currentTimeMillis();
    }


    /**
     * Sends an ack.
     *
     * @param corrupted     True if the packet was corrupted and the ack is requesting a retransmission
     * @throws IOException
     */
    private void sendAck(boolean corrupted) throws IOException {
        DatagramPacket ackPacket = Packets.craftPacket(new Ack(corrupted), connection.getAddress(), connection.getPort());
        lastPacketSent = ackPacket;
        sendPacket(ackPacket, false);
        lastPacketSentAck = true;
    }
}
