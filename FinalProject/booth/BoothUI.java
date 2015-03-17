
package FinalProject.booth;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.*;

class BoothUI extends JPanel implements ActionListener{
	
	private Booth model;
	
	public BoothUI(Booth model){
		super();
		this.model = model;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
		);
		
		
		
		JPanel candidateList = new JPanel();
		
		JButton btnVote = new JButton("Vote");
		
		JTextPane textPane = new JTextPane();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap(15, Short.MAX_VALUE)
					.addComponent(textPane, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnVote)
						.addComponent(candidateList, GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE))
					.addGap(118))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap(66, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(textPane, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(candidateList, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnVote)))
					.addGap(22))
		);
		candidateList.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		
		
		
		ButtonGroup candidateGroup = new ButtonGroup();
		JRadioButton rdbtnCandidate;
		
//		for(int i=0;i<candidates.length;i++){
//			rdbtnCandidate = new JRadioButton(candidate.firstName + " " + candidate.lastName);
//			candidateList.add(rdbtnCandidate);
//			candidateGroup.add(rdbtnCandidate);
//		}
		
		panel.setLayout(gl_panel);
		setLayout(groupLayout);
	}
	
	public void showWelcome(){
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.print(e);
	}
}