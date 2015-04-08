package FinalProject.test;

import FinalProject.filereaders.CandidateReader;
import FinalProject.persons.Voter;

/**
 * Created by natebosscher1 on 15-04-07.
 */
public class bTest3 extends BoothTestBench {
    /*
        Test voting results
     */
    public static void main(String[] args){
        bTest3 b3 = new bTest3();
        assert b3.test();

        System.out.println("TEST 3 COMPLETE");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        b3.destroy();

        System.out.println("TEST 3 REMOVED");
    }

    public boolean test(){
        loadVoters();
        CandidateReader dr = new CandidateReader("FinalProject/test/candidates.txt");
        dr.parse();

        Voter v;

        // everyone votes for candidate 0
        v = vd1.pop();
        b1.register(v);
        b1.verify(v.getPin());
        b1.vote(dr.candidates.get(0));

        v = vd2.pop();
        b2.register(v);
        b2.verify(v.getPin());
        b2.vote(dr.candidates.get(0));

        v = vd3.pop();
        b3.register(v);
        b3.verify(v.getPin());
        b3.vote(dr.candidates.get(0));

        return true;
    }
}
