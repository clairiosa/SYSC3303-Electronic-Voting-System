package FinalProject.test;

import FinalProject.filereaders.CandidateReader;
import FinalProject.masterserver.MasterServer;
import FinalProject.persons.Voter;

/**
 * Created by natebosscher1 on 15-04-07.
 */
public class bTest5 extends BoothTestBench {
    /*
        Test voting results
     */
    public static void main(String[] args){
        bTest5 b5 = new bTest5();
        assert b5.test();

        System.out.println("TEST 5 COMPLETE");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        MasterServer.frame.dispose();

        b5.destroy();

        System.out.println("TEST 5 REMOVED");
    }

    public boolean test(){
        loadVoters();
        CandidateReader dr = new CandidateReader("FinalProject/test/candidates.txt");
        dr.parse();

//        Voter v;

        for(Voter v : vd1){
            b1.register(v);
            b1.verify(v.getPin());
            int randomIndex = (int) Math.round(Math.random() * (dr.candidates.size() -1));
            b1.vote(dr.candidates.get(randomIndex));
        }

        for(Voter v : vd2){
            b2.register(v);
            b2.verify(v.getPin());
            int randomIndex = (int) Math.round(Math.random() * (dr.candidates.size() -1));
            b2.vote(dr.candidates.get(randomIndex));
        }

        for(Voter v : vd3){
            b3.register(v);
            b3.verify(v.getPin());
            int randomIndex = (int) Math.round(Math.random() * (dr.candidates.size() -1));
            b3.vote(dr.candidates.get(randomIndex));
        }

        return true;
    }
}
