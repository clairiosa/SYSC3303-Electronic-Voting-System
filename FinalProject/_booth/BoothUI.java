
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
	
	private JLabel lblRegisterStatus;
	private JLabel lblVerifyStatus;
	private JLabel lblVoteStatus;
	
	private JTextField txtFirstName;
	private JTextField txtLastName;
	private JTextField txtPin;

	private JPanel pnlRegister;
	private JPanel pnlVerify;
	private JPanel pnlVote;
	
	private JPanel voteOptions;
	
	public BoothUI(Booth model){
		super();
		this.model = model;
		
		CardLayout c = new CardLayout();
		this.setLayout(c);
//		c.addLayoutComponent(this, "layoutComponent");
	}
	
	public void start(){
		JFrame frame = new JFrame("Voter Booth");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 400));

        frame.getContentPane().add(this);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		CardLayout layout = (CardLayout)(this.getLayout());
		
		/*
		 * Welcome panel
		 *	
		 */
		
		pnlRegister = new JPanel();
		pnlVerify = new JPanel();
		pnlVote = new JPanel();
		
		voteOptions = new JPanel(new GridLayout(0, 1));
		
		JButton btnRegister = new JButton("Register"); btnRegister.addActionListener(this);
		JButton btnVerify = new JButton("Verify"); btnVerify.addActionListener(this);
		JButton btnVote = new JButton("Vote"); btnVote.addActionListener(this);
		
		txtFirstName = new JTextField(); txtFirstName.setColumns(10);
		txtLastName = new JTextField(); txtLastName.setColumns(10);
		txtPin = new JTextField(); txtPin.setColumns(5);
		
		lblRegisterStatus = new JLabel();
		lblVerifyStatus = new JLabel();
		lblVoteStatus = new JLabel();
	
		pnlRegister.add(lblRegisterStatus);
		pnlRegister.add(txtFirstName);
		pnlRegister.add(txtLastName);
		pnlRegister.add(btnRegister);
		
		pnlVerify.add(lblVerifyStatus);
		pnlVerify.add(txtPin);
		pnlVerify.add(btnVerify);
		
		
		pnlVote.add(lblVoteStatus);
		pnlVote.add(voteOptions);
		pnlVote.add(btnVote);
		
		this.add(pnlRegister, "register");
		this.add(pnlVerify, "verify");
		this.add(pnlVote, "vote");
		
		
//		
//		txtFirstName = new JTextField();
//		txtFirstName.setColumns(10);
//		
//		txtLastName = new JTextField();
//		txtLastName.setColumns(10);
//		
//		JButton btnRegister = new JButton("Register");
//		btnRegister.addActionListener(this);
//		
//		JLabel lblFirstName = new JLabel("First Name");
//		
//		JTextPane textPane = new JTextPane();
//		textPane.setEditable(false);
//		textPane.setText("hello world");
//		
//		JLabel lblLastName = new JLabel("Last Name");
//		GroupLayout gl_panel = new GroupLayout(panel);
//		gl_panel.setHorizontalGroup(
//			gl_panel.createSequentialGroup()
//				.addComponent(textPane)
//				.addGap(10)
//				.addGroup(
//					gl_panel.createParallelGroup(Alignment.TRAILING)
//						.addGroup(gl_panel.createSequentialGroup()
//							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
//								.addGroup(gl_panel.createSequentialGroup()
//									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
//										.addComponent(lblLastName)
//										.addComponent(lblFirstName)
//										.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
//											.addComponent(txtLastName, Alignment.LEADING)
//											.addComponent(txtFirstName, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE)))
//									.addGap(20))
//								.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
//									.addComponent(btnRegister)
//									.addContainerGap()))))
//				.addContainerGap()
//		);
//		gl_panel.setVerticalGroup(
//			gl_panel.createParallelGroup(Alignment.LEADING)
//				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
//					.addGroup(gl_panel.createSequentialGroup()
//						.addGap(18)
//						.addComponent(lblFirstName)
//						.addGap(3)
//						.addComponent(txtFirstName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//						.addGap(4)
//						.addComponent(lblLastName)
//						.addPreferredGap(ComponentPlacement.RELATED)
//						.addComponent(txtLastName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//						.addPreferredGap(ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
//						.addComponent(btnRegister)
//						.addContainerGap())
//					.addGroup(gl_panel.createSequentialGroup()
//							.addGap(18)
//							.addComponent(textPane, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
//							.addContainerGap()))
//		);
//		
//		panel.setLayout(gl_panel);
		
