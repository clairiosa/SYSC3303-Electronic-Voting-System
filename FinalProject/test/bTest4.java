package FinalProject.test;

import FinalProject.filereaders.CandidateReader;
import FinalProject.persons.Voter;

/**
 * Created by natebosscher1 on 15-04-07.
 */
public class bTest4 extends BoothTestBench {
    /*
        Test voting results
     */
    public static void main(String[] args){
        bTest4 b4 = new bTest4();
        assert b4.test();

        System.out.println("TEST 3 COMPLETE");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        b4.destroy();

        System.out.println("TEST 3 REMOVED");
    }

    public boolean test(){
        loadVoters();
        CandidateReader dr = new CandidateReader("FinalProject/test/candidates.txt");
        dr.parse();

        Voter v;

        // everyone votes for a different candidate
        v = vd1.pop();
        b1.register(v);
        b1.verify(v.getPin());
        b1.vote(dr.candidates.get(0));

        v = vd2.pop();
        b2.register(v);
        b2.verify(v.getPin());
        b2.vote(dr.candidates.get(1));

        v = vd3.pop();
        b3.register(v);
        b3.verify(v.getPin());
        b3.vote(dr.candidates.get(2));

        v = vd3.pop();
        b4.register(v);
        b4.verify(v.getPin());
        b4.vote(dr.candidates.get(3));

        return true;
    }
}
