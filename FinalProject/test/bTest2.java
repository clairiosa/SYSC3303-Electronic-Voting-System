package FinalProject.test;

import FinalProject.CandidateReader;
import FinalProject.test.BoothTestBench;
import FinalProject.persons.Voter;

/**
 * Created by natebosscher on 15-04-06.
 */
public class bTest2 extends BoothTestBench {
    /*
        Test voter verification
     */

    public boolean test(){
        loadVoters();
        CandidateReader dr = new CandidateReader("FinalProject/test/candidates.txt");

        Voter v;

        v = vd1.pop();
        b1.register(v);
        assert b1.verify(v.getPin());

        v = vd2.pop();
        b2.register(v);
        assert b2.verify(v.getPin());

        v = vd3.pop();
        b3.register(v);
        assert b3.verify(v.getPin());

        v = vd3.pop();
        b4.register(v);
        assert b4.verify(v.getPin());


        v = vd3.pop();
        b4.register(v);
        assert !b4.verify(v.getPin() + "1");

        v = vd3.pop();
        b3.register(v);
        assert !b3.verify(v.getPin() + "1");

        v = vd2.pop();
        b2.register(v);
        assert !b2.verify(v.getPin() + "1");

        v = vd1.pop();
        b1.register(v);
        assert !b1.verify(v.getPin() + "1");

        return true;
    }

    public void destroy(){
        b1.shutdown();
        b2.shutdown();
        b3.shutdown();
        b4.shutdown();
    }
}
