/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Ack.java
 *
 */


package FinalProject.communication.communicationobjects;


import java.io.Serializable;

public class Connect implements Serializable{
    private int port;
    public Connect(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }
}
