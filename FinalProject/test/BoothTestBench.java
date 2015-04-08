package FinalProject.test;

import FinalProject.filereaders.VoterReader;
import FinalProject.booth.Booth;
import FinalProject.districtserver.DistrictServer;
import FinalProject.masterserver.MasterServer;
import FinalProject.persons.Voter;

import java.io.IOException;
import java.util.Stack;

/**
 * Created by natebosscher on 15-04-06.
 */
public class BoothTestBench {
    private final Thread m,d0,d1,d2;
    protected Booth b1, b2, b3, b4;

    // voter by district
    protected Stack<Voter> vd1, vd2, vd3;

    public BoothTestBench(){
        m = new Thread() {
            public void run() {
                MasterServer.main(new String[] { "2000", "FinalProject/test/voters.txt",
                        "FinalProject/test/candidates.txt", "5000" });
            }
        };

        d0 = new Thread() {
            public void run() {
                DistrictServer.main(new String[] { "2011", "127.0.0.1", "2000",
                        "0" });
            }
        };
        d1 = new Thread() {
            public void run() {
                DistrictServer.main(new String[] { "2012", "127.0.0.1", "2000",
                        "1" });
            }
        };
        d2 = new Thread() {
            public void run() {
                DistrictServer.main(new String[] { "2013", "127.0.0.1", "2000",
                        "2" });
            }
        };

        m.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        d0.start();
        d1.start();
        d2.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            b1 = new Booth("127.0.0.1", "d1", Integer.parseInt("2011"), Integer.parseInt("2101"));
            b2 = new Booth("127.0.0.1", "d2", Integer.parseInt("2012"), Integer.parseInt("2102"));
            b3 = new Booth("127.0.0.1", "d3", Integer.parseInt("2013"), Integer.parseInt("2203"));
            b4 = new Booth("127.0.0.1", "d3", Integer.parseInt("2013"), Integer.parseInt("2204"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            return;
        }

        b1.run();
        b2.run();
        b3.run();
        b4.run();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Test bench ready");
    }

    protected void loadVoters(){
        VoterReader vr = new VoterReader("FinalProject/test/voters.txt");
        vd1 = new Stack<>();
        vd2 = new Stack<>();
        vd3 = new Stack<>();

        for(int i = 0; i < vr.voters.size(); i++){
            if(vr.voters.get(i).getDistrictId().equals("0")){
                vd1.add(vr.voters.get(i));
            }else if(vr.voters.get(i).getDistrictId().equals("1")){
                vd2.add(vr.voters.get(i));
            }else if(vr.voters.get(i).getDistrictId().equals("2")){
                vd3.add(vr.voters.get(i));
            }
        }
    }

    public boolean test(){ // fill in
        return false;
    }

    public void destroy(){
        try {
            MasterServer.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            m.join();
            d0.join();
            d1.join();
            d2.join();

            b1.join();
            b2.join();
            b3.join();
            b4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MasterServer.reset();
    }
}
