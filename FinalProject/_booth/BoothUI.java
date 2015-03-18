
package FinalProject._booth;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

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
	private JPanel pnlStatus;
	private JPanel pnlAction;

	private JTable tblResults;
	
	public BoothUI(Booth model){
		super();
		this.model = model;
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
		
		/*
		 * Welcome panel
		 *	
		 */
		
		pnlRegister = new JPanel();
		pnlVerify = new JPanel();
		pnlVote = new JPanel();
		pnlStatus = new JPanel();
		pnlAction = new JPanel();
		
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
		
		tblResults = new JTable(); tblResults.addColumn(new TableColumn()); tblResults.addColumn(new TableColumn());
		TableColumnModel headers = tblResults.getTableHeader().getColumnModel(); 
		headers.getColumn(0).setHeaderValue("Candidate"); headers.getColumn(1).setHeaderValue("Votes");
		tblResults.repaint();
		
		pnlStatus.add(tblResults);
	
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
		
		CardLayout c = new CardLayout();
		pnlAction.setLayout(c);
		
		pnlAction.add(pnlRegister, "register");
		pnlAction.add(pnlVerify, "verify");
		pnlAction.add(pnlVote, "vote");
		
		this.add(pnlAction);
//		this.add(tblResults);
		
		
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
		lblVerifyStatus.setText("");
		CardLayout layout = (CardLayout)(pnlAction.getLayout());
		layout.show(pnlAction, "verify");
	}
	
	public void showRegister(){
		lblRegisterStatus.setText("");
		CardLayout layout = (CardLayout)(pnlAction.getLayout());
		layout.show(pnlAction, "register");
	}
	
	public boolean showVoting(){
		
		if(this.candidates == null){
			(new Thread() {
			    public void run() {
			    	candidates = model.getCandidates();
			    	showVoting();
			    }
			}).start();
			
			return false;
		}
		
		lblVoteStatus.setText("");
		CardLayout layout = (CardLayout)(pnlAction.getLayout());
		layout.show(pnlAction, "vote");
		
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		
		switch(e.getActionCommand()){
		case "Vote":
			showRegister();
//			if(candidates.length == 0){
//				System.out.println("Load candidates");
//				return;
//			}
//			
//			Enumeration<AbstractButton> lst = this.candidateGroup.getElements();
//			AbstractButton b;
//			
//			int i = 0;
//			while(lst.hasMoreElements()){
//				b = lst.nextElement();
//				
//				if(b.isSelected()){
//					if(this.model.vote(candidates[i])){
//						showWelcome();
//						model.clearVoter();
//					}else{
//						// model error
//					}
//					return;
//				}
//				
//				i++;
//			}
//			
//			System.out.println("No one selected");
			
			break;
		case "Register":
			
			final Voter v = new Voter(txtFirstName.getText() + " " + txtLastName.getText(), null);
			lblRegisterStatus.setText("Searching...");
			
			(new Thread() {
			    public void run() {
			    	showVerify();
			    	return;
//			    	if(model.register(v)){
//						showVerify();
//					}else{
//						// model error
//						lblRegisterStatus.setText("Invalid!");
//					}
			    }
			}).start();
			
			break;
		case "Verify":
			lblVerifyStatus.setText("Verifying...");
			
			(new Thread() {
			    public void run() {
			    	
			    	if(!showVoting()){
			    		lblVerifyStatus.setText("Loading Candidates...");
			    	}
			    	
//			    	if(model.verify(v)){
//						showVoting();
//					}else{
//						// model error
//						lblVerifyStatus.setText("Incorrect!");
//					}
			    	
			    }
			}).start();
			
			break;
		}
	}
}