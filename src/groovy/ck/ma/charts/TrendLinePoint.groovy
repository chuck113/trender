package ck.ma.charts


/**
 * A single point on a trendline
 */
class TrendLinePoint {
  Date date
  double price

  public String toString ( ) {
    return "TrendLinePoint{" +
    "date=" + date +
    ", price=" + price +
    '}' ;
  }
}