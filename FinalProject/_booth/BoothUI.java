
package FinalProject._booth;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import FinalProject.masterserver.ElectionResults;
import FinalProject.persons.Candidate;
import FinalProject.persons.Voter;

import java.awt.*;
import java.util.Enumeration;

class BoothUI extends JPanel implements ActionListener{
	
	private Booth model;
	private ButtonGroup candidateGroup;
	private JPanel candidatePanel;
	private Candidate[] candidates;
	
	private JTextField txtFirstName;
	private JTextField txtLastName;
	
	private JPanel panel;
	private JPanel panel2;
	
	public BoothUI(Booth model){
		super();
		this.model = model;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		/*
		 * Welcome panel
		 * 
		 */
		
		panel = new JPanel();
		
		txtFirstName = new JTextField();
		txtFirstName.setText("First Name");
		txtFirstName.setColumns(10);
		
		txtLastName = new JTextField();
		txtLastName.setText("Last Name");
		txtLastName.setColumns(10);
		
		JButton btnRegister = new JButton("Register");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(25)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(txtLastName, Alignment.LEADING)
						.addComponent(txtFirstName, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
					.addContainerGap(20, Short.MAX_VALUE))
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap(164, Short.MAX_VALUE)
					.addComponent(btnRegister)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(25)
					.addComponent(txtFirstName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(txtLastName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
					.addComponent(btnRegister)
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(86)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 265, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(99, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(24)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(116, Short.MAX_VALUE))
		);
		setLayout(groupLayout);
		
		
		
		/*
		 * Voting panel
		 * 
		 */
		
		panel2 = new JPanel();
		GroupLayout groupLayout2 = new GroupLayout(this);
		groupLayout2.setHorizontalGroup(
			groupLayout2.createParallelGroup(Alignment.LEADING)
				.addComponent(panel2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
		);
		groupLayout2.setVerticalGroup(
			groupLayout2.createParallelGroup(Alignment.LEADING)
				.addComponent(panel2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
		);
		
		this.candidatePanel = new JPanel();
		
		JButton btnVote = new JButton("Vote");
		
		JTextPane textPane = new JTextPane();
		GroupLayout gl_panel2 = new GroupLayout(panel2);
		gl_panel2.setHorizontalGroup(
			gl_panel2.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel2.createSequentialGroup()
					.addContainerGap(15, Short.MAX_VALUE)
					.addComponent(textPane, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel2.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnVote)
						.addComponent(this.candidatePanel, GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE))
					.addGap(118))
		);
		gl_panel2.setVerticalGroup(
			gl_panel2.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel2.createSequentialGroup()
					.addContainerGap(66, Short.MAX_VALUE)
					.addGroup(gl_panel2.createParallelGroup(Alignment.TRAILING)
						.addComponent(textPane, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel2.createSequentialGroup()
							.addComponent(this.candidatePanel, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnVote)))
					.addGap(22))
		);
		
		this.candidatePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		ButtonGroup candidateGroup = new ButtonGroup();
		
		panel2.setLayout(gl_panel2);
		setLayout(groupLayout);
	}
	
	public void updateStats(ElectionResults r){
		
	}
	
	public void updateCandidateList(Candidate[] candidates){
		for(int i=0;i<candidates.length;i++){
			JRadioButton rdbtnCandidate;
			
			rdbtnCandidate = new JRadioButton(candidates[i].getName());
			this.candidatePanel.add(rdbtnCandidate);
			this.candidateGroup.add(rdbtnCandidate);
		}
		
		this.candidates = candidates;
	}
	
	public void showWelcome(){
		this.panel2.setVisible(false);
		this.panel.setVisible(true);
	}
	
	public void showVoting(){
		this.panel.setVisible(false);
		this.panel2.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "vote":
			Enumeration<AbstractButton> lst = this.candidateGroup.getElements();
			AbstractButton b;
			
			int i = 0;
			while(lst.hasMoreElements()){
				b = lst.nextElement();
				if(b.isSelected()){
					if(this.model.vote(this.candidates[i])){
						this.showWelcome();
					}else{
						// model error
					}
				}
				i++;
			}
			break;
		case "register":
			
			Voter v = new Voter(txtFirstName + " " + txtLastName, null);
			if(this.model.register(v)){
				this.showVoting();
			}else{
				// model error
			}
			
			break;
		}
	}
}