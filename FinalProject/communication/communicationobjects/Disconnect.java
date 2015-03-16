/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Disconnect.java
 *
 * Sent to ensure clean disconnects.  Has no contents.
 *
 * Intended to be sent by Comm so implements Serializable.
 * Serializable makes it so the object can be turned into an array of bytes by Packets.java's functions.
 */


package FinalProject.communication.communicationobjects;


import java.io.Serializable;

public class Disconnect implements Serializable {
    //public String string; // In case you want an exit string.
    public Disconnect() {
    }
}
