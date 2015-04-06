package FinalProject.test;

import FinalProject.CandidateReader;
import FinalProject.VoterReader;
import FinalProject.test.BoothTestBench;
import FinalProject.persons.Voter;

import java.util.LinkedList;

/**
 * Created by natebosscher on 15-04-06.
 */
public class bTest1 extends BoothTestBench {
    /*
        Test voter registration
     */

    public boolean test(){
        loadVoters();
        CandidateReader dr = new CandidateReader("FinalProject/test/candidates.txt");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

    public void destroy(){
//
//        b1.shutdown();
//        b2.shutdown();
//        b3.shutdown();
//        b4.shutdown();
    }
}
