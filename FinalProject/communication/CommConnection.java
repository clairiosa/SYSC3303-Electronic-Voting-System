/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	CommConnection.java
 *
 */

package FinalProject.communication;


import java.io.Serializable;
import java.net.InetAddress;

public class CommConnection implements Serializable{
    int port;
    InetAddress address;

    CommConnection(int port, InetAddress address){
        this.port = port;
        this.address = address;
    }
}
