package FinalProject.test;

import FinalProject.masterserver.MasterServer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by natebosscher on 15-04-06.
 */
public class BoothTest {
    public static void reset(){
        try {
            MasterServer.shutdown();
            MasterServer.reset();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {

        Thread t = (new Thread() {
            public void run() {
                bTest1.main(null);
            }
        });

        t.start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t.interrupt();

        t = (new Thread() {
            public void run() {
                bTest2.main(null);
            }
        });

        t.start();
        t.interrupt();

    }
}
