package ck.ma.charts

/**
 * A result of a trendline calculation, abstract from chart and data
 */
class TrendResult{
    TrendLinePoint[] trendline
    double averageDiff, diffPercent
}