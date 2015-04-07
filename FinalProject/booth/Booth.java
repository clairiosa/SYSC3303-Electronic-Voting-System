
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

import FinalProject.electionobjects.Ballot;
import FinalProject.resultdata.ResultItem;
import FinalProject.resultdata.BoothElectionResults;
import FinalProject.electionobjects.Credential;
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
    private Thread statusUpdateThread;


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
        System.out.println("client shutdown");

        try {
            clientServer.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        statusUpdateThread.interrupt();
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
	    		if(((String)msg).equals("end")){
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

        statusUpdateThread = new Thread() {
            public void run() {
                while (true) {
                    window.updateStats(getElectionStatus());

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (exit)
                        return;
                }
            }
        };

        statusUpdateThread.start();

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
            ResultItem[] rs = new ResultItem[2];

            Candidate c1 = new Candidate("Bob", "Liberal");
            Candidate c2 = new Candidate("Fred", "Conservative");

            rs[0] = new ResultItem(c1, 2);
            rs[1] = new ResultItem(c2, 1);

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
            System.out.println("Invalid args");
        }else{
            booth.run();
        }

    }
}