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




/**
063 * A simple demonstration application showing how to create a bar chart.
064 */
public class ElectionBarDemo extends JFrame {

    private static final long serialVersionUID = 1L;

    static {
        // set a theme using the new shadow generator feature available in
        // 1.0.14 - for backwards compatibility it is not enabled by default
        ChartFactory.setChartTheme(new StandardChartTheme("JFree/Shadow",
                true));
    }

    /**
077     * Creates a new demo instance.
078     *
079     * @param title  the frame title.
080     */
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
     	while(it.hasMoreElements()) {
     		Candidate c = (Candidate) it.nextElement();
     		totalVotes+=c.getVoteCount();
     	}
 		projected=totalVotes/size;
 		//projected=Candidate.totalVotes/size;
     	Enumeration<Candidate> it1 = candidates.elements();
     	while(it1.hasMoreElements()) {
     		Candidate c = (Candidate) it1.nextElement();
     		dataset.addValue(projected,"Projected", c.getName()+"("+c.getParty()+")");
            dataset.addValue(c.getVoteCount(), "Actual", c.getName()+"("+c.getParty()+")");
     	}
        return dataset;
    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
110     *
     * @return The chart.
     */
    private static JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
            "Canadian Election: Voting Results", null /* x-axis label*/, 
                "Votes" /* y-axis label */, dataset);
        chart.addSubtitle(new TextTitle("Subtitle"));
        chart.setBackgroundPaint(Color.white);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        // ******************************************************************
        //  More than 150 demo applications are included with the JFreeChart
        //  Developer Guide...for more information, see:
        //
        //  >   http://www.object-refinery.com/jfreechart/guide.html
        //
        // ******************************************************************

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
     * @param args  ignored.
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
