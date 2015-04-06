package FinalProject;

import FinalProject.persons.Voter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by natebosscher on 15-04-06.
 */
public class VoterReader extends CSVFileReader {
    public ArrayList<Voter> voters;

    public VoterReader(String filename) {
        super(filename);
        voters = new ArrayList<>();
    }

    public void parse(){
        try {
            FileInputStream fis1 = new FileInputStream(filename);

            // Construct BufferedReader from InputStreamReader
            BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1));
            Voter v = null;
            String[] cols;
            String name, address, postal, city, province, district, pin;
            String line;
            int i = 0;

            while ((line = br1.readLine()) != null) {
                if(!line.substring(0,1).equals("#")) {
                    cols = line.split(",");

                    if(cols.length == 7) {
                        name = clean(cols[0]);
                        address = clean(cols[1]);
                        postal = clean(cols[2]);
                        city = clean(cols[3]);
                        province = clean(cols[4]);
                        district = clean(cols[5]);
                        pin = clean(cols[6]);

                        v = new Voter(name, address, postal, city, province, district, null, pin, null, null, false, false);

                        voters.add(v);
                    }else{
                        System.out.println("Format error on line " + i);
                    }
                }

                i++;
            }

            br1.close();
        } catch (Exception e) {
            System.out.println("Error reading voters file.");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
