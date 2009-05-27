package ck.ma

import ck.ma.data.yahoodata.YahooQuoteService
import ck.ma.charts.jfree.JFreeUtils
import ck.ma.algorithms.OldTrender
import ck.ma.charts.TrendLine
import ck.ma.publishing.WebPageBuilder
import ck.ma.algorithms.MovingAverage
import ck.ma.algorithms.MovingAverageJava
import ck.ma.algorithms.Trending
import ck.ma.publishing.TrendingEntry
import ck.ma.data.Ftse100Quotes
import ck.ma.publishing.EmaIndicatorEntry
import ck.ma.data.localdata.LocalFileData

/**
 *
 */
class MarketAnalysis {

  private artifactFolder
  private static final ROOT = "."
  private static final IMAGES_FOLDER="images"

  private timeFrameDays =200

  public MarketAnalysis(artifactFolder){
    this.artifactFolder = artifactFolder
    //this.imagesFolder = ROOT+"/images"

    new File(artifactFolder).mkdirs()
    new File(artifactFolder, IMAGES_FOLDER).mkdirs()
  }

  private quoteService = new YahooQuoteService();
  //private quoteService = new LocalFileData();
  private charting = new JFreeUtils();
  private trender = new OldTrender();

  private movingAve = new MovingAverage();
  private trending = new Trending();

  static void main(args) {
    def analysis = new MarketAnalysis("./generated")
    println "working"

    def symbols = ["BARC", "XTA", "HSBA"]
    //def symbols = new Ftse100Quotes().get()

    //analysis.publishRatios(symbols)
    analysis.publishEmaIndicators(symbols)
  }

  def publishCharts() {
    def analysis = new MarketAnalysis("./generated")

    def symbols = ["BARC", "XTA", "TW", "HSBA"]
    def charts = []

    symbols.each() {symbol ->
      charts << emaChart(symbol)
    }

    WebPageBuilder.writeCharts(charts, analysis.artifactFolder, "Market Info")
  }

  def makeChartsForSymbols(symbols, outputFolder, emaLengths){
    def charts =[:]

    symbols.each() {symbol ->
      charts[(symbol)] = drawEmaChart(symbol, outputFolder, emaLengths)
      System.out.println("MarketAnalysis.makeChartsForSymbols done "+symbol);
    }

    charts
  }

  def makeChartsForSymbolsOLD(trendingEntries, outputFolder){
    def charts =[:]

    trendingEntries.each() {entry ->
      charts[(entry.name)] = drawEmaChart(entry.name, outputFolder)
    }

    charts
  }

  def publishEmaIndicators(symbols) {
    def now = new Date()
    def emaLengths = [13, 26, 52]
    def entries = []
    def numberOfChartsToDraw = 10

    symbols.each(){symbol ->
      def candleData = quoteService.getQuotes(now.minus(timeFrameDays), now, symbol)
      def symbolEmas = [:]

      emaLengths.each(){emaLength ->
        def emaValues = movingAve.ema(candleData, emaLength)
        def movingAveData = trending.hardRightTrend(emaValues)
        symbolEmas[emaLength] = movingAveData
      }

      entries << new EmaIndicatorEntry(name: symbol, emaLengths:emaLengths, emas: symbolEmas)
    }


    WebPageBuilder.writeEmaIndicators(entries, emaLengths, "EMA indicators", artifactFolder,
            makeChartsForSymbols(symbols, artifactFolder +"/"+ IMAGES_FOLDER, emaLengths))
  }

  def publishRatios(symbols) {
    def now = new Date()
    def entries = []

    def days = 21
    def numberOfChartsToDraw = 2

    symbols.each(){symbol ->
      def candleData = quoteService.getQuotes(now.minus(timeFrameDays), now, symbol)

      //def fiveEma = movingAve.ema(candleData, 5);
      def thirteenEma = movingAve.ema(candleData, 13)

      def magnitudeRatio = trending.magnitudeRatio(thirteenEma.points, days);
      def purityRatio = trending.purityRatio(thirteenEma.points, days)

      TrendingEntry e = new TrendingEntry(
              magnitudeRatio: magnitudeRatio,
              purityRatio: purityRatio,
              name: symbol
      );

      entries << e
    }

    entries.sort{it.magnitudeRatio}
    entries = entries.reverse()
    WebPageBuilder.writeBestTrenders(entries, artifactFolder, "title", makeChartsForSymbolsOLD(entries[0..numberOfChartsToDraw]))
  }


  def drawEmaChart(symbol, outputFolder, emaLengths) {
    def now = new Date()

    def candleData = quoteService.getQuotes(now.minus(timeFrameDays), now, symbol)
    //def fiveEma = movingAve.ema(candleData, 5);
    //def thirteenEma = movingAve.ema(candleData, 13)

    def linesToPlot = emaLengths.collect {it -> movingAve.ema(candleData, it)}

//    def linesToPlot = [
//            //new TrendLine(points:ckTrendResult.trendline, title:"CK trendline"),
//            //new TrendLine(points:dumbTrendLine.trendline, title:"dumb trendline"),
//            //movingAve.sma(candleData, 4),
//            fiveEma,
//            //thirteenEma
//            //new TrendLine(points:est1Line.trendline, title:"5% higher"),
//            //new TrendLine(points:est2Line.trendline, title:"10% higher")
//    ]

    System.out.println("MarketAnalysis.drawEmaChart rendering...");

    def chart = charting.renderCandleChart(symbol, candleData, linesToPlot/*, 5, 13*/)
    def fileName = symbol + ".png"
    def file = outputFolder+"/" + fileName
    charting.writeToPNG(chart, file)
    fileName
  }

  def drawEmaChart(symbol, outputFolder) {
    def now = new Date()

    def candleData = quoteService.getQuotes(now.minus(timeFrameDays), now, symbol)
    def fiveEma = movingAve.ema(candleData, 5);
    def thirteenEma = movingAve.ema(candleData, 13)

    def trendingEntries = {}


    def linesToPlot = [
            //new TrendLine(points:ckTrendResult.trendline, title:"CK trendline"),
            //new TrendLine(points:dumbTrendLine.trendline, title:"dumb trendline"),
            //movingAve.sma(candleData, 4),
            fiveEma,
            //thirteenEma
            //new TrendLine(points:est1Line.trendline, title:"5% higher"),
            //new TrendLine(points:est2Line.trendline, title:"10% higher")
    ]

    System.out.println("MarketAnalysis.drawEmaChart rendering...");

    def chart = charting.renderCandleChart(symbol, candleData, linesToPlot/*, 5, 13*/)
    def fileName = symbol + ".png"
    def file = outputFolder+"/" + fileName
    charting.writeToPNG(chart, file)
    fileName
  }

  def emaChart(symbol, emas) {
    file = drawEmaChart(symbol)

    // new MarketInfo(chartFiles:[file], info:[(MarketInfo.TREND_DEVIATION_PRCENTKEY) : ckTrendResult.diffPercent])
    new MarketInfo(chartFiles: [file], info: [])
  }
}