import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
//import java.io.File;
import java.util.concurrent.ConcurrentHashMap;



//***setImage() in ModelSuper only lets you set the image once.***//

public class framer1 extends JFrame {
	
	private static final long serialVersionUID = -3468702018359690051L;
	private JPanel contentPane;
	private static ConcurrentHashMap<String, Candidate> candidates;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		candidates=new ConcurrentHashMap<String,Candidate>();
    	Candidate c1=new Candidate("Jonathan Oommen", "Conservative");
    	Candidate c2=new Candidate("David Bews", "Liberal");
    	c1.setVoteCount(5000);
    	c2.setVoteCount(7000);
       	c1.setVotingPercentage(44.0);
       	c2.setVotingPercentage(56.0);
    	candidates.put(c1.getName(), c1);
    	candidates.put(c2.getName(), c2);
		
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
	}

	/**
	 * Create the frame.
	 */
	public framer1(ConcurrentHashMap<String, Candidate> cand) {
		candidates=cand;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1250, 850);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane TabbedMainWindow = new JTabbedPane(JTabbedPane.TOP);
		TabbedMainWindow.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		contentPane.add(TabbedMainWindow, BorderLayout.CENTER);

		FancyDisplayWindow1 displayWindow = new FancyDisplayWindow1(candidates);
		TabbedMainWindow.addTab("Election Results", null, displayWindow, null);




	}
	

	public ConcurrentHashMap<String, Candidate> getCandidates(){
		return candidates;
	}


}
