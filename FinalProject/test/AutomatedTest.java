/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	AutomatedTest.java
 *
 * Implementation of CommInterface.
 *
 * Responsible for all network traffic within the Electronic Voting System.  Simulates TCP/IP through UDP (Uses
 * ACKs, Checksums and requests retransmission of corrupted packets).  Each server/client implements an instance
 * of this class.
 *
 */


package FinalProject;

import FinalProject._booth.Booth;
//import FinalProject.masterserver.MasterServer;
import FinalProject.districtserver.DistrictServer;


public class AutomatedTest {

    public static void main(String args[]) {
        System.out.println("\nAutomated test of the SYSC3303 Electronic Voting System");

        /*
        Thread masterServer = new Thread(){
            public void run() {
                MasterServer.main(new String[]{"2000", "voterFile.txt", "candidateFile.txt", "5000"});
            }
        };
        */
        Thread districtServer = new Thread(){
            public void run() {
                DistrictServer.main(new String[]{"2010", "127.0.0.1", "2000", "0", "test"});
            }
        };
        Thread boothServer1 = new Thread(){
            public void run() {
                Booth.main(new String[]{"127.0.0.1", "2010", "2101"});
            }
        };

        districtServer.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boothServer1.start();
        /*
        Thread boothServer2 = new Thread(){
            public void run() {
                Booth.main(new String[]{"127.0.0.1", "2010", "2102"});
            }
        };
        Thread boothServer3 = new Thread(){
            public void run() {
                Booth.main(new String[]{"127.0.0.1", "2010", "2103"});
            }
        };
        */


    }
}
