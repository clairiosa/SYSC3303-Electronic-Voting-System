/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Ack.java
 *
 */


package FinalProject.communication;


class Ack {
    int status;

    public Ack(int status) {
        this.status = status;
    }

    public int getStatus() {
       return status;
    }
}