//		GroupLayout groupLayout = new GroupLayout(this);
//		groupLayout.setHorizontalGroup(
//			groupLayout.createParallelGroup(Alignment.LEADING)
//				.addGroup(groupLayout.createSequentialGroup()
//					.addGap(268)
//					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 265, GroupLayout.PREFERRED_SIZE)
//					.addContainerGap(267, Short.MAX_VALUE))
//		);
//		groupLayout.setVerticalGroup(
//			groupLayout.createParallelGroup(Alignment.LEADING)
//				.addGroup(groupLayout.createSequentialGroup()
//					.addGap(97)
//					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 183, GroupLayout.PREFERRED_SIZE)
//					.addContainerGap(120, Short.MAX_VALUE))
//		);
//		panel.setLayout(groupLayout);
		
		
//		panel.setVisible(true);
		
		
		/*
		 * Voting panel
		 * 
		 */
		
//		GroupLayout groupLayout2 = new GroupLayout(this);
//		groupLayout2.setHorizontalGroup(
//			groupLayout2.createParallelGroup(Alignment.LEADING)
//				.addComponent(panel2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
//		);
//		groupLayout2.setVerticalGroup(
//			groupLayout2.createParallelGroup(Alignment.LEADING)
//				.addComponent(panel2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
//		);
//		
//		candidatePanel = new JPanel();
//		panel2.add(candidatePanel);
//		JButton btnVote = new JButton("Vote");
//		btnVote.addActionListener(this);
//		
//		GroupLayout gl_panel2 = new GroupLayout(panel2);
//		gl_panel2.setHorizontalGroup(
//			gl_panel2.createParallelGroup(Alignment.LEADING)
//				.addGroup(Alignment.TRAILING, gl_panel2.createSequentialGroup()
//					.addContainerGap(15, Short.MAX_VALUE)
//					.addGroup(gl_panel2.createParallelGroup(Alignment.TRAILING)
//						.addComponent(btnVote)
//						.addComponent(candidatePanel, GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE))
//					.addGap(118))
//		);
//		gl_panel2.setVerticalGroup(
//			gl_panel2.createParallelGroup(Alignment.LEADING)
//				.addGroup(Alignment.TRAILING, gl_panel2.createSequentialGroup()
//					.addContainerGap(66, Short.MAX_VALUE)
//					.addGroup(gl_panel2.createParallelGroup(Alignment.TRAILING)
//						.addGroup(gl_panel2.createSequentialGroup()
//							.addComponent(candidatePanel, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE)
//							.addPreferredGap(ComponentPlacement.RELATED)
//							.addComponent(btnVote)))
//					.addGap(22))
//		);
//		
//		candidateGroup = new ButtonGroup();
//		candidatePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
//		
//		panel2.setLayout(gl_panel2);
//		panel2.setLayout(groupLayout2);
		
	}
	
	public void updateStats(ElectionResults r){
		
	}
	
	public void updateCandidateList(Candidate[] candidates){
		JRadioButton rdbtnCandidate;
		
		//Put the radio buttons in a column in a panel.
		JPanel radioPanel = new JPanel(new GridLayout(0, 1));
		
		for(int i=0;i<candidates.length;i++){	
			rdbtnCandidate = new JRadioButton(candidates[i].getName());
			radioPanel.add(rdbtnCandidate);
			candidateGroup.add(rdbtnCandidate);
		}
		
		candidatePanel.add(radioPanel, BorderLayout.LINE_START);
		candidatePanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		System.out.println("added candidates");
		
		this.candidates = candidates;
	}
	
	public void showVerify(){
		CardLayout layout = (CardLayout)(this.getLayout());
		layout.show(this, "verify");
	}
	
	public void showWelcome(){
		CardLayout layout = (CardLayout)(this.getLayout());
		layout.show(this, "welcome");
	}
	
	public void showVoting(){
		CardLayout layout = (CardLayout)(this.getLayout());
		layout.show(this, "vote");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		
		switch(e.getActionCommand()){
		case "Vote":
			if(candidates.length == 0){
				System.out.println("Load candidates");
				return;
			}
			
			Enumeration<AbstractButton> lst = this.candidateGroup.getElements();
			AbstractButton b;
			
			int i = 0;
			while(lst.hasMoreElements()){
				b = lst.nextElement();
				if(b.isSelected()){
					if(this.model.vote(candidates[i])){
						this.showWelcome();
					}else{
						// model error
					}
					return;
				}
				i++;
			}
			
			System.out.println("No one selected");
			
			break;
		case "Register":
			
			final Voter v = new Voter(txtFirstName.getText() + " " + txtLastName.getText(), null);
			lblRegisterStatus.setText("Searching...");
			
			(new Thread() {
			    public void run() {
			    	if(model.register(v)){
						showVerify();
					}else{
						// model error
						lblRegisterStatus.setText("Invalid!");
					}
			    }
			}).start();
			
			break;
		}
	}
}