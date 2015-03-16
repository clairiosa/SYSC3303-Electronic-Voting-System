/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	WorkerThread.java
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

    BlockingQueue<Object> receivedObjectQueue;

    /**
     * @param connection
     * @param socket
     * @param receivedObjectQueue
     * @param queueSemaphore
     * @throws SocketException
     */
    public WorkerThread(Connection connection, DatagramSocket socket, BlockingQueue<Object> receivedObjectQueue, Semaphore queueSemaphore) throws SocketException {
        threadId = Thread.currentThread().getId();
        this.connection = connection;

        this.socket = socket;
        this.receivedObjectQueue = receivedObjectQueue;

        this.queueSemaphore = queueSemaphore;
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

        while (true) {

            // Receiving.
            DatagramPacket incomingPacket;
            try {
                incomingPacket = connection.getIncomingNonBlocking();
                if (incomingPacket == null) {
                    if (timeoutOnLastPacket(lastSentPacketTime) && waitingOnReply) {
                        lastSentPacketTime = sendPacket(lastPacketSent, true);
                        lastPacketSentAck = false;
                    }

                } else {
                    if (Packets.validateChecksum(incomingPacket.getData(), incomingPacket.getLength())) {
                        if (lastPacketReceivedChecksum == Packets.calculateChecksum(incomingPacket.getData(), incomingPacket.getLength())) { //Duplicate
                            sendAck(false);
                        } else { // Not duplicate.
                            obj = Packets.decodePacket(incomingPacket);
                            if (obj instanceof Ack) {
                                waitingOnReply = false;
                                Ack receivedAck = (Ack) obj;
                                if (receivedAck.getStatus()) {
                                    lastSentPacketTime = sendPacket(lastPacketSent, !lastPacketSentAck);
                                }
                            } else if (obj instanceof Connect) {
                                Connect connect = (Connect) obj;
                                sendAck(false);
                            } else if (obj instanceof Disconnect) {
                                cleanDisconnect = true;
                                break;
                            } else {
                                queueSemaphore.acquire();
                                receivedObjectQueue.put(obj);
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
            outgoingPacket = connection.outgoingPacketQueue.poll();

            try {
                if (outgoingPacket != null) {
                    lastSentPacketTime = sendPacket(outgoingPacket, true);
                    lastPacketSentAck = false;
                    lastPacketSent = outgoingPacket;
                }

                if (outgoingPacket == null && incomingPacket == null) {
                    Thread.sleep(10);
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
     * @param lastSentPacketTime
     * @return
     */
    private boolean timeoutOnLastPacket(long lastSentPacketTime) {
        return lastSentPacketTime > 0 && (System.currentTimeMillis() - lastSentPacketTime) > 2000;
    }


    /**
     * @param packet
     * @param waiting
     * @return
     * @throws IOException
     */
    private long sendPacket(DatagramPacket packet, boolean waiting) throws IOException {
        socket.send(packet);
        waitingOnReply = waiting;
        return System.currentTimeMillis();
    }


    /**
     * @param corrupted
     * @throws IOException
     */
    private void sendAck(boolean corrupted) throws IOException {
        DatagramPacket ackPacket = Packets.craftPacket(new Ack(corrupted), connection.getAddress(), connection.getPort());
        lastPacketSent = ackPacket;
        sendPacket(ackPacket, false);
        lastPacketSentAck = true;
    }
}
