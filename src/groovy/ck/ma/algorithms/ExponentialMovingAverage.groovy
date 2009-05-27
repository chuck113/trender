package ck.ma.algorithms

import java.math.RoundingMode
import ck.ma.charts.TrendLinePoint
import ck.ma.charts.TrendLine
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: ck
 * Date: May 9, 2009
 * Time: 5:29:02 PM
 * To change this template use File | Settings | File Templates.
 */

public class MovingAverage {

  private static final LOG = Logger.getLogger(MovingAverage.class)

  static void main(args) {
   // def prices = [18, 19, 21, 22, 11, 12, 13]
    //def result2 = new MovingAverage().simpleMAJava(prices, 3);
    //println result2

    //def result = new MovingAverage().simpleMA(prices, 3)
    //println result
    //assert result.size() == 4

    def prices = [64.75  , 63.79  , 63.73  , 63.73  , 63.55  , 63.19  , 63.91  , 63.85  , 62.95  , 63.37  , 61.33  , 61.51  , 61.87  , 60.25  , 59.35  , 59.95  , 58.93  , 57.68  , 58.82  , 58.87];

    def result = new MovingAverage().myEmaEasy(prices, 10)
    println result

  }


  def sma(candleData, length){
    if (length >= candleData.size) {
      LOG.warn("length was greater than supplid chart data")
       println "length was greater than supplid chart data"
      []
    }

    def resultSize = candleData.size - length + 1
    def result = []

    for (def i = 0; i < resultSize; i++) {
      def ma = 0.0d;
      def newArray = candleData[i..i + length - 1]

      newArray.each(){constituent -> ma += constituent.close}
      result << new TrendLinePoint(date:newArray[-1].date, price:(ma / length))
    }

    //System.out.println("MovingAverage.sma result is: ${result}");
    new TrendLine(points:result, title:"Simple MA of " +length)
  }

  /** returns a list of TrendLinePoints */
  def ema(candleData, length) {
    if (length >= candleData.size) {
      LOG.warn("length was greater than supplid chart data")
      return []
    }

    def resultSize = candleData.size - length + 1
    def result = []

    //def debug = sma(candleData, length)
    //System.out.println("MovingAverage.ema line: ${debug}")
    def smaTrendLine = sma(candleData, length).points
    //System.out.println("MovingAverage.ema line: ${smaTrendLine}");
    def previousEma = smaTrendLine[0].price
    def k = 2d / (length + 1)

    //System.out.println("MovingAverage.ema smaTrendLine[0]: " + smaTrendLine[0]);
    result << new TrendLinePoint(date:smaTrendLine[0].date, price:previousEma) 

    for (int i = 0; i < resultSize - 1; i++) {
      int offset = i + length;
      def data = candleData.get(offset)
      def price = data.close;
      //println "price: $price"

      //EMA(current) = ( (Price(current) - EMA(prev) ) x Multiplier) + EMA(prev)
      def ema = ((price - previousEma) * k) + previousEma;

      previousEma = ema;
      result << new TrendLinePoint(date:data.date, price:ema) 
    }

    println "ExponentialMovingAverate.ema length: $length, size: ${candleData.size}, result size: ${result.size}, last price: ${result[-1].price}"

    ///result
    new TrendLine(points:result, title:"EMA of " +length)
  }

  def myEmaEasy(prices, length) {
    if (length >= prices.size) { return [] }

    def resultSize = prices.size - length + 1;
    def result = []
    def previousEma = simpleMA(prices, length)[0]
    def k = 2d / (length + 1)

    result << previousEma

    for (int i = 0; i < resultSize - 1; i++) {
      int offset = i + length;
      def price = prices.get(offset);

      //EMA(current) = ( (Price(current) - EMA(prev) ) x Multiplier) + EMA(prev)
      def ema = ((price - previousEma) * k) + previousEma;

      previousEma = ema;
      result << ema
    }

    result
  }

//  public List<BigDecimal> ema(List<BigDecimal> prices, int length) {
//    if (length >= prices.size()) {
//      return Collections.emptyList();
//    }
//
//    int resultSize = prices.size() - length + 1;
//    List<BigDecimal> result = new ArrayList<BigDecimal>(resultSize);
//    BigDecimal k = new BigDecimal(2.0d).divide(new BigDecimal(length + 1), 3, RoundingMode.HALF_UP);
//
//    // optimize by using subset
//    List<BigDecimal> smas = simpleMA(prices, length);
//    // System.out.println("MovingAverageJava.ema last sma is: " + smas.get(0));
//    BigDecimal previousDayEma = smas.get(0);
//    result.add(previousDayEma);
//
//    for (int i = 0; i < resultSize - 1; i++) {
//      int offset = i + length;
//      BigDecimal price = prices.get(offset);
//
//      BigDecimal ema = price.subtract(previousDayEma).multiply(k).add(previousDayEma);
//      //System.out.println("MovingAverageJava.ema  number"+i+" "+FORMAT.format(price.doubleValue()) +" | "
//      //        +FORMAT.format(previousDayEma.doubleValue())+" | " +FORMAT.format(ema.doubleValue()));
//
//      previousDayEma = ema;
//      result.add(ema);
//    }
//
//    return result;
//  }

  public List<BigDecimal> simpleMAJava(List<BigDecimal> prices, int length) {
    if (length >= prices.size()) {
      return Collections.emptyList();
    }

    int resultSize = prices.size() - length + 1;
    List<BigDecimal> result = new ArrayList<BigDecimal>(resultSize);

    for (int i = 0; i < resultSize; i++) {
      BigDecimal ma = new BigDecimal(0.0d);
      List<BigDecimal> newArray = prices.subList(i, i + length); //prices[i..i+length-1]

      for (BigDecimal d: newArray) {
        ma = ma.add(d);
      }

      result.add(ma.divide(new BigDecimal(length), 3, RoundingMode.HALF_UP));
    }

    return result;
  }

  def simpleMA(prices, length) {
    if (length >= prices.size) { [] }

    def resultSize = prices.size - length + 1
    def result = []

    for (def i = 0; i < resultSize; i++) {
      def ma = 0.0d;
      def newArray = prices[i..i + length - 1]

      newArray.each {constiuent -> ma += constiuent}
      result << (ma / length)
    }

    result
  }

}