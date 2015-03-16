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

    public WorkerThread(Connection connection, DatagramSocket socket, BlockingQueue<Object> receivedObjectQueue, Semaphore queueSemaphore) throws SocketException {
        threadId = Thread.currentThread().getId();
        this.connection = connection;

        this.socket = socket;
        this.receivedObjectQueue = receivedObjectQueue;

        this.queueSemaphore = queueSemaphore;
    }




    @Override
    public void run() {
        System.out.println(connection.getPort() + " Worker Thread Created");

        DatagramPacket incomingPacket;
        DatagramPacket outgoingPacket;
        boolean cleanDisconnect = false;
        Object obj;
        long lastPacketReceivedChecksum = 0;

        long lastSentPacketTime = 0;
        waitingOnReply = false;

        lastPacketSentAck = false;

        while (true) {

            // Receiving.
            incomingPacket = connection.incomingPacketQueue.poll();
            try {
                if (incomingPacket == null) {
                    if (timeoutOnLastPacket(lastSentPacketTime) && waitingOnReply) {
                        System.out.println("Timeout "+ connection.getPort());
                        lastSentPacketTime = sendPacket(lastPacketSent, true);
                        lastPacketSentAck = false;
                    }

                } else {
                    System.out.println("Got Packet");
                    if (Packets.validateChecksum(incomingPacket.getData(), incomingPacket.getLength())) {
                        if (lastPacketReceivedChecksum == Packets.calculateChecksum(incomingPacket.getData(), incomingPacket.getLength())) { //Duplicate
                            sendAck(false);
                            System.out.println("Sent ACK");
                        } else { // Not duplicate.
                            obj = Packets.decodePacket(incomingPacket);
                            System.out.println("Decoded Packet");
                            if (obj instanceof Ack) {
                                System.out.println("Ack " + connection.getPort());
                                waitingOnReply = false;
                                System.out.println(connection.getPort() + " Waiting set to " + waitingOnReply);
                                Ack receivedAck = (Ack) obj;
                                if (receivedAck.getStatus()) {
                                    System.out.println("Corrupted");
                                    lastSentPacketTime = sendPacket(lastPacketSent, !lastPacketSentAck);
                                }
                            } else if (obj instanceof Connect) {
                                System.out.println("Connected!");
                                Connect connect = (Connect) obj;
                                sendAck(false);
                                System.out.println("Sent Ack" + connection.getPort());
                            } else if (obj instanceof Disconnect) {
                                System.out.println("Disconnect");
                                cleanDisconnect = true;
                                break;
                            } else {
                                System.out.println("in queue " + connection.getPort() + " " + obj);
                                queueSemaphore.acquire();
                                receivedObjectQueue.put(obj);
                                queueSemaphore.release();
                                sendAck(false);
                                System.out.println("Sent Ack" + connection.getPort());
                            }

                            lastPacketReceived = incomingPacket;
                            lastPacketReceivedChecksum = Packets.calculateChecksum(lastPacketReceived.getData(), lastPacketReceived.getLength());
                        }
                    } else {
                        System.out.println("checksum failed.");
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
                    System.out.println("Sending Packet");
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

    private boolean timeoutOnLastPacket(long lastSentPacketTime) {
        return lastSentPacketTime > 0 && (System.currentTimeMillis() - lastSentPacketTime) > 2000;
    }


    private long sendPacket(DatagramPacket packet, boolean waiting) throws IOException {
        socket.send(packet);
        waitingOnReply = waiting;
        System.out.println(connection.getPort() + " Waiting set to " + waiting);
        return System.currentTimeMillis();
    }

    private void sendAck(boolean corrupted) throws IOException {
        DatagramPacket ackPacket = Packets.craftPacket(new Ack(corrupted), connection.getAddress(), connection.getPort());
        lastPacketSent = ackPacket;
        sendPacket(ackPacket, false);
        lastPacketSentAck = true;
    }
}
