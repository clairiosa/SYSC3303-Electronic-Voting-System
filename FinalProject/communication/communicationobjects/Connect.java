/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Connect.java
 *
 * Sent when initializing a new connection.  In this implementation it has no contents.
 *
 * Intended to be sent by Comm so implements Serializable.
 * Serializable makes it so the object can be turned into an array of bytes by Packets.java's functions.
 */


package FinalProject.communication.communicationobjects;


import java.io.Serializable;

public class Connect implements Serializable{
    public Connect() {
    }
}
