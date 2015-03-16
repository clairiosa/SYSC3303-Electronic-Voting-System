
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

class VoterBooth extends JPanel implements ActionListener {
	  // override the paintComponent method
	  // THE MAIN DEMO OF THIS EXAMPLE:
	
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Font f = new Font("Helvetica", Font.BOLD, 14);
    FontMetrics fm = g.getFontMetrics(f);

    // FontMetrics fim = g.getFontMetrics(fi);
    int cx = 75; int cy = 100;
    g.setFont(f);
    g.drawString("Hello, ", cx, cy);
    cx += fm.stringWidth("Hello, ");
    // g.setFont(fi);
    g.drawString("World!", cx, cy);


    JButton button = new JButton("Register");
    JButton button2 = new JButton("Vote!");

    add(button);
    add(button2);

    button.setLocation(240, 280);

    button.addActionListener(this);
    button2.addActionListener(this);
  }

  public void actionPerformed(ActionEvent e){
  	String com = e.getActionCommand();
  	System.out.println(com);
  }
} 
	
class MyFrame extends JFrame {
	public MyFrame() {
		setTitle("Voter Booth");
		setSize(300,200); // default size is 0,0
		setLocation(0,0); // default is 0,0 (top left corner)

		addWindowListener(new WindowAdapter() {
	  	public void windowClosing(WindowEvent e) {
			   System.exit(0);
		  	} //windowClosing
		} );

		Container contentPane = getContentPane();
		contentPane.add( new VoterBooth()); 
	} 

	public static void main(String[] args) {
		JFrame f = new MyFrame();
		f.show();
	}
}

class Terminator extends WindowAdapter {
  public void windowClosing(WindowEvent e) {
    System.exit(0); 
  }
}