/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	framer1.java
 *
 */


package FinalProject.masterserver;

import java.awt.BorderLayout;
import java.awt.EventQueue;
//import java.io.File;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import FinalProject.persons.Candidate;



/**
 * This class represents the frame of the Graphical User Interface   
 **/

public class framer1 extends JFrame {
	
	private static final long serialVersionUID = -3468702018359690051L;
	private JPanel contentPane;
	private static ConcurrentHashMap<String, Candidate> candidates;
	private FancyDisplayWindow1 displayWindow;

	

	/**
	 * Create the frame.
	 * 
	 * @param cand - candidates information hashmap 
	 */
	public framer1(ConcurrentHashMap<String, Candidate> cand) {
		//create the Graphical User Interface window  
		candidates=cand;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1250, 850);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		//create the TabbedMainWidow 
		JTabbedPane TabbedMainWindow = new JTabbedPane(JTabbedPane.TOP);
		TabbedMainWindow.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		contentPane.add(TabbedMainWindow, BorderLayout.CENTER);

		//add main tab to the GUI 
		displayWindow = new FancyDisplayWindow1(candidates);
		TabbedMainWindow.addTab("Election Results", null, displayWindow, null);

	}
	
	//get the candidates information 
	public ConcurrentHashMap<String, Candidate> getCandidates(){
		return candidates;
	}
	
	//get the candidates information 
		public void setCandidates(ConcurrentHashMap<String, Candidate> cands){
			displayWindow.setCandidates(cands);
		}
	
	
	
	/**
	 * Main test method 
	 */
	public static void main(String[] args) {
		//create fake data 
		candidates=new ConcurrentHashMap<String,Candidate>();
    	Candidate c1=new Candidate("Jonathan Oommen", "Conservative");
    	Candidate c2=new Candidate("David Bews", "Liberal");
    	c1.addVotes(5000);
    	c2.addVotes(7000);
    	candidates.put(c1.getName(), c1);
    	candidates.put(c2.getName(), c2);
		
    	//thread for the Graphical User Interface 
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					framer1 frame = new framer1(candidates);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
     	while(true){
     		try {
				Thread.sleep(1000);
	     		Enumeration<Candidate> it1 = candidates.elements();
	     		while(it1.hasMoreElements()) {  //look through the candidates and create dataset 
	     			Candidate c = (Candidate) it1.nextElement();
	     			c.addVotes(1000);
	     		}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
     	}	
     }


}
