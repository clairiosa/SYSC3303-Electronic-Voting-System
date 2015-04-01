
package FinalProject.booth;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import FinalProject.BoothElectionResults;
import FinalProject.persons.Candidate;
import FinalProject.persons.Voter;

import java.awt.*;
import java.util.Enumeration;

class BoothUI extends JPanel implements ActionListener{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final Booth model;

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

    private JTable tblResults;

	private JFrame frame;

	private String districtId;

    public BoothUI(Booth model, String districtId){
        super();
        
        this.model = model;
        tblResults = null;
        this.districtId = districtId;
//		c.addLayoutComponent(this, "layoutComponent");
    }

    public void start(){
        pnlStatus = new JPanel();

        pnlStatus.setBackground(new Color(100, 100, 100));
        pnlStatus.setPreferredSize(new Dimension(300, 400));

        setPreferredSize(new Dimension(500, 400));
        setBackground(new Color(255, 255, 255));
        setLocation(300,0);

        frame = new JFrame("Voter Booth");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 400));

        GroupLayout layout = new GroupLayout(frame.getContentPane());

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addComponent(pnlStatus, 300, 300, 300)
                        .addComponent(this, 400, 400, GroupLayout.DEFAULT_SIZE)
        );

        layout.setVerticalGroup(
                layout.createParallelGroup()
                        .addComponent(pnlStatus, 400, 400, 400)
                        .addComponent(this)
        );

        frame.getContentPane().setLayout(layout);

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

        voteOptions = new JPanel(new GridLayout(0, 1));
        candidateGroup = new ButtonGroup();

        JButton btnRegister = new JButton("Register"); btnRegister.addActionListener(this);
        JButton btnVerify = new JButton("Verify"); btnVerify.addActionListener(this);
        JButton btnVote = new JButton("Vote"); btnVote.addActionListener(this);
        JLabel lbl = new JLabel(districtId);

        txtFirstName = new JTextField(); txtFirstName.setColumns(10);
        txtLastName = new JTextField(); txtLastName.setColumns(10);
        txtPin = new JTextField(); txtPin.setColumns(5);

        lblRegisterStatus = new JLabel();
        lblVerifyStatus = new JLabel();
        lblVoteStatus = new JLabel();


        tblResults = new JTable(0,2);
        tblResults.getColumnModel().getColumn(0).setHeaderValue("Candidate");
        tblResults.getColumnModel().getColumn(1).setHeaderValue("Votes");
        tblResults.getColumnModel().getColumn(0).setPreferredWidth(10);

        GroupLayout tblLayout = new GroupLayout(pnlStatus);

        tblLayout.setHorizontalGroup(
                tblLayout.createSequentialGroup()
                        .addComponent(tblResults));

        tblLayout.setVerticalGroup(
                tblLayout.createSequentialGroup()
                        .addComponent(tblResults));

        pnlStatus.setLayout(tblLayout);

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
        this.setLayout(c);

        add(pnlRegister, "register");
        add(pnlVerify, "verify");
        add(pnlVote, "vote");

        pnlStatus.add(tblResults);
    }

    public void updateStats(BoothElectionResults r){
        if(tblResults == null)
            return;
        if(r == null){
            System.out.println("STATS ERROR");
            return;
        }

        DefaultTableModel model = (DefaultTableModel)tblResults.getModel();

        while(model.getRowCount() > 0){
            model.removeRow(0);
        }

        String n; int c;
        for(int i=0; i < r.results.length;i++){
            n = r.results[i].candidate.getName();
            c = r.results[i].count;
            model.addRow(new Object[]{ n, c });
        }

        model.addRow(new Object[]{ "", ""});

        int t = r.totalVotes;
        String dt = r.generated.toString();

        model.addRow(new Object[]{ "Total", t });
        model.addRow(new Object[]{ "Updated", dt });

        tblResults.setModel(model);
        tblResults.repaint();
    }

    public void updateCandidateList(){
        JRadioButton rdbtnCandidate;

        for(int i=0;i<candidates.length;i++){
            rdbtnCandidate = new JRadioButton(candidates[i].getName());
            voteOptions.add(rdbtnCandidate);
            candidateGroup.add(rdbtnCandidate);
        }
    }

    public void showVerify(){
        lblVerifyStatus.setText("");

        CardLayout layout = (CardLayout)(this.getLayout());
        layout.show(this, "verify");
    }

    public void showRegister(){
        lblRegisterStatus.setText("");
        CardLayout layout = (CardLayout)(this.getLayout());
        layout.show(this, "register");
    }

    public boolean showVoting(){
        if(this.candidates == null){
            (new Thread() {
                public void run() {
                    candidates = model.getCandidates();
                    updateCandidateList();
                    showVoting();
                }
            }).start();

            return false;
        }

        lblVoteStatus.setText("");
        CardLayout layout = (CardLayout)(this.getLayout());
        layout.show(this, "vote");

        return true;
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

                Enumeration<AbstractButton> lst = candidateGroup.getElements();
                AbstractButton b;

                int i = 0;
                while(lst.hasMoreElements()){
                    b = lst.nextElement();

                    if(b.isSelected()){
                        if(this.model.vote(candidates[i])){
                            showRegister();
                            model.clearVoter();
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
                String name = txtFirstName.getText() + " " + txtLastName.getText();
                name = name.trim();
                final Voter v = new Voter(name, null);
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
            case "Verify":
                lblVerifyStatus.setText("Verifying...");

                (new Thread() {
                    public void run() {
                        if(model.verify(txtPin.getText())){
                            if(!showVoting()){
                                lblVerifyStatus.setText("Loading Candidates...");
                            }
                        }else{
//						// model error
                            lblVerifyStatus.setText("Incorrect!");
                        }
                    }
                }).start();

                break;
        }
    }

	public void exit() {
		frame.dispose();
	}
}