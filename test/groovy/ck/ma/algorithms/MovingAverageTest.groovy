package ck.ma.algorithms
/**
 * Created by IntelliJ IDEA.
 * User: ck
 * Date: 17-May-2009
 * Time: 17:19:14
 * To change this template use File | Settings | File Templates.
 */
import groovy.util.GroovyTestCase
import ck.ma.data.localdata.LocalCandleData



public class MovingAverageTest extends GroovyTestCase{

  private movingAverage

  void setUp(){
     movingAverage = new MovingAverage()
  }

  private makeCandleData(def prices){
    //def LocalCandleData(date, open, close, high, low) {
   //def candleData = []
    prices.collect(){it -> new LocalCandleData(new Date(), it, it, 20, 20)} // only need close
  }

  /**
   * using values from http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:moving_averages
   */  
  void testEmaValues(){
    def prices = [64.75  , 63.79  , 63.73  , 63.73  , 63.55  , 63.19  , 63.91  , 63.85  , 62.95  , 63.37  , 61.33  , 61.51  , 61.87  , 60.25  , 59.35  , 59.95  , 58.93  , 57.68  , 58.82  , 58.87];
    def results = [63.682 , 63.254 , 62.937 , 62.743 , 62.290 , 61.755 , 61.427 , 60.973 , 60.374 , 60.092 , 59.870];

    def values = movingAverage.ema(makeCandleData(prices), 10).points
    assertLength (11, values)

    values.eachWithIndex(){value, i ->
      assetWithinRange(results[i], value.price, 0.0005d)
      println "comparing ${results[i]} and  $value.price"
    }
  }

  private assetWithinRange(expected, actual, range){
    assertTrue("was not in range, expected ${expected}, was ${actual}", Math.abs(expected - actual) < range)
  }
}