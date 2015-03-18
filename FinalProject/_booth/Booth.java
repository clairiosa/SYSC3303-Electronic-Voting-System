
package FinalProject._booth;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import FinalProject.BoothElectionResult;
import FinalProject.BoothElectionResults;
import FinalProject.Credential;
import FinalProject.communication.Comm;
import FinalProject.persons.Candidate;
import FinalProject.persons.Voter;

public class Booth extends Thread { 
	private Comm clientServer;
	private Voter voter;
	
	private InetAddress parentIP;
	private int parentPort;
	private Object cmdInProgress;
	private boolean dummyData;
	
	public Booth(String parentServer, int port) throws UnknownHostException{
		parentIP = InetAddress.getByName(parentServer);
		parentPort = port;
		dummyData = false;
		cmdInProgress = new Object();
	}
	
	public void connect(int listenPort) throws IOException, InterruptedException{
		clientServer = new Comm(listenPort);
		clientServer.connectToParent(parentIP, parentPort);
		
		Thread.sleep(1000);
	}
	
	public void run(){
		final BoothUI window = new BoothUI(this);
		window.start();
		
		(new Thread() {
		    public void run() {
				while(true){
					synchronized(cmdInProgress){
						window.updateStats(getElectionStatus());
					}
					
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
			
			Candidate[] res;
			try {
				res = (Candidate[]) this.clientServer.getMessageBlocking();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		return res;
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
			try {
				this.clientServer.sendMessageParent("status");
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
			BoothElectionResults res;
			try {
				res = (BoothElectionResults) this.clientServer.getMessageBlocking();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
			return res;
		}
	}
	
	public void parse(String textFile){
		
	}

	public boolean register(Voter p){
		String s;
		
		synchronized(cmdInProgress){
			try {
				this.clientServer.sendMessageParent(p);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
			
			try {
				s = (String) this.clientServer.getMessageBlocking();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
			this.voter = p;
		}
		
		return (s == "true");
	}
	
	public boolean verify(String pin){
		Credential c = new Credential(voter.getName(), pin);
		String s;
		
		synchronized(cmdInProgress){
			try {
				this.clientServer.sendMessageParent(c);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
			try {
				s = (String) this.clientServer.getMessageBlocking();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		return (s == "true");
	}

	public boolean vote(Candidate c){
		if(this.voter == null){
			System.out.println("Unknown State");
			return false;
		}
		String s;
		this.voter.setCandidate(c);
		
		synchronized(cmdInProgress){
			try {
				this.clientServer.sendMessageParent(this.voter);
			} catch (IOException | InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return false;
			}
			
			
			try {
				s = (String) this.clientServer.getMessageBlocking();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		
		this.voter = null;
		return (s == "true");
	}

	public static void main(String args[]) {
		
		if(args.length < 3){
			System.out.print("Booth serverIp serverPort listenPort");
			return;
		}
		
		Booth booth;
		try {
			booth = new Booth(args[0], Integer.parseInt(args[1]));
		}catch(NumberFormatException e){
			System.out.println("Invalid port number");
			return;
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Unknown host error");
			return;
		}
		
		try{
			booth.connect(Integer.parseInt(args[2]));
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("IO exception");
			return;
		}catch(InterruptedException e){
			e.printStackTrace();
			System.out.println("Interrupted IO exception");
			return;
		}
		
		booth.run();
		
	}
}