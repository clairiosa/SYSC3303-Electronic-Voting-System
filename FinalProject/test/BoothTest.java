package FinalProject.test;

import FinalProject.masterserver.MasterServer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by natebosscher on 15-04-06.
 */
public class BoothTest {

    public static void main(String args[]) {

        Thread t = (new Thread() {
            public void run() {
                bTest1.main(null);
            }
        });
        t.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("-----------------------");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t = (new Thread() {
            public void run() {
                bTest2.main(null);
            }
        });

        t.start();


    }
}
