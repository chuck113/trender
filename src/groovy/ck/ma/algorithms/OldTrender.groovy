package ck.ma.algorithms

import ck.ma.charts.TrendResult
import ck.ma.charts.TrendLinePoint


class OldTrender {

    private def log(msg) {
        println "LOG $msg"
    }

    def createDumbTrendLine(candleData, first, last){
        if(candleData == null || candleData.size == 0){
            return new TrendResult(trendline: [], averageDiff: 0, diffPercent: 0)

        }
        if (candleData[0].close > candleData[-1].close) {
            log("ending")
            return;
        }


        def difference = last - first
        def size = candleData.size
        def estRiseEachTick = (last - first) / size

        // the total difference between the anticipated trend
        def diffCount = 0
        def datePriceResult = []
        // the price before the current iteration, for use in the loop
        def p = candleData[0].close

        (1..(size - 1)).each {i ->
            def estimate = first + estRiseEachTick + (estRiseEachTick * i)
            def diff = candleData[i].close - estimate
            def diffPct = (diff / difference) * 100
            diffCount += Math.abs(diff)
            //log "it $i $p, est : $estimate, diff: $diff, diffPct: $diffPct"
            //log "${candleData[i].close} > $p"

            datePriceResult << new TrendLinePoint(date: candleData[i].date, price: estimate)

            p = candleData[i].close
        }

        def aveDifference = diffCount / size

        new TrendResult(trendline: datePriceResult, averageDiff: aveDifference, diffPercent: ((aveDifference / difference) * 100))
    }

    def createDumbTrendLine(candleData){
        def first = candleData[0].close
        def last = candleData[-1].close

        createDumbTrendLine(candleData, first, last)
    }

    def createTrendEstimate(candleData, startPoint, endPoint) {
        if(candleData == null || candleData.size == 0){
            return new TrendResult(trendline: [], averageDiff: 0, diffPercent: 0)

        }
        if (candleData[0].close > candleData[-1].close) {
            log("ending")
            return;
        }

        def first = startPoint
        def last = endPoint
        def difference = last - first
        def size = candleData.size
        def estRiseEachTick = (last - first) / size

        // the total difference between the anticipated trend
        def diffCount = 0
        def datePriceResult = []
        // the price before the current iteration, for use in the loop
        def p = candleData[0].close

        (1..(size - 1)).each {i ->
            def estimate = first + estRiseEachTick + (estRiseEachTick * i)
            def diff = candleData[i].close - estimate
            def diffPct = (diff / difference) * 100
            diffCount += Math.abs(diff)
            log "it $i $p, est : $estimate, diff: $diff, diffPct: $diffPct"
            log "${candleData[i].close} > $p"

            datePriceResult << new TrendLinePoint(date: candleData[i].date, price: candleData[i].close)

            p = candleData[i].close
        }

        def aveDifference = diffCount / size
        println "diff percent is $aveDifference"

        new TrendResult(trendline: datePriceResult, averageDiff: aveDifference, diffPercent: ((aveDifference / difference) * 100))
    }

    /**
     * Algorithm:
     *
     * 1 find average diff.
     *
     * 2. Try a line starting 5, 10, 20, 30 and 40% of the trend size below the lowest line,
     * then try one at 5, 10, 20, 30 and 40%  above, see which one comes out with the lowest
     * difference.
     *
     * If we try those while doing the same with the finish line we can see which is best.
     */
    def findBestTrendLine(candleData, ratioDiff){
       def firstResult = createDumbTrendLine(candleData)

        if(firstResult == null){
            null
        }

        if (candleData[0].close > candleData[-1].close) {
            log("ending"); return;
        }

        def first = candleData[0].close
        def last = candleData[-1].close
        def difference = last - first

        def fivePercentHigher = first + (difference * ratioDiff)
        createDumbTrendLine(candleData, fivePercentHigher, last)
    }

    /**
     * Given an array of CandleData
     */
    def createFirstTrendEstimate(candleData) {
        def first = candleData[0].close
        def last = candleData[-1].close

        return createTrendEstimate(candleData, first, last)
    }
}