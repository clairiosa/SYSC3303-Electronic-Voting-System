package FinalProject.test;

import FinalProject.filereaders.CandidateReader;
import FinalProject.filereaders.VoterReader;
import FinalProject.masterserver.MasterServer;
import FinalProject.test.BoothTestBench;
import FinalProject.persons.Voter;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

/**
 * Created by natebosscher on 15-04-06.
 */
public class bTest1 extends BoothTestBench {
    /*
        Test voter registration
     */

    public static void main(String[] args){
        bTest1 b1 = new bTest1();
        System.out.println("Test 1 Instantiated");
        assert b1.test();


        System.out.println("TEST 1 COMPLETE");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MasterServer.frame.dispose();

        b1.destroy();

        System.out.println("TEST 1 REMOVED");
    }

    public boolean test() {
        loadVoters();
        CandidateReader dr = new CandidateReader("FinalProject/test/candidates.txt");

        Voter v;

        // can't register twice, covers case of voting twice
        v = vd1.pop();
        assert b1.register(v);
        assert !b1.register(v);

        // can't register in an incorrect district before correct registration
        v = vd1.pop();
        assert !b2.register(v);
        assert !b3.register(v);
        assert b1.register(v);

        // can't register in an incorrect district after correct registration
        v = vd2.pop();
        assert b2.register(v);
        assert !b2.register(v);
        assert !b1.register(v);
        assert !b3.register(v);

        // can't register in an incorrect district before or after correct registration
        v = vd3.pop();
        assert !b2.register(v);
        assert b3.register(v);
        assert !b4.register(v);

        // can't register in an incorrect district before or after correct registration
        v = vd3.pop();
        assert !b2.register(v);
        assert b4.register(v);
        assert !b3.register(v);

        // complete
        return true;
    }
}
