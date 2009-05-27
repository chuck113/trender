package ck.ma.charts


class TrendLine{
    TrendLinePoint[] points    // was refactored from trendLine 
    String title

    public String toString ( ) {
      return "TrendLine{" +
      "points=" + ( points == null ? null : Arrays . asList ( points ) ) +
      ", title='" + title + '\'' +
      '}' ;
  }

}