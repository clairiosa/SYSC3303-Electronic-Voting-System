/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	ElectionBarDemo.java
 *
 */


package FinalProject.masterserver;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

import FinalProject.persons.Candidate;


/**
 * This class is for the election pie chart    
 **/

public class ElectionBarDemo extends JFrame {

    private static final long serialVersionUID = 1L;

    static {
        ChartFactory.setChartTheme(new StandardChartTheme("JFree/Shadow",true));
    }

    /**
    * Creates a new demo instance.
    *
    * @param title  the frame title.
    */
    public ElectionBarDemo(String title,ConcurrentHashMap<String, Candidate> candidates) {
        super(title);
        CategoryDataset dataset = createDataset(candidates);
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(chartPanel);
    }

    /**
     * Returns a sample dataset.
     *
     * @return The dataset.
     */
    private static CategoryDataset createDataset(ConcurrentHashMap<String, Candidate> candidates) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
     	Enumeration<Candidate> it = candidates.elements();
     	int projected = 0; 
     	int totalVotes=0;
     	int size = candidates.size();
     	while(it.hasMoreElements()) {  //get the overall number of total votes 
     		Candidate c = (Candidate) it.nextElement();
     		totalVotes+=c.getVoteCount();
     	}
 		projected=totalVotes/size;  //get the projected number of votes for each candidate by taking the average 
     	Enumeration<Candidate> it1 = candidates.elements();
     	while(it1.hasMoreElements()) { //add values to the table 
     		Candidate c = (Candidate) it1.nextElement();
     		dataset.addValue(projected,"Projected", c.getName()+"("+c.getParty()+")");
            dataset.addValue(c.getVoteCount(), "Actual", c.getName()+"("+c.getParty()+")");
     	} 
        return dataset;
    }

    /**
     * Creates election data chart 
     *
     * @param dataset  the dataset.
110     *
     * @return The chart.
     */
    private static JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
            "Canadian Election: Voting Results", null /* x-axis label*/, 
                "Votes" /* y-axis label */, dataset);
        chart.addSubtitle(new TextTitle("Dathonian Core Technologies"));
        chart.setBackgroundPaint(Color.white);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        chart.getLegend().setFrame(BlockBorder.NONE);
        return chart;
    }
    
    
    /**
     * Starting point for the demonstration application.
     *
     * 
     */
    public static void main(String[] args) {
    	ConcurrentHashMap<String, Candidate> candidates=new ConcurrentHashMap<String,Candidate>();
    	Candidate c1=new Candidate("Jonathan Oommen", "Conservative");
    	Candidate c2=new Candidate("David Bews", "Liberal");
    	c1.setVoteCount(5000);
    	c2.setVoteCount(7000);
    	candidates.put(c1.getName(), c1);
    	candidates.put(c2.getName(), c2);
    	
        ElectionBarDemo demo = new ElectionBarDemo("2015 Canadian Election", candidates);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }



}
