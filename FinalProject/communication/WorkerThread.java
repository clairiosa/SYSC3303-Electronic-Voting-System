/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	WorkerThread.java
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

class WorkerThread implements Runnable{
    long threadId;
    Connection connection;
    boolean waitingOnReply;
    boolean lastPacketSentAck;
    DatagramPacket lastPacketSent;
    DatagramPacket lastPacketReceived;
    DatagramSocket socket;
    Semaphore queueSemaphore;
    int maxTimeouts;

    BlockingQueue<CommTuple> receivedObjectQueue;

    /**
     * Constructor for the worker thread.
     *
     * @param connection            Connection object associated with the worker thread.
     * @param socket                Socket to send messages out on.
     * @param receivedObjectQueue   Queue to put received messages on.
     * @param queueSemaphore        Semaphore governing access to the queue.
     * @throws SocketException
     */
    public WorkerThread(Connection connection, DatagramSocket socket, BlockingQueue<CommTuple> receivedObjectQueue, Semaphore queueSemaphore) throws SocketException {
        threadId = Thread.currentThread().getId();
        this.connection = connection;

        this.socket = socket;
        this.receivedObjectQueue = receivedObjectQueue;

        this.queueSemaphore = queueSemaphore;

        maxTimeouts = 5;

    }




    @Override
    public void run() {

        DatagramPacket outgoingPacket;
        boolean cleanDisconnect = false;
        Object obj;
        long lastPacketReceivedChecksum = 0;

        long lastSentPacketTime = 0;
        waitingOnReply = false;

        lastPacketSentAck = false;


        int currentTimeouts = 0;

        while (true) {

            // Receiving.
            DatagramPacket incomingPacket;
            try {
                incomingPacket = connection.getIncomingNonBlocking();
                if (incomingPacket == null) {
                    if (timeoutOnLastPacket(lastSentPacketTime) && waitingOnReply) {
                        currentTimeouts++;
                        if (currentTimeouts == maxTimeouts) {
                            lastSentPacketTime = 0;
                            synchronized(connection.waitAckSync) {
                                connection.setAckResultReady(true);
                                connection.setAckResult(false);
                                connection.waitAckSync.notify();
                            }
                        } else {
                            lastSentPacketTime = sendPacket(lastPacketSent, true);
                            lastPacketSentAck = false;
                        }
                    }

                } else {
                    currentTimeouts = 0;
                    if (Packets.validateChecksum(incomingPacket.getData(), incomingPacket.getLength())) {
                        obj = Packets.decodePacket(incomingPacket);
                        if ((lastPacketReceivedChecksum == Packets.calculateChecksum(incomingPacket.getData(), incomingPacket.getLength())) && !(obj instanceof Ack)) { //Duplicate
                            sendAck(false);
                        } else { // Not duplicate.
                            if (obj instanceof Ack) {
                                if (waitingOnReply) {
                                    synchronized(connection.waitAckSync) {
                                        connection.setAckResultReady(true);
                                        connection.setAckResult(true);
                                        connection.waitAckSync.notify();
                                    }
                                    waitingOnReply = false;
                                    Ack receivedAck = (Ack) obj;
                                    if (receivedAck.isCorrupted()) {
                                        lastSentPacketTime = sendPacket(lastPacketSent, !lastPacketSentAck);
                                    } else if (receivedAck.isParentAck()) {
                                        receivedObjectQueue.put(new CommTuple(obj, connection));
                                    }
                                }
                            } else if (obj instanceof Connect) {
                                sendAck(false);
                            } else if (obj instanceof Disconnect) {
                                cleanDisconnect = true;
                                break;
                            } else {
                                queueSemaphore.acquire();
                                receivedObjectQueue.put(new CommTuple(obj, connection));
                                queueSemaphore.release();
                                sendAck(false);
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


            // Sending
            try {
                outgoingPacket = connection.getOutgoingNonBlocking();
                if (outgoingPacket != null) {
                    lastSentPacketTime = sendPacket(outgoingPacket, true);
                    lastPacketSentAck = false;
                    lastPacketSent = outgoingPacket;
                }

                if (outgoingPacket == null && incomingPacket == null) {
                    Thread.sleep(5);
                }

                if (Thread.interrupted()) {
                    break;
                }

            } catch (IOException e) {
                //
                break;
            } catch (InterruptedException e) {
                //
                break;
            }

        }


        if (!cleanDisconnect) {
            try {
                socket.send(Packets.craftPacket(new Disconnect(), connection.getAddress(), connection.getPort()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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


    /**
     * Sends a parent connection aack.
     *
     * @param corrupted     True if the packet was corrupted and the ack is requesting a retransmission
     * @throws IOException
     */
    private void sendAck(boolean corrupted, boolean parentAck) throws IOException {
        Ack ack = new Ack(corrupted);
        ack.setParentAck(parentAck);
        DatagramPacket ackPacket = Packets.craftPacket(ack , connection.getAddress(), connection.getPort());
        lastPacketSent = ackPacket;
        sendPacket(ackPacket, false);
        lastPacketSentAck = true;
    }
}
