/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	WorkerThread.java
 *
 */

package FinalProject.communication;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

class WorkerThread implements Runnable{
    long threadId;
    Connection connection;
    boolean waitingOnReply;
    DatagramPacket lastPacketSent;
    DatagramPacket lastPacketReceived;
    DatagramSocket socket;

    BlockingQueue<Object> receivedObjectQueue;

    public WorkerThread(Connection connection, int socketPort, BlockingQueue<Object> receivedObjectQueue) throws SocketException {
        threadId = Thread.currentThread().getId();
        this.connection = connection;
        socket = new DatagramSocket(socketPort);
        this.receivedObjectQueue = receivedObjectQueue;
    }




    @Override
    public void run() {

        DatagramPacket incomingPacket;
        DatagramPacket outgoingPacket;
        boolean cleanDisconnect = false;
        Object obj = null;

        while (true) {
            incomingPacket = connection.incomingPacketQueue.poll();
            if (incomingPacket == null) {
                if (timeoutOnLastPacket()) {
                    try {
                        resendLastPacket();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } else {

                try {
                    obj = Packets.decodePacket(incomingPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (obj instanceof Ack) {
                    //
                } else if (obj instanceof Connection) {

                } else if (obj instanceof String) {
                    cleanDisconnect = true;
                    break;
                } else {
                    try {
                        receivedObjectQueue.put(obj);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            outgoingPacket = connection.outgoingPacketQueue.poll();
            if (outgoingPacket != null) {
                try {
                    socket.send(outgoingPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outgoingPacket == null && incomingPacket == null) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    break;
                }
            }

            if (Thread.interrupted()) {
                break;
            }

        }

        if (!cleanDisconnect) {
            try {
                socket.send(Packets.craftPacket("INSERT DISCONNECT OBJECT", connection.getAddress(), connection.getPort()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }

    private boolean timeoutOnLastPacket() {
        return false;
    }

    private void resendLastPacket() throws IOException {
        socket.send(lastPacketSent);
    }
}
