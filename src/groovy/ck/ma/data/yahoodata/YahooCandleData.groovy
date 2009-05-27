package ck.ma.data.yahoodata

import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * A candle data object, created from data from yahoo
 */
class YahooCandleData {
    Date date
    double open, close, high, low

    private static final DateFormat df = new SimpleDateFormat("y-M-d");

    public YahooCandleData(String rawInput) {
        // ordering is Date,Open,High,Low,Close,Volume,Adj Close
        def constituates = rawInput.tokenize(",")
        date = df.parse(constituates[0])
        open = constituates[1].toDouble()
        high = constituates[2].toDouble()
        low = constituates[3].toDouble()
        close = constituates[4].toDouble()
    }

    public String toString ( ) {
      return "YahooCandleData{" +
        "date=" + date +
        ", open=" + open +
        ", close=" + close +
        ", high=" + high +
        ", low=" + low +
        '}' ;
    }
}