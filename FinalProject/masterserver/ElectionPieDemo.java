
/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	ElectionPieDemo.java
 *
 */

package FinalProject.masterserver;

import java.awt.Font;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RefineryUtilities;

import FinalProject.persons.Candidate;


/**
 * This class is for the election pie chart    
 **/

public class ElectionPieDemo extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
     * Default constructor.
     *
     * @param title  the frame title.
     */
    public ElectionPieDemo(String title,ConcurrentHashMap<String, Candidate> candidates) {
        super(title);
        setContentPane(createDemoPanel(candidates));
    }

    /**
     * Creates a sample dataset with the voting percentages of the candidates 
     * 
     * @return A sample dataset.
     */
    private static PieDataset createDataset(ConcurrentHashMap<String, Candidate> candidates) {
        DefaultPieDataset dataset = new DefaultPieDataset();
     	Enumeration<Candidate> it1 = candidates.elements();
     	while(it1.hasMoreElements()) {  //look through the candidates and create dataset 
     		Candidate c = (Candidate) it1.nextElement();
     		dataset.setValue(c.getName(), c.getVotingPercentage());
     	}
        return dataset;        
    }
    
    public void updateData(ConcurrentHashMap<String, Candidate> candidates)
    {
    	setContentPane(createDemoPanel(candidates));
    	 revalidate();
    }
    
    
    
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A chart.
     */
    private static JFreeChart createChart(PieDataset dataset) {
        
        JFreeChart chart = ChartFactory.createPieChart(
            "Canadian Election: Voting Results",  // chart title
            dataset,             // data
            true,               // include legend
            true,
            false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        return chart;
        
    }
    
    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     * 
     * @return A panel.
     */
    public static JPanel createDemoPanel(ConcurrentHashMap<String, Candidate> candidates) {
        JFreeChart chart = createChart(createDataset(candidates));
        return new ChartPanel(chart);
    }
    
    
    
    
    /**
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {
    	ConcurrentHashMap<String, Candidate> candidates=new ConcurrentHashMap<String,Candidate>();
    	Candidate c1=new Candidate("Jonathan Oommen", "Conservative");
    	Candidate c2=new Candidate("David Bews", "Liberal");
    	c1.setVoteCount(5000);
    	c2.setVoteCount(7000);
       	c1.setVotingPercentage(44.0);
       	c2.setVotingPercentage(56.0);
    	candidates.put(c1.getName(), c1);
    	candidates.put(c2.getName(), c2);
    	
        ElectionBarDemo demo = new ElectionBarDemo("2015 Canadian Election", candidates);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    	
    	
    	ElectionPieDemo demo1 = new ElectionPieDemo("2015 Election Percentages", candidates);
        demo1.pack();
        RefineryUtilities.centerFrameOnScreen(demo1);
        demo1.setVisible(true);

    }

}
