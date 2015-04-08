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

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        b2.destroy();

        System.out.println("TEST 2 REMOVED");
    }

    public boolean test(){
        loadVoters();
        CandidateReader dr = new CandidateReader("FinalProject/test/candidates.txt");

        Voter v;

        // verify at booth 1
        v = vd1.pop();
        b1.register(v);
        assert b1.verify(v.getPin());

        // verify at booth 2
        v = vd2.pop();
        b2.register(v);
        assert b2.verify(v.getPin());

        // verify at booth 3
        v = vd3.pop();
        b3.register(v);
        assert b3.verify(v.getPin());

        // verify at booth 4
        v = vd3.pop();
        b4.register(v);
        assert b4.verify(v.getPin());

        // verify with incorrect pin at booth 4
        v = vd3.pop();
        b4.register(v);
        assert !b4.verify(v.getPin() + "1");

        // verify with incorrect pin at booth 3
        v = vd3.pop();
        b3.register(v);
        assert !b3.verify(v.getPin() + "1");

        // verify with incorrect pin at booth 2
        v = vd2.pop();
        b2.register(v);
        assert !b2.verify(v.getPin() + "1");

        // verify with incorrect pin at booth 1
        v = vd1.pop();
        b1.register(v);
        assert !b1.verify(v.getPin() + "1");

        // complete
        return true;
    }
}
