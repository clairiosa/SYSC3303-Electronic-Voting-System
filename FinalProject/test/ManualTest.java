/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	ManualTest.java
 *
 * Launches 4 booths, attached to 3 districts, attached to one master for manual testing.
 *
 */

package FinalProject.test;

import FinalProject.booth.Booth;
import FinalProject.districtserver.DistrictServer;
import FinalProject.masterserver.MasterServer;

public class ManualTest {

    public static void main(String args[]) throws InterruptedException {
        System.out.println("\nAutomated test of the SYSC3303 Electronic Voting System");

        Thread masterServer = new Thread() {
            public void run() {
                MasterServer.main(new String[] { "2001", "FinalProject/test/voters.txt", "FinalProject/test/candidates.txt", "5000" });
            }
        };

        Thread districtServer1 = new Thread() {
            public void run() {
                DistrictServer.main(new String[] { "2011", "127.0.0.1", "2001", "1" });
            }
        };
        Thread districtServer2 = new Thread() {
            public void run() {
                DistrictServer.main(new String[] { "2012", "127.0.0.1", "2001", "2" });
            }
        };
        Thread districtServer3 = new Thread() {
            public void run() {
                DistrictServer.main(new String[] { "2013", "127.0.0.1", "2001", "3" });
            }
        };

        Thread boothServer1 = new Thread() {
            public void run() {
                Booth.main(new String[] { "127.0.0.1", "2011", "2101" });
            }
        };
        Thread boothServer2 = new Thread() {
            public void run() {
                Booth.main(new String[] { "127.0.0.1", "2011", "2102" });
            }
        };
        Thread boothServer3 = new Thread() {
            public void run() {
                Booth.main(new String[] { "127.0.0.1", "2012", "2103" });
            }
        };
        Thread boothServer4 = new Thread() {
            public void run() {
                Booth.main(new String[] { "127.0.0.1", "2013", "2104" });
            }
        };

        masterServer.start();
        districtServer1.start();
        districtServer2.start();
        districtServer3.start();




        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boothServer1.start();
        boothServer2.start();
        boothServer3.start();
        boothServer4.start();

        masterServer.join();
        districtServer1.join();
        districtServer2.join();
        districtServer3.join();

        boothServer1.join();
        boothServer2.join();
        boothServer3.join();
        boothServer4.join();
    }
}
