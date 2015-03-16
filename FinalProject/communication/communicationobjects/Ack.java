/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Ack.java
 *
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
