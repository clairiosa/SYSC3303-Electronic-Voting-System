/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Ack.java
 *
 * Used for ack packets.
 *
 * Intended to be sent by Comm so implements Serializable.
 * Serializable makes it so the object can be turned into an array of bytes by Packets.java's functions.
 */


package FinalProject.communication.communicationobjects;


import java.io.Serializable;

public class Ack implements Serializable {
    private boolean corrupted;
    private boolean parentAck;

    /**
     *
     * @param corrupted    True if this ack is being sent in response to a corrupted packet.
     */
    public Ack(boolean corrupted) {
        this.corrupted = corrupted;
    }

    public boolean isCorrupted() {
        return corrupted;
    }

    public boolean isParentAck() {
        return parentAck;
    }

    public void setParentAck(boolean parentAck) {
        this.parentAck = parentAck;
    }
}
