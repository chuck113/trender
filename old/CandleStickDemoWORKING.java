package java; /**
 * Created by IntelliJ IDEA.
 * User: ck
 * Date: Apr 25, 2009
 * Time: 12:19:31 PM
 * To change this template use File | Settings | File Templates.
 */
import org.jfree.chart.*;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.xy.*;
import org.jfree.data.time.*;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.text.*;
import java.util.*;
import java.util.List;

public class CandleStickDemoWORKING /*extends JFrame*/ {
    public CandleStickDemoWORKING(String stockSymbol) {
        //super("java.CandlestickDemo");
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DateAxis    domainAxis       = new DateAxis("Date");
        NumberAxis  rangeAxis        = new NumberAxis("Price");
        CandlestickRenderer renderer = new CandlestickRenderer();
        XYDataset   dataset          = getDataSet(stockSymbol);

//
//        //Do some setting up, see the API Doc
        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setDrawVolume(false);
        rangeAxis.setAutoRangeIncludesZero(false);

        final XYSeries series1 = new XYSeries("Series 1");
    series1.add(22, 200.0);
    series1.add(22, 210.0);

        final TimeSeries s1 = new TimeSeries("Random Data");
        RegularTimePeriod t = new Day();
        s1.add(t, new Date().getTime());        


        DefaultOHLCDataset ds = null;
        TimeSeries ts = new TimeSeries("name");
        //ts.add(new RegularTimePeriod());

//        final TimeSeries mav = MovingAverage.createMovingAverage(
//            eur, "30 day moving average", 30, 30
//
//    XYSeriesCollection col = new XYSeriesCollection(series1);
//      col.addSeries(ds);

  // causes failure
//        domainAxis.setTimeline( SegmentedTimeline.newMondayThroughFridayTimeline() );

//
//        //Now create the chart and chart panel
//        JFreeChart chart = new JFreeChart(stockSymbol, null, mainPlot, false);
//        ChartPanel chartPanel = new ChartPanel(chart);
//        chartPanel.setPreferredSize(new Dimension(600, 300));



        XYPlot plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);

        plot.setOrientation(PlotOrientation.VERTICAL);

        JFreeChart chart = new JFreeChart("Bar Chart", JFreeChart.DEFAULT_TITLE_FONT, plot, true);

        chart.setBackgroundPaint(java.awt.Color.white);

		System.out.println("outputing chart");
		try{
			//ChartUtilities.saveChartAsPNG(new File("mychart.jpg"), chart, 650, 450); //width height


            final ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            final File file1 = new File("barchart100.png");
            ChartUtilities.saveChartAsPNG(file1, chart, 600, 400, info);

            //PrintWriter p = new PrintWriter(System.out);
            //ChartRenderingInfo r = new ChartRenderingInfo();

			//ChartUtilities.writeImageMap(p, "s", r, true);
		}catch(Exception e){e.printStackTrace();}
		System.out.println("done outputing chart");
        //this.add(chartPanel);
        //this.pack();
    }

    public static AbstractXYDataset getDataSet(String stockSymbol) {
        //This is the dataset we are going to create
        DefaultOHLCDataset result = null;
        //This is the data needed for the dataset
        OHLCDataItem[] data;

        //This is where we go get the data, replace with your own data source
        data = getData(stockSymbol);

        //Create a dataset, an Open, High, Low, Close dataset
        result = new DefaultOHLCDataset(stockSymbol, data);

        return result;
    }
    //This method uses yahoo finance to get the OHLC data
    public static OHLCDataItem[] getData(String stockSymbol) {
        List<OHLCDataItem> dataItems = new ArrayList<OHLCDataItem>();
        try {
            String strUrl= "http://ichart.finance.yahoo.com/table.csv?s="+stockSymbol+"&a=0&b=1&c=2008&d=3&e=30&f=2008&ignore=.csv";
            URL url = new URL(strUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            DateFormat df = new SimpleDateFormat("y-M-d");

            String inputLine;
            in.readLine();
			//System.out.println("reading...");
            while ((inputLine = in.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(inputLine, ",");

                Date date       = df.parse( st.nextToken() );
                double open     = Double.parseDouble( st.nextToken() );
                double high     = Double.parseDouble( st.nextToken() );
                double low      = Double.parseDouble( st.nextToken() );
                double close    = Double.parseDouble( st.nextToken() );
                double volume   = Double.parseDouble( st.nextToken() );
                double adjClose = Double.parseDouble( st.nextToken() );

                OHLCDataItem item = new OHLCDataItem(date, open, high, low, close, volume);
				System.out.println("adding price: " + high);
                dataItems.add(item);
            }
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //Data from Yahoo is from newest to oldest. Reverse so it is oldest to newest
        Collections.reverse(dataItems);

        //Convert the list into an array
        OHLCDataItem[] data = dataItems.toArray(new OHLCDataItem[dataItems.size()]);

        return data;
    }

    public static void main(String[] args) {
        new CandleStickDemoWORKING("BARC.L");//.setVisible(true);
    }
}
