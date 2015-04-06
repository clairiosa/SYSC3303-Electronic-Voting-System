
package FinalProject.booth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import FinalProject.Ballot;
import FinalProject.BoothElectionResult;
import FinalProject.BoothElectionResults;
import FinalProject.Credential;
import FinalProject.communication.Comm;
import FinalProject.persons.Candidate;
import FinalProject.persons.Voter;

public class Booth extends Thread {
    final private Comm clientServer;
    private Voter voter;

    private InetAddress parentIP;
    private String districtId;
    private int parentPort;
    private Object cmdInProgress;
    private boolean dummyData;
    
    private BlockingQueue<Candidate[]> _candidates;
    private BlockingQueue<BoothElectionResults> _results;
    private BlockingQueue<String> _msgs;
    
    private final BoothUI window; 
    private boolean exit;
    

    public Booth(String parentServer, String districtId, int port, int listenPort) throws UnknownHostException, IOException, InterruptedException{
        parentIP = InetAddress.getByName(parentServer);
        parentPort = port;
        dummyData = false;
        cmdInProgress = new Object();
        this.districtId = districtId;
        exit = false;

        _candidates = new ArrayBlockingQueue<Candidate[]>(10);
        _results = new ArrayBlockingQueue<BoothElectionResults>(10);
        _msgs = new ArrayBlockingQueue<String>(10);

		this.clientServer = new Comm(listenPort);
		clientServer.connectToParent(parentIP, parentPort);
		window = new BoothUI(this, districtId);
		Thread.sleep(5000);
    }

    public void shutdown(){
        try {
            clientServer.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        window.exit();
    }
    
    public void recv(){
    	Object msg = null;
        while(msg == null) {
            try {
                msg = clientServer.getMessageBlocking(10, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    	
    	try {
	    	if(msg instanceof Candidate[]){
	    		_candidates.put((Candidate[])msg);
	    	}else if(msg instanceof BoothElectionResults){
	    		_results.put((BoothElectionResults)msg);
	    	}else if(msg instanceof String){
	    		if(((String)msg).equals("done")){
                    exit = true;
                    shutdown();
	    			return;
	    		}
				_msgs.put((String)msg);
	    	}else{
	    		System.out.println("unknown type");
	    	}
    	} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    public void run(){
        window.start();

        (new Thread() {
            public void run() {
                while(true){
                    window.updateStats(getElectionStatus());

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                    if(exit)
                    	return;
                }
            }}).start();
    }


    public void clearVoter(){
        this.voter = null;
    }

    public Candidate[] getCandidates(){
        if(dummyData){
            Candidate[] clist = new Candidate[2];
            clist[0] = new Candidate("Jack", "Conservative");
            clist[1] = new Candidate("Bob", "Liberal");

            return clist;
        }else{
            try {
                this.clientServer.sendMessageParent("candidates");
            } catch (IOException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
            
            recv();

            Candidate[] res = null;
			try {
				res = _candidates.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            return res;
        }
    }

    public BoothElectionResults getElectionStatus(){

        if(dummyData){
            BoothElectionResult[] rs = new BoothElectionResult[2];

            Candidate c1 = new Candidate("Bob", "Liberal");
            Candidate c2 = new Candidate("Fred", "Conservative");

            rs[0] = new BoothElectionResult(c1, 2);
            rs[1] = new BoothElectionResult(c2, 1);

            BoothElectionResults r = new BoothElectionResults(rs,3);

            return r;
        }else{
        	BoothElectionResults res = null;
            try {
                this.clientServer.sendMessageParent("status");
            } catch (IOException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
            
            recv();
            
            try {
                res = _results.take();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        	
            return res;
        }
    }

    public void testCommand(Candidate[] c, String pin, String user){
        int index = (int)(Math.random() * c.length % 1);

        Voter v = new Voter(user, null);

        if(register(v)){
            System.out.print("Registered ");
            if(verify(pin)){
                System.out.print("Verified ");

                if(vote(c[index])){
                    System.out.print("Voted for " + c[index].getName());
                }else{
                    System.out.println("VOTE ERROR");
                    System.exit(-1);
                }
            }else{
                System.out.println("VERIFY ERROR");
                System.exit(-1);
            }
        }else{
            System.out.println("Couldn't register");
        }

        if (v.getName().equals("Ellena Jeanbaptiste")) {
            try {
                clientServer.shutdown();
            } catch (InterruptedException e) {
                System.exit(0);
            }
            System.exit(0);
        }

        System.out.print("\n");
    }

    public void parse(String userFile){
        Candidate[] c = getCandidates();
        File votersFile = new File (userFile);

        try{
            FileReader fr = new FileReader(votersFile);
            BufferedReader br = new BufferedReader(fr);
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                String everything = sb.toString();

                String lst[] = everything.split("\r\n");

                for(int i=0; i < lst.length-1;i++){
                    testCommand(c, lst[i], lst[i+1]);
                }

            } finally {
                br.close();
            }
        }catch(FileNotFoundException e){
            System.out.println("file doesn't exist");
            System.exit(-1);
            return;
        }catch(IOException e){
            e.printStackTrace();
            return;
        }
    }

    public boolean register(Voter p){
        String s;

        try {
            this.clientServer.sendMessageParent(p);
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        recv();
        
        try {
            s = _msgs.take();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        this.voter = p;
        
        return s.equals("true");
    }

    public boolean verify(String pin){
        Credential c = new Credential(voter.getName(), pin);
        String s;
        
        try {
            this.clientServer.sendMessageParent(c);
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        
        recv();

        try {
            s = _msgs.take();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return s.equals("true");
    }

    public boolean vote(Candidate c){
        if(this.voter == null){
            System.out.println("Unknown State");
            return false;
        }
        String s;

        Ballot b = new Ballot(null, this.voter, c);

        synchronized(cmdInProgress){
            try {
                this.clientServer.sendMessageParent(b);
            } catch (IOException | InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return false;
            }

            recv();

            try {
                s = _msgs.take();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }

        this.voter = null;
        return s.equals("true");
    }

    public static void main(String args[]) {

        if(args.length != 4 && args.length != 5){
            System.out.print("Booth serverIp districtId serverPort listenPort [test file.txt]");
            return;
        }

        Booth booth;
        try {
            booth = new Booth(args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        }catch(NumberFormatException e){
            System.out.println("Invalid port number");
            return;
        } catch (UnknownHostException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            System.out.println("Unknown host error");
            return;
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("IO exception");
            return;
        }catch(InterruptedException e){
            e.printStackTrace();
            System.out.println("Interrupted IO exception");
            return;
        }

        if(args.length == 5){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            booth.parse(args[4]);
        }else{
            booth.run();
        }

    }
}