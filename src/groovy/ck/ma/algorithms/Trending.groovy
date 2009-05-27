package ck.ma.algorithms

import ck.ma.publishing.EmaValues
import ck.ma.charts.TrendLinePoint

/**
 * Created by IntelliJ IDEA.
 * User: ck
 * Date: 13-May-2009
 * Time: 10:23:41
 * To change this template use File | Settings | File Templates.
 */

public class Trending {

  /**
   * Given an ema and a lenght, this will calculate the magnitude of the trend,
   * essentially the steepness of the curve over the given length, measured in
   * days
   */
  def magnitudeRatio(emaPoints, length){
     if(length > emaPoints.size()){
       return 0.0d
     }

    def relevantEmas = emaPoints[emaPoints.size() -length..emaPoints.size()-1]
    def movement = relevantEmas[-1].price - relevantEmas[0].price;

    movement / relevantEmas[-1].price
  }


   /**
    * Given a list of ema points, works out the magnitute of trend
    * starting at the hard right edge. If the hard right is trending
    * up then the result will be > 0, if the fill line is going up
    * then the result is 1.
    */
  def hardRightTrend(emaPoints){
    if(emaPoints.points.size() == 0)throw new IllegalArgumentException("emaPoints is zero");

    //println "Trending.hardRightTrend emaPoints size: ${emaPoints.points.size()}"
    /*TrendLinePoint[]*/ def points = emaPoints.points
    def lastPrice = points[-1].price
    def count = 0
    def deltaTotal = 0    

    //(emaPoints.size()-1..0).each{i->
    for(def i=(points.size()-2); i>=0; i--){
      def price = points[i].price
      //System.out.println("Trending.hardRightTrend comparing $lastPrice and $price");
       if(price < lastPrice){
         count++
         deltaTotal += (lastPrice - price)
       } else {
         break
       }
     }

    new EmaValues(trendLength: count, trendRise:(deltaTotal / lastPrice))
  }

  def purityRatio(emaPoints, length){
     if(length > emaPoints.size()){
       return 0.0d
     }


    // only need 9 points in the loop so miss out the first
    def relevantEmas = emaPoints[emaPoints.size() -length+1..emaPoints.size()-1]

    def absTotal = 0.0d // the toal movement, -ve or +ve
    def lastPrice = emaPoints[emaPoints.size() -length].price

    def movement = relevantEmas[-1].price - lastPrice

    relevantEmas.each(){point ->
      def diff = point.price - lastPrice
      absTotal += diff.abs();
      //println "last: $lastPrice, this: $point.price, diff: $diff, abs: ${diff.abs()}"
      lastPrice = point.price
    }

    //println "abstotal: $absTotal movement: $movement"
    movement / absTotal
  }
}