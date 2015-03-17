
package FinalProject.booth;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import FinalProject.Ballot;
import FinalProject.communication.Comm;
import FinalProject.masterserver.ElectionResults;
import FinalProject.persons.Person;

public class Booth extends Thread { 
	Comm clientServer;
	
	public Booth(){
		
	}
	
	public void connect() throws IOException, InterruptedException{
//		this.clientServer = new Comm(2101);
//		clientServer.connectToParent(InetAddress.getByName("127.0.0.1"), 2011);
//		Thread.sleep(1000);
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

	public boolean register(Person p){
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
		
		return (s == "true");
	}

	public boolean vote(Ballot b){
		
		try {
			this.clientServer.sendMessageParent(b);
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
		return (s == "true");
		
	}

	public static void main(String args[]) {
		
		Booth booth = new Booth();
		try{
			booth.connect();
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