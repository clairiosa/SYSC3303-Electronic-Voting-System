package FinalProject.filereaders;

/**
 * Created by natebosscher on 15-04-06.
 *
 */
public class CSVFileReader {
    protected String filename;

    public CSVFileReader(String filename){
        this.filename = filename;
    }

    public String clean(String str){
        String s = str.trim();
        return s.replaceAll("^\"", "").replaceAll("\"$", "");
    }

    public void parse(){}
}
