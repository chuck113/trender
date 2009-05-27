package java;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.xy.XYDataset;

/**
 * A demo showing how to create an HTML image map for a bar chart.
 */
public class ImageMapDemo7 {

    /**
     * Default constructor.
     */
    public ImageMapDemo7() {
        super();
    }

    /**
     * Starting point for the demo.
     *
     * @param args ignored.
     */
    public static void main(final String[] args) {

        JFreeChart chart = null;
        XYDataset dataset = CandleStickDemoWORKING.getDataSet("BARC.L");

        DateAxis domainAxis = new DateAxis("Date");
        NumberAxis rangeAxis = new NumberAxis("Price");
        CandlestickRenderer renderer = new CandlestickRenderer();

        XYPlot plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);

        plot.setOrientation(PlotOrientation.VERTICAL);
        chart = new JFreeChart("Bar Chart", JFreeChart.DEFAULT_TITLE_FONT, plot, true);

        chart.setBackgroundPaint(java.awt.Color.white);

        // save it to an image
        try {
            final ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            final File file1 = new File("barchart100.png");
            ChartUtilities.saveChartAsPNG(file1, chart, 600, 400, info);

            // write an HTML page incorporating the image with an image map
            final File file2 = new File("barchart100.html");
            final OutputStream out = new BufferedOutputStream(new FileOutputStream(file2));
            final PrintWriter writer = new PrintWriter(out);
            writer.println("<HTML>");
            writer.println("<HEAD><TITLE>JFreeChart Image Map Demo</TITLE></HEAD>");
            writer.println("<BODY>");
//            ChartUtilities.writeImageMap(writer, "chart", info);
            writer.println("<IMG SRC=\"barchart100.png\" "
                    + "WIDTH=\"600\" HEIGHT=\"400\" BORDER=\"0\" USEMAP=\"#chart\">");
            writer.println("</BODY>");
            writer.println("</HTML>");
            writer.close();

        }
        catch (IOException e) {
            System.out.println(e.toString());
        }

    }

}