/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Ack.java
 *
 * Intended to be sent by Comm so implements Serializable.
 * Serializable makes it so the object can be turned into an array of bytes by Packets.java's functions.
 */


package FinalProject.communication.communicationobjects;


import java.io.Serializable;

public class Ack implements Serializable {
    boolean status;

    public Ack(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }
}
