package ck.ma.algorithms;

import ck.ma.charts.TrendResult;
import ck.ma.charts.TrendLinePoint;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.text.NumberFormat;


public class MovingAverageJava {
    private static final NumberFormat FORMAT = NumberFormat.getNumberInstance();

    private static List<BigDecimal> asBDList(double[] doubles) {
        List<BigDecimal> result = new ArrayList<BigDecimal>(doubles.length);

        for (double d : doubles) {
            result.add(new BigDecimal(d));
        }

        return result;
    }

    public List<BigDecimal> ema(double[] prices, int length) {
        return ema(asBDList(prices), length);
    }


    public List<BigDecimal> ema(List<BigDecimal> prices, int length) {
        if (length >= prices.size()) {
            return Collections.emptyList();
        }

        int resultSize = prices.size() - length + 1;
         //System.out.println("resultSize size is " + resultSize);

        List<BigDecimal> result = new ArrayList<BigDecimal>(resultSize);
        //List<BigDecimal> rest = prices.subList(length-1, (prices.size()));  //prices[length..(prices.size - 1)]

       // System.out.println("rest size is " + rest.size());

        BigDecimal k = new BigDecimal(2.0d).divide(new BigDecimal(length + 1), 3, RoundingMode.HALF_UP);

        // optimize by using subset
        List<BigDecimal> smas = simpleMA(prices, length);
       // System.out.println("MovingAverageJava.ema last sma is: " + smas.get(0));
        BigDecimal previousDayEma = smas.get(0);
        result.add(previousDayEma);

       // System.out.println("MovingAverageJava.ema  number -1     |        | " +FORMAT.format(previousDayEma.doubleValue()));


        for (int i = 0; i < resultSize-1; i++) {
            int offset = i + length;
            BigDecimal price = prices.get(offset);

            BigDecimal ema = price.subtract(previousDayEma).multiply(k).add(previousDayEma);
            //System.out.println("MovingAverageJava.ema  number"+i+" "+FORMAT.format(price.doubleValue()) +" | "
            //        +FORMAT.format(previousDayEma.doubleValue())+" | " +FORMAT.format(ema.doubleValue()));

            previousDayEma = ema;
            result.add(ema);
        }

        return result;
    }

    public List<BigDecimal> simpleMA(double[] prices, int length) {
        return simpleMA(asBDList(prices), length);
    }

    public List<BigDecimal> simpleMA(List<BigDecimal> prices, int length) {
        if (length >= prices.size()) {
            return Collections.emptyList();
        }

        int resultSize = prices.size() - length + 1;

        List<BigDecimal> result = new ArrayList<BigDecimal>(resultSize);

        for (int i = 0; i < resultSize; i++) {
            BigDecimal ma = new BigDecimal(0.0d);

            List<BigDecimal> newArray = prices.subList(i, i + length); //prices[i..i+length-1]

            for (BigDecimal d : newArray) {
                ma = ma.add(d);
            }

            result.add(ma.divide(new BigDecimal(length), 3, RoundingMode.HALF_UP));
        }

        return result;
    }

}
