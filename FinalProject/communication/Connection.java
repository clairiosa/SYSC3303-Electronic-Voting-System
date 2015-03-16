/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Connection.java
 *
 */

package FinalProject.communication;


import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class Connection implements Serializable{
    private int port;
    private InetAddress address;
    private Thread workerThread;

    BlockingQueue<DatagramPacket> incomingPacketQueue = new LinkedBlockingQueue<DatagramPacket>();
    BlockingQueue<DatagramPacket> outgoingPacketQueue = new LinkedBlockingQueue<DatagramPacket>();

    Connection(InetAddress address, int port){
        this.port = port;
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public Thread getWorkerThread() {
        return workerThread;
    }

    public void setWorkerThread(Thread workerThread) {
        this.workerThread = workerThread;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Connection other = (Connection) obj;
        return !(other.getPort() != port || other.getAddress() != address);
    }
}
