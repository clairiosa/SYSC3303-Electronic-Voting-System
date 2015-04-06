package FinalProject.test;

import FinalProject.CandidateReader;
import FinalProject.VoterReader;
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

        try {
            System.setIn(new ByteArrayInputStream("done".getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        b1.destroy();

        System.out.println("TEST 1 COMPLETE");
    }

    public boolean test() {
        loadVoters();
        CandidateReader dr = new CandidateReader("FinalProject/test/candidates.txt");

        Voter v;

        v = vd1.pop();
        assert b1.register(v);
        assert !b1.register(v);

        v = vd1.pop();
        assert !b2.register(v);
        assert !b3.register(v);
        assert b1.register(v);

        v = vd2.pop();
        assert b2.register(v);
        assert !b2.register(v);
        assert !b1.register(v);
        assert !b3.register(v);

        v = vd3.pop();
        assert !b2.register(v);
        assert b3.register(v);
        assert !b4.register(v);

        v = vd3.pop();
        assert !b2.register(v);
        assert b4.register(v);
        assert !b3.register(v);

        return true;
    }
}
