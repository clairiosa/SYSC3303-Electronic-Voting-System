
package FinalProject.masterserver;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.jfree.ui.RefineryUtilities;

import FinalProject.persons.Candidate;


//The view used for the setup process. the only view available at this point. Review the JFrame class for more details.
public class FancyDisplayWindow1 extends JPanel {
	private static final long serialVersionUID = -3468702018359690051L;

	private JLabel image_label;
	private ConcurrentHashMap<String, Candidate> candidates; 


	// Constructors for the setupView
	
	protected FancyDisplayWindow1(ConcurrentHashMap<String, Candidate> candidates) {
		this.candidates=candidates;
		this.initialize();
	}
	

	private void initialize() {
		Dimension size = new Dimension(175, 25);
		Dimension image_size = new Dimension(775,535);
		JTextArea field = new JTextArea(); 
		field.setEditable(false);
		Font font = new Font("Courier", Font.BOLD,20);
		field.setFont(font);
		field.setText("\nHello and welcome!! The election results have been provided to you by Dathonian Core Technologies. \n" +
					  "Dathonian Core Technologies is the independent, non-partisan agency responsible for conducting\n" +
					  "federal elections and referendums in Canada.\n\n\n");
		field.setBackground(null);
		String path = "./election-year-2015.png";
        File file = new File(path);
        BufferedImage image=null;
        try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}



		JButton pieGraphButton = new JButton("View Percentages Graph");
		pieGraphButton.setPreferredSize(size);
		pieGraphButton.setMaximumSize(size);
		pieGraphButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
		    	ElectionPieDemo demo1 = new ElectionPieDemo("2015 Election Percentages", candidates);
		        demo1.pack();
		        RefineryUtilities.centerFrameOnScreen(demo1);
		        demo1.setVisible(true);
			}
		});

		JButton barGraphButton = new JButton("View Votes Graph");
		barGraphButton.setPreferredSize(size);
		barGraphButton.setMaximumSize(size);
		barGraphButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
		        ElectionBarDemo demo = new ElectionBarDemo("2015 Canadian Election", candidates);
		        demo.pack();
		        RefineryUtilities.centerFrameOnScreen(demo);
		        demo.setVisible(true);
			}
		});

	
		image_label = new JLabel(new ImageIcon(image));
		image_label.setPreferredSize(image_size);
		add(field, BorderLayout.SOUTH);
		add(image_label, BorderLayout.CENTER);
		add(pieGraphButton, BorderLayout.WEST);
		add(barGraphButton, BorderLayout.WEST);
		this.setVisible(true);
	}





}
