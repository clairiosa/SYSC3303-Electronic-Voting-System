/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *  @Author David Bews
 *
 *	communication.CommTuple.java
 *
 * Holds an object and it's associated connection object.  Used to facilitate the sendMessageReply command.
 *
 */


package FinalProject.communication;


class CommTuple {
    private Object obj;
    private Connection connection;

    CommTuple(Object obj, Connection connection){
        this.obj = obj;
        this.connection = connection;
    }

    public Object getObj() {
        return obj;
    }

    public Connection getConnection() {
        return connection;
    }
}
