
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
        this(parentServer, districtId, port, listenPort, false);
    }

    public Booth(String parentServer, String districtId, int port, int listenPort, boolean isTesting) throws UnknownHostException, IOException, InterruptedException{
        parentIP = InetAddress.getByName(parentServer);
        parentPort = port;
        dummyData = false;
        cmdInProgress = new Object();
        this.districtId = districtId;
        exit = false;
        statusUpdateThread = null;

        _candidates = new ArrayBlockingQueue<Candidate[]>(10);
        _results = new ArrayBlockingQueue<BoothElectionResults>(10);
        _msgs = new ArrayBlockingQueue<String>(10);

		this.clientServer = new Comm(listenPort);
		clientServer.connectToParent(parentIP, parentPort);

        if(!isTesting) {
            window = new BoothUI(this, districtId);
            Thread.sleep(5000);
        }else{
            window = null;
        }
    }

    public void shutdown(){
        try {
            clientServer.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(statusUpdateThread != null) {
            try {
                window.exit();
            }catch(Exception e){}

            statusUpdateThread.interrupt();
        }

        System.out.println("Booth shutdown");
    }
    
    public boolean recv(){
    	Object msg = null;
        int i = 0;

        while(msg == null) {
            try {
                msg = clientServer.getMessageBlocking(10, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}

            if(i == 100)
                return false;

            i++;
        }
    	
    	try {
	    	if(msg instanceof Candidate[]){
                System.out.println("Got candidate list");
	    		_candidates.put((Candidate[])msg);
	    	}else if(msg instanceof BoothElectionResults){
                System.out.println("Got election results");
	    		_results.put((BoothElectionResults)msg);
	    	}else if(msg instanceof String){

                if(((String)msg).equals("end")){
                    System.out.println("Got terminate");
                    exit = true;
                    shutdown();
	    			return false;
	    		}

                System.out.println("Got string '" + msg + "'");

				_msgs.put((String)msg);
	    	}else{
	    		System.out.println("unknown type");
	    	}
    	} catch (InterruptedException e) {
			e.printStackTrace();
		}

        return true;
    }

    public void run(){
        System.out.println("Booth Started");

        if(window != null)
        window.start();

        statusUpdateThread = new Thread() {
            public void run() {
                while (true) {
                    if(window != null)
                        window.updateStats(getElectionStatus());
                    else
                        getElectionStatus();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
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
            
            if(recv()) {

                Candidate[] res = null;
                try {
                    res = _candidates.take();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return res;
            }else{
                return null;
            }
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

            synchronized(cmdInProgress) {
                try {
                    this.clientServer.sendMessageParent("status");
                } catch (IOException | InterruptedException e) {
                    // TODO Auto-generated catch block
//                System.out.println("INTERRUPTED");
                    e.printStackTrace();
                    return null;
                }

                recv();
            }

            try {
                res = _results.take();
            } catch (InterruptedException e) {
                return null;
            }

            return res;
        }
    }

    public boolean register(Voter p){
        String s = "";

        synchronized(cmdInProgress) {

            try {
                this.clientServer.sendMessageParent(p);
            } catch (IOException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }

            recv();
        }
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
        String s;

        synchronized(cmdInProgress) {
            Credential c = new Credential(voter.getName(), pin);

            try {
                this.clientServer.sendMessageParent(c);
            } catch (IOException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }

            recv();
        }
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
        }

        try {
            s = _msgs.take();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
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