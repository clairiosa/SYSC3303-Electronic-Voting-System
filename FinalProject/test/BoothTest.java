package FinalProject.test;

import FinalProject.masterserver.MasterServer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by natebosscher on 15-04-06.
 */
public class BoothTest {

    public static void main(String args[]) throws InterruptedException {

        Thread t = (new Thread() {
            public void run() {
                bTest1.main(null);
            }
        });
        t.start();
        t.join();

        System.out.println("-----------------------");
        Thread.sleep(5000);

        t = (new Thread() {
            public void run() {
                bTest2.main(null);
            }
        });

        t.start();
        t.join();


        System.out.println("-----------------------");
        Thread.sleep(5000);

        t = (new Thread() {
            public void run() {
                bTest3.main(null);
            }
        });

        t.start();
        t.join();

        System.out.println("-----------------------");
        Thread.sleep(5000);

        t = (new Thread() {
            public void run() {
                bTest4.main(null);
            }
        });

        t.start();
        t.join();

        System.out.println("-----------------------");
        Thread.sleep(5000);

        System.exit(0);
    }
}
