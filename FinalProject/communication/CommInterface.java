/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *  @Author David Bews
 *
 *	communication.CommInterface.java
 *
 *
 *  Public functions used for sending and receiving objects.
 *
 *  Received objects from the network are placed in a queue for the class instancing Comm to retrieve.  It is
 *  that classes responsibility to determine what the object is.  (Use instanceof).
 *
 */


package FinalProject.communication;


import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

interface CommInterface {


    /**
     * Initializes a connection to the parent server of this particular server/client.  Should only be called once.
     *
     * @param parentServer      The IP Address of the parent server.
     * @param port              The port of the parent server.
     * @throws IOException
     * @throws InterruptedException
     */
    public int connectToParent(InetAddress parentServer, int port) throws IOException, InterruptedException;


    /**
     * Closes all threads and connections gracefully in preparation for shutting down the application.
     *
     * @throws InterruptedException
     */
    public void shutdown() throws InterruptedException;


    /**
     * Sends the object to each client connected to this server.
     *
     * @param obj               Object to send to each client.
     * @throws IOException
     * @throws InterruptedException
     */
    public int sendMessageClient(Object obj) throws IOException, InterruptedException;


    /**
     * Sends the object to the connected parent server.
     *
     * @param obj               Object to send to the parent.
     * @throws IOException
     * @throws InterruptedException
     */
    public int sendMessageParent(Object obj) throws IOException, InterruptedException;


    /**
     * Sends the object to every connection present.
     *
     * @param obj               Object to broadcast to every connection.
     * @throws IOException
     * @throws InterruptedException
     */
    public int sendMessageBroadcast(Object obj) throws IOException, InterruptedException;


    /**
     * Replies to the last message taking off the queue.
     *
     * @param obj               Object to reply with.
     * @throws IOException
     * @throws InterruptedException
     */
    public int sendMessageReply(Object obj) throws IOException, InterruptedException;


    /**
     * Retrieves the first object available in the queue, null if no objects are available.
     * Received objects from the network are placed in a queue for the class instancing Comm to retrieve.  It is
     * that classes responsibility to determine what the object is.  (Use instanceof).
     *
     * @return      The first object in the queue.
     */
    public Object getMessageNonBlocking();


    /**
     * Waits to retrieve the first object in the queue, blocks operation.
     * Received objects from the network are placed in a queue for the class instancing Comm to retrieve.  It is
     * that classes responsibility to determine what the object is.  (Use instanceof).
     *
     * @return      The first object in the queue.
     * @throws InterruptedException
     */
    public Object getMessageBlocking() throws InterruptedException;


    /**
     * Waits for a specified duration for an object to appear in the queue, blocks operation until then.
     * Received objects from the network are placed in a queue for the class instancing Comm to retrieve.  It is
     * that classes responsibility to determine what the object is.  (Use instanceof).
     *
     * @param timeDuration      The time to spend waiting on an object.
     * @param timeUnit          The unit of time for the aforementioned.
     * @return                  The first object in the queue.
     * @throws InterruptedException
     */
    public Object getMessageBlocking(long timeDuration, TimeUnit timeUnit) throws InterruptedException;
}

