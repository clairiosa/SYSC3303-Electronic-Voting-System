package FinalProject.test;

import FinalProject.filereaders.CandidateReader;
import FinalProject.test.BoothTestBench;
import FinalProject.persons.Voter;

/**
 * Created by natebosscher on 15-04-06.
 */
public class bTest2 extends BoothTestBench {
    /*
        Test voter verification
     */
    public static void main(String[] args){
        bTest2 b2 = new bTest2();
        assert b2.test();

        System.out.println("TEST 2 COMPLETE");
    }

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
}
