/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	AutomatedTest.java
 *
 * Runs a fully automated test.
 *
 */

package FinalProject.test;

import FinalProject.booth.Booth;
import FinalProject.districtserver.DistrictServer;
import FinalProject.masterserver.MasterServer;

public class AutomatedTest {

	public static void main(String args[]) {
		System.out
				.println("\nAutomated test of the SYSC3303 Electronic Voting System");

		Thread masterServer = new Thread() {
			public void run() {
				MasterServer.main(new String[] { "2000", "voters.txt",
						"candidates.txt", "5000" });
			}
		};

		Thread districtServer = new Thread() {
			public void run() {
				DistrictServer.main(new String[] { "2010", "127.0.0.1", "2000",
						"1" });
				// DistrictServer.main(new String[]{"2010", "127.0.0.1", "2000",
				// "1", "candidates.txt", "voters.txt"});
			}
		};
		// Thread boothServer1 = new Thread(){
		// public void run() {
		// DistrictServer.main(new String[]{"2010", "127.0.0.1", "2000", "0",
		// "candidates.txt", "voters.txt"});
		// }
		// };
		//
		Thread boothServer1 = new Thread() {
			public void run() {
				Booth.main(new String[] { "127.0.0.1", "2010", "2101" });
				// Booth.main(new String[]{"127.0.0.1", "2010", "2101",
				// "./src/FinalProject/test/voters.txt"});
			}
		};

		masterServer.start();
		districtServer.start();



		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		 boothServer1.start();
		/*
		 * Thread boothServer2 = new Thread(){ public void run() {
		 * Booth.main(new String[]{"127.0.0.1", "2010", "2102"}); } }; Thread
		 * boothServer3 = new Thread(){ public void run() { Booth.main(new
		 * String[]{"127.0.0.1", "2010", "2103"}); } };
		 */

	}
}
