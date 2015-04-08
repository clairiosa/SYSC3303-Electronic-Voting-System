/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	FancyDispayWindow1.java
 *
 */

package FinalProject.masterserver;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.jfree.ui.RefineryUtilities;

import FinalProject.persons.Candidate;

/**
 * This class represents the main window of the Graphical User Interface   
 **/


public class FancyDisplayWindow1 extends JPanel {
	private static final long serialVersionUID = -3468702018359690051L;

	private JLabel image_label;
	private ConcurrentHashMap<String, Candidate> candidates;  
	private JTextArea results;
	private ElectionBarDemo demo;
	private ElectionPieDemo demo1;
	private boolean piegraph; 
	private boolean bargraph; 


	// Constructors for the FacyDisplayWindow 
	protected FancyDisplayWindow1(ConcurrentHashMap<String, Candidate> candidates) {
		this.candidates=candidates;
		results = new JTextArea();
		this.initialize();
		piegraph=false; 
		bargraph=false;
	}
	
	//initialize the Graphical User Interface 
	private void initialize() {
		Dimension size = new Dimension(175, 25);
		Dimension image_size = new Dimension(640,480);
		JTextArea field = new JTextArea(); 
		field.setEditable(false);
		Font font = new Font("Courier", Font.BOLD,20);
		field.setFont(font);
		field.setText("\nHello and welcome!! The election results have been provided to you by Dathonian Core Technologies. \n" +
					  "Dathonian Core Technologies is the independent, non-partisan agency responsible for conducting\n" +
					  "federal elections and referendums in Canada.\n\n\n");
		field.setBackground(null);
		
		
		 
		results.setEditable(false);
		results.setFont(font);

		
		
		//get GUI backgroup photo 
		String path = "FinalProject/images/election-year-2015.png";
		//String path = "./election-year-2015.png";
        File file = new File(path);
        BufferedImage image=null;
        try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}


        //button to create a pie chart of the election data voting percentages 
		JButton pieGraphButton = new JButton("View Percentages Graph");
		pieGraphButton.setPreferredSize(size);
		pieGraphButton.setMaximumSize(size);
		pieGraphButton.addActionListener(new ActionListener() {
			//when the button is clicked 
			public void actionPerformed(ActionEvent evt) {
				demo1 = new ElectionPieDemo("2015 Election Percentages", candidates);
		        demo1.pack();
		        RefineryUtilities.centerFrameOnScreen(demo1);
		        demo1.setVisible(true);
		        piegraph=true;
			}
		});

		 //button to create a bar graph of the election data
		JButton barGraphButton = new JButton("View Votes Graph");
		barGraphButton.setPreferredSize(size);
		barGraphButton.setMaximumSize(size);
		barGraphButton.addActionListener(new ActionListener() {
			//when the button is clicked make the chart with the data 
			public void actionPerformed(ActionEvent evt) {
		        demo = new ElectionBarDemo("2015 Canadian Election", candidates);
		        demo.pack();
		        RefineryUtilities.centerFrameOnScreen(demo);
		        demo.setVisible(true);
		    	bargraph = true;
			}
		});

	
		image_label = new JLabel(new ImageIcon(image));
		image_label.setPreferredSize(image_size);
		
		//add components to the JFrame 
		add(field, BorderLayout.SOUTH);
		add(results, BorderLayout.SOUTH);
		add(image_label, BorderLayout.CENTER);
		add(pieGraphButton, BorderLayout.WEST);
		add(barGraphButton, BorderLayout.WEST);
		this.setVisible(true);
		
		new Thread(new Runnable() {
			public void run() {
				while(true){
					try {
						updateResults();
						Thread.sleep(500);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		
		new Thread(new Runnable() {
			public void run() {
				while(true){
					try {
						updateGraphs();
						Thread.sleep(2000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	
	}
	
	
	
	
	
	//get the candidates information 
	public ConcurrentHashMap<String, Candidate> getCandidates(){
		return candidates;
	}
	
	//get the candidates information 
		public void setCandidates(ConcurrentHashMap<String, Candidate> cands){
			candidates=cands;
		}
		
	public void updateResults(){
		String s=new String("2015 Election Results\n");
		Enumeration<Candidate> it1 = candidates.elements();
     	while(it1.hasMoreElements()) {  //look through the candidates and create dataset 
     		Candidate c = (Candidate) it1.nextElement();
     		s=s+c.getName()+"("+c.getParty()+")"+": "+c.getVoteCount()+ "("+c.getVotingPercentage()+")"    +"\n";
     	}
		results.setText(s);
		results.setBackground(null);
		this.updateUI();

	}

	public void updateGraphs(){
		if(bargraph && piegraph){
			demo.updateData(candidates);
			demo.invalidate();
			demo1.updateData(candidates);
			demo1.invalidate();

	    	

		}
		
	}





}
