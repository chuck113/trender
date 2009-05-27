package ck.ma.charts.jfree

import org.jfree.chart.ChartRenderingInfo
import org.jfree.chart.ChartUtilities
import org.jfree.chart.JFreeChart
import org.jfree.chart.axis.DateAxis
import org.jfree.chart.axis.NumberAxis
import org.jfree.chart.axis.SegmentedTimeline
import org.jfree.chart.entity.StandardEntityCollection
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.CandlestickRenderer
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.data.time.Day
import org.jfree.data.time.RegularTimePeriod
import org.jfree.data.time.TimeSeries
import org.jfree.data.time.TimeSeriesCollection
import org.jfree.data.xy.DefaultOHLCDataset
import org.jfree.data.xy.OHLCDataItem
import org.jfree.data.xy.XYDataset
import org.jfree.data.time.MovingAverage

class JFreeUtils {

    /**
     * creates a JFreeChart OHLCDataItem object from a single candle data object such as YahooCandleData
     *
     * OHLCDataItem item = new OHLCDataItem(date, open, high, low, close, volume);
     */
    def createOHLCDataItem(candlestickData) {
        new OHLCDataItem(candlestickData.date, candlestickData.open, candlestickData.high, candlestickData.low, candlestickData.close, 0.0d);
    }

    /**
     * Given a trendline made up of TrendLinePoint's, will create a JFreeChart @Link{TimeSeriesCollection} containing a
     * TimeSeries with points for each line of the trendline.
     */
    def createTimeSeriesFromTrendLine(trendline, name, intervalClass) {
        TimeSeries ts = new TimeSeries(name);

        trendline.each {i ->
            ts.add(RegularTimePeriod.createInstance(intervalClass, i.date, TimeZone.getDefault()), i.price)
        }

        new TimeSeriesCollection(ts);
    }

    /**
    * Takes candleData such as YahooCandleData  and converts it into a XYDataset object ready
     * for plotting
    */
    def makeXYDataSetFromCandleData(candleData, title){
        def convertedData = []

        candleData.each {i -> convertedData << createOHLCDataItem(i)}

        new DefaultOHLCDataset(title, convertedData as OHLCDataItem[]);
    }

    //PriceLineType
    def renderCandleChart(symbol, candleData, customLines, long...emas) {
       renderCandleChart(PriceLineType.CANDLE, symbol, candleData, customLines, emas)
    }

    /**
     * Draws and saves a jfreechart.
     *
     * @candleData a list of CandleData objects to be drawn.
     * @lines TrendLine a list of lists, each with time to price values for drawing more lines
     * @emas the ema lines that are to be drawn. Eventually replaces this with a builder object of
     * all the types of extra, jfreeChart generated lines that can be generated
     */
    def renderCandleChart(priceLineType, symbol, candleData, customLines, long...emas) {
       System.out.println("JFreeUtils.renderCandleChart drawing chart for "+symbol);

        XYDataset dataset = makeXYDataSetFromCandleData(candleData, symbol)

        DateAxis domainAxis = new DateAxis("Date");
        NumberAxis rangeAxis = new NumberAxis("Price");
        CandlestickRenderer renderer = new CandlestickRenderer();

        renderer.setSeriesPaint(0, java.awt.Color.BLACK);
        renderer.setDrawVolume(false);
        rangeAxis.setAutoRangeIncludesZero(false);

      // caused failure initially
        //domainAxis.setTimeline(SegmentedTimeline.newMondayThroughFridayTimeline());

        // fixes bug but graphs are ugly: http://www.jfree.org/phpBB2/viewtopic.php?f=3&t=26926&start=0
        domainAxis.setAutoTickUnitSelection(false);
        domainAxis.setTimeline(nonTradingDaysTimeLine(candleData))

        XYPlot plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);
        def linesCount = 1 // the amount of lines drawn on the chart, candles are teh 1st

        customLines.each(){line ->
            plot.setRenderer(linesCount, new XYLineAndShapeRenderer(true, false));
            def jfreePlots = createTimeSeriesFromTrendLine(line.points, line.title, Day.class)
            plot.setDataset(linesCount, jfreePlots)
            linesCount++;
        }

        emas.each(){ ema ->
            plot.setRenderer(linesCount, new XYLineAndShapeRenderer(true, false));
            println "ema is $ema"
            XYDataset mav = MovingAverage.createMovingAverage(dataset, ema+" day moving average", ema * 24 * 60 * 60 * 1000L, 0L)
            plot.setDataset(linesCount, mav)
            linesCount++;
        }

      System.out.println("JFreeUtils.renderCandleChart drawing chart for "+symbol);

        //candlePlot.setOrientation(PlotOrientation.VERTICAL);
        JFreeChart chart = new JFreeChart(symbol, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        //chart.setBackgroundPaint(java.awt.Color.white);
      System.out.println("JFreeUtils.renderCandleChart DONE drawing chart for "+symbol);

        chart
    }

    def writeToPNG(chart, filename){
        final ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
        ChartUtilities.saveChartAsPNG(new File(filename), chart, 600, 400, info);
    }

    /**
     * Given some candle data, creates a SegmentedTimeline for all the
     * days in the data - removed holidays etc.
     */
    def nonTradingDaysTimeLine(candleData){
        def firstDay = candleData[0].date
        def lastDay = candleData[-1].date

        Date dateIter = firstDay

        // get segments from midday
        SegmentedTimeline timeline = SegmentedTimeline.newMondayThroughFridayTimeline()
        def allGivenDates = []

        candleData.each{d ->
            allGivenDates << d.date
        }

        while(dateIter.before(lastDay)){
            if(!allGivenDates.contains(dateIter)){
                def midday = new Date(dateIter.getTime() + (1000 * 60 * 60 * 12))
                timeline.addException(midday)
            }

            dateIter = dateIter + 1
        }

        timeline
    }
}
