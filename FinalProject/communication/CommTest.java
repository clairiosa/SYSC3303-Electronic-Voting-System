/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	CommTest.java
 *
 * Example class showing how to use Comm.java
 *
 */


package FinalProject.communication;

import FinalProject.communication.CommError;
import java.io.IOException;
import java.net.InetAddress;

public class CommTest {

    public static void main(String args[]) throws IOException, InterruptedException {
        // Setting up multiple Comm instances for each simulated server.
        // In reality each server only has one.

        /* Layout of the test.
         *
         *                    ___________masterServer_____________
         *                   |                                    |
         *           districtServer1                      districtServer2
         *        ___________|_________                    _______|_______
         *       |                     |                  |               |
         * clientServer1         clientServer2      clientServer3   clientServer4
         *
         */
        System.out.println("Starting the Servers");
        Comm masterServer = new Comm(2000);
        Comm districtServer1 = new Comm(2011);
        Comm clientServer1 = new Comm(2101);
        Comm clientServer2 = new Comm(2102);
        Comm districtServer2 = new Comm(2012);
        Comm clientServer3 = new Comm(2103);
        Comm clientServer4 = new Comm(2104);

        // Always connect to the parent before having children connect
        System.out.println("Connection to Parents");
        districtServer1.connectToParent(InetAddress.getByName("127.0.0.1"), 2000);
        clientServer1.connectToParent(InetAddress.getByName("127.0.0.1"), 2011);
        clientServer2.connectToParent(InetAddress.getByName("127.0.0.1"), 2011);

        districtServer2.connectToParent(InetAddress.getByName("127.0.0.1"), 2000);
        clientServer3.connectToParent(InetAddress.getByName("127.0.0.1"), 2012);
        clientServer4.connectToParent(InetAddress.getByName("127.0.0.1"), 2012);

        System.out.println("~~~All Proper Parent Connections Established~~~");

        System.out.println("connectToParent -> Timeout");

        int sentMessage = masterServer.connectToParent(InetAddress.getByName("127.0.0.1"), 1000);
        if (sentMessage == CommError.ERROR_TIMEOUT) {
            System.out.println(CommError.ERROR_TIMEOUT + ": Timeout");
        }


        Object obj; // Our received object.
        String objectExample; // Our test object to send.


        /******************************************************************
         **************************** IMPORTANT ***************************
         ******************************************************************/
        /*
         * Each custom class sent over Comm MUST IMPLEMENT Serializable.
         * See any object in FinalProject.communication.communicationobjects for details.
         */



        // MasterServer -> District Servers
        System.out.println("\nMasterServer -> District Servers");
        objectExample = "Information from Master";
        masterServer.sendMessageClient(objectExample);

        // Getting the message from the queues.  Normally you would have a separate thread.
        // Intentionally verbose so you can understand the workings.
        obj = districtServer1.getMessageBlocking();
        if (obj instanceof String) {
            String receivedString = (String) obj;
            System.out.println("D1 : " + receivedString);
        }
        obj = districtServer2.getMessageBlocking();
        if (obj instanceof String) {
            String receivedString = (String) obj;
            System.out.println("D2 : " + receivedString);
        }



        // MasterServer -> Parent (error)
        System.out.println("\nMasterServer -> Parent (error)");
        objectExample = "This message will never make it";
        sentMessage = masterServer.sendMessageParent(objectExample);
        if (sentMessage == CommError.ERROR_NO_PARENT) {
            System.out.println(CommError.ERROR_NO_PARENT + ": No Parent");
        }



        // District Servers -> MasterServer
        System.out.println("\nDistrict Servers -> MasterServer");
        objectExample = "Reply from District 1";
        districtServer1.sendMessageParent(objectExample);
        objectExample = "Reply from District 2";
        districtServer2.sendMessageParent(objectExample);

        // Getting the message from the queues.  Normally you would have a separate thread.
        // Intentionally verbose so you can understand the workings.
        obj = masterServer.getMessageBlocking();
        if (obj instanceof String) {
            String receivedString = (String) obj;
            System.out.println("MA : " + receivedString);
        }
        obj = masterServer.getMessageBlocking();
        if (obj instanceof String) {
            String receivedString = (String) obj;
            System.out.println("MA : " + receivedString);
        }



        // DistrictServer1 -> Client Servers
        System.out.println("\nDistrictServer1 -> Client Servers");
        objectExample = "Information for Clients of District 1";
        districtServer1.sendMessageClient(objectExample);

        // Getting the message from the queues.  Normally you would have a separate thread.
        // Intentionally verbose so you can understand the workings.
        obj = clientServer1.getMessageBlocking();
        if (obj instanceof String) {
            String receivedString = (String) obj;
            System.out.println("C1 : " + receivedString);
        }
        obj = clientServer2.getMessageBlocking();
        if (obj instanceof String) {
            String receivedString = (String) obj;
            System.out.println("C2 : " + receivedString);
        }


        // DistrictServer2 -> Client Servers
        System.out.println("\nDistrictServer2 -> Client Servers");
        objectExample = "Information for Clients of District 2";
        districtServer2.sendMessageClient(objectExample);

        // Getting the message from the queues.  Normally you would have a separate thread.
        // Intentionally verbose so you can understand the workings.
        obj = clientServer3.getMessageBlocking();
        if (obj instanceof String) {
            String receivedString = (String) obj;
            System.out.println("C3 : " + receivedString);
        }
        obj = clientServer4.getMessageBlocking();
        if (obj instanceof String) {
            String receivedString = (String) obj;
            System.out.println("C4 : " + receivedString);
        }



        // Client Servers -> DistrictServer1
        System.out.println("\nClient Servers -> DistrictServer1");
        objectExample = "Reply from Client 1";
        clientServer1.sendMessageParent(objectExample);
        objectExample = "Reply from Client 2";
        clientServer2.sendMessageParent(objectExample);

        // Getting the message from the queues.  Normally you would have a separate thread.
        // Intentionally verbose so you can understand the workings.
        obj = districtServer1.getMessageBlocking();
        if (obj instanceof String) {
            String receivedString = (String) obj;
            System.out.println("D1 : " + receivedString);
        }
        obj = districtServer1.getMessageBlocking();
        if (obj instanceof String) {
            String receivedString = (String) obj;
            System.out.println("D1 : " + receivedString);
        }



        // DClient Servers -> DistrictServer2
        System.out.println("\nClient Servers -> DistrictServer2");
        objectExample = "Reply from Client 3";
        clientServer3.sendMessageParent(objectExample);
        objectExample = "Reply from Client 4";
        clientServer4.sendMessageParent(objectExample);

        // Getting the message from the queues.  Normally you would have a separate thread.
        // Intentionally verbose so you can understand the workings.
        obj = districtServer2.getMessageBlocking();
        if (obj instanceof String) {
            String receivedString = (String) obj;
            System.out.println("D2 : " + receivedString);
        }
        obj = districtServer2.getMessageBlocking();
        if (obj instanceof String) {
            String receivedString = (String) obj;
            System.out.println("D2 : " + receivedString);
        }




        // DistrictServer1 ---Broadcast--> Master and Clients
        System.out.println("\nDistrictServer1 ---Broadcast--> Master and Clients");
        objectExample = "Broadcast Message from DistrictServer1";
        districtServer1.sendMessageBroadcast(objectExample);

        // Getting the message from the queues.  Normally you would have a separate thread.
        // Intentionally verbose so you can understand the workings.
        obj = clientServer1.getMessageBlocking();
        if (obj instanceof String) {
            String receivedString = (String) obj;
            System.out.println("C1 : " + receivedString);
        }
        obj = clientServer2.getMessageBlocking();
        if (obj instanceof String) {
            String receivedString = (String) obj;
            System.out.println("C2 : " + receivedString);
        }
        obj = masterServer.getMessageBlocking();
        if (obj instanceof String) {
            String receivedString = (String) obj;
            System.out.println("MA : " + receivedString);
        }



        // ClientServer1 -> DistrictServer1 --Reply-> ClientServer1
        System.out.println("\nClientServer1 -> DistrictServer1 --Reply-> ClientServer1");
        objectExample = "Reply needed message for DistrictServer1 from Client 1";
        clientServer1.sendMessageParent(objectExample);

        // Getting the message from the queues.  Normally you would have a separate thread.
        // Intentionally verbose so you can understand the workings.
        obj = districtServer1.getMessageBlocking();
        if (obj instanceof String) {
            String receivedString = (String) obj;
            System.out.println("D1 : " + receivedString);

            // Sending the reply.
            objectExample = "Reply message for Client 1 from DistrictServer1";
            districtServer1.sendMessageReply(objectExample);
        }
        obj = clientServer1.getMessageBlocking();
        if (obj instanceof String) {
            String receivedString = (String) obj;
            System.out.println("C2 : " + receivedString);
        }

        System.out.println("\nShutting Down Gracefully");
        masterServer.shutdown();
        districtServer1.shutdown();
        clientServer1.shutdown();
        clientServer2.shutdown();
        districtServer2.shutdown();
        clientServer3.shutdown();
        clientServer4.shutdown();

    }
}
