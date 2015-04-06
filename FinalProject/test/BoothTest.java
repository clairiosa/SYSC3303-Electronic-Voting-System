package FinalProject.test;

/**
 * Created by natebosscher on 15-04-06.
 */
public class BoothTest {
    public static void main(String args[]) {
        System.out.println("Booth Test Starting...");

        bTest1 b1 = new bTest1();
        System.out.println("Test 1 Instantiated");
        assert b1.test();
        b1.destroy();

        System.out.println("TEST 1 COMPLETE");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        bTest2 b2 = new bTest2();
        assert b2.test();
        b2.destroy();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("TEST 2 COMPLETE");
    }
}
