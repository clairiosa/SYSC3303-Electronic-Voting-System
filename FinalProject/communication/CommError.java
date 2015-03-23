/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *  @Author David Bews
 *
 *	communication.CommError.java
 *
 *  Constants for error messages.  Because Comm can't throw custom exceptions (kills the thread) the functions return
 *  integers as error codes, which are declared here.
 *
 */

package FinalProject.communication;

final class CommError {

    private CommError(){}

    static final int ERROR_TIMEOUT = 1000;
    static final int ERROR_NO_PARENT = 1001;
    static final int ERROR_NO_CLIENTS = 1002;

}
