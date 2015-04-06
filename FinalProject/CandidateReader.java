package FinalProject;

import FinalProject.persons.Candidate;
import FinalProject.persons.Voter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by natebosscher on 15-04-06.
 */
public class CandidateReader extends CSVFileReader {

    public ArrayList<Candidate> candidates;

    public CandidateReader(String filename){
        super(filename);
        candidates = new ArrayList<>();
    }

    public void parse(){
        try {
            FileInputStream fis1 = new FileInputStream(filename);

            // Construct BufferedReader from InputStreamReader
            BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1));
            Candidate c = null;
            String[] cols;
            String line, name, party;
            int i = 0;

            while ((line = br1.readLine()) != null) {
                if(!line.substring(0,1).equals("#")) {
                    cols = line.split(",");
                    if(cols.length == 2){
                        name = clean(cols[0]);
                        party = clean(cols[1]);

                        c = new Candidate(name, party);
                        candidates.add(c);
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
