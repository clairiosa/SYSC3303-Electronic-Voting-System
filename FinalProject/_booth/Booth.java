
package FinalProject._booth;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import FinalProject.communication.Comm;
import FinalProject.masterserver.ElectionResults;
import FinalProject.persons.Candidate;
import FinalProject.persons.Person;
import FinalProject.persons.Voter;

public class Booth extends Thread { 
	private Comm clientServer;
	private Voter voter;
	
	private InetAddress parentIP;
	private int parentPort;
	
	public Booth(String parentServer, int port) throws UnknownHostException{
		parentIP = InetAddress.getByName(parentServer);
		parentPort = port;
	}
	
	public void connect(int listenPort) throws IOException, InterruptedException{
		this.clientServer = new Comm(listenPort);
		clientServer.connectToParent(parentIP, parentPort);
		Thread.sleep(1000);
	}
	
	public void run(){
		BoothUI window = new BoothUI(this);
	}

	public ElectionResults getElectionStatus(){
		try {
			this.clientServer.sendMessageParent("status");
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		ElectionResults res;
		try {
			res = (ElectionResults) this.clientServer.getMessageBlocking();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return res;
	}

	public boolean register(Voter p){
		try {
			this.clientServer.sendMessageParent(p);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		String s;
		try {
			s = (String) this.clientServer.getMessageBlocking();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		this.voter = p;
		
		return (s == "true");
	}

	public boolean vote(Candidate c){
		
		this.voter.setCandidate(c);
		
		try {
			this.clientServer.sendMessageParent(this.voter);
		} catch (IOException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		
		String s;
		try {
			s = (String) this.clientServer.getMessageBlocking();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
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
			System.out.println("IO exception");
			return;
		}catch(InterruptedException e){
			System.out.println("Interrupted IO exception");
			return;
		}
		
		booth.run();
		
	}
}