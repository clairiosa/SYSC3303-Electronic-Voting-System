package FinalProject.booth;

import FinalProject.VoterReader;
import FinalProject.districtserver.DistrictServer;
import FinalProject.masterserver.MasterServer;
import FinalProject.persons.Voter;

import java.io.IOException;
import java.util.Stack;

/**
 * Created by natebosscher on 15-04-06.
 */
public class BoothTestBench {

    protected Booth b1, b2, b3, b4;
    // voter by district
    protected Stack<Voter> vd1, vd2, vd3;

    public BoothTestBench(){
        Thread m = new Thread() {
            public void run() {
                MasterServer.main(new String[] { "2000", "FinalProject/test/voters.txt",
                        "FinalProject/test/candidates.txt", "5000" });
            }
        };

        Thread d0 = new Thread() {
            public void run() {
                DistrictServer.main(new String[] { "2011", "127.0.0.1", "2000",
                        "0" });
            }
        };
        Thread d1 = new Thread() {
            public void run() {
                DistrictServer.main(new String[] { "2012", "127.0.0.1", "2000",
                        "1" });
            }
        };
        Thread d2 = new Thread() {
            public void run() {
                DistrictServer.main(new String[] { "2013", "127.0.0.1", "2000",
                        "2" });
            }
        };

        m.start();

        System.out.println("Master started");

        d0.start();
        d1.start();
        d2.start();

        System.out.println("Districts started");

        try {
            Thread.sleep(2000);
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
            e.printStackTrace();
        }

        System.out.println("Booths started");
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
}
