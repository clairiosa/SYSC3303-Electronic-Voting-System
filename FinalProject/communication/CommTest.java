/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Ack.java
 *
 */


package FinalProject.communication;

import java.io.IOException;
import java.net.InetAddress;

public class CommTest {

    public static void main(String args[]) throws IOException, InterruptedException {
        Comm comm1 = new Comm(2000);
        Comm comm2 = new Comm(2001);
        Comm comm3 = new Comm(2002);
        Comm comm4 = new Comm(2003);

        comm2.connectToParent(InetAddress.getByName("127.0.0.1"), 2000);
        comm3.connectToParent(InetAddress.getByName("127.0.0.1"), 2000);
        comm4.connectToParent(InetAddress.getByName("127.0.0.1"), 2001);
        Thread.sleep(4000);

        System.out.println("~~~~~~~~~~~~~~~~~~~Message to Clients");
        comm1.sendMessageClient("~~~~~Message to Clients");
        System.out.println((String) comm2.getMessageBlocking());
        System.out.println((String) comm3.getMessageBlocking());
        Thread.sleep(4000);
        System.out.println("~~~~~~~~~~~~~~~~~~~Message to Parents");

        comm2.sendMessageParent("~~~~~Message to Parent1 from 2");
        comm3.sendMessageParent("~~~~~Message to Parent1 from 3");
        System.out.println((String) comm1.getMessageBlocking());
        Thread.sleep(10);
        System.out.println((String) comm1.getMessageBlocking());
        comm4.sendMessageParent("~~~~~Message to Parent2 from 4");
        System.out.println((String) comm2.getMessageBlocking());
        Thread.sleep(6000);
        System.out.println("~~~~~~~~~~~~~~~~~~~Message Broadcast");

        comm2.broadcastMessage("~~~~~Message to 1 & 4 from 2");

        System.out.println((String) comm1.getMessageBlocking());
        System.out.println((String) comm4.getMessageBlocking());

        /*
        comm2.sendTestMessage("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Testing Message");
        System.out.println((String) comm1.getMessageBlocking());
        System.out.println((String) comm4.getMessageBlocking());

        comm1.sendTestMessage("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Testing Message");
        System.out.println((String) comm2.getMessageBlocking());
        System.out.println((String) comm3.getMessageBlocking());
        */

        Thread.sleep(6000);

        comm1.shutdown();
        comm2.shutdown();
        comm3.shutdown();
        comm4.shutdown();

    }
}
