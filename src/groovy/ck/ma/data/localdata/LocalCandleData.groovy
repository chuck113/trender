package ck.ma.data.localdata

import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * Created by IntelliJ IDEA.
 * User: ck
 * Date: 12-May-2009
 * Time: 15:56:35
 * To change this template use File | Settings | File Templates.
 */

public class LocalCandleData {
  Date date
  double open, close, high, low

  //"EEE, MMM d, ''yy"  	Wed, Jul 4, '01
  private static final DateFormat df = new SimpleDateFormat("d-MMM-yy");

  def LocalCandleData(date, open, close, high, low) {
    this.date = date;
    this.open = open;
    this.close = close;
    this.high = high;
    this.low = low;
  }

  public LocalCandleData(String rawInput) {
    // ordering is Date,Open,High,Low,Close,Volume,Adj Close
    rawInput = rawInput.replaceAll("\\s+", " ");

    def constituates = rawInput.tokenize(" ")
    date = df.parse(constituates[0])
    open = constituates[1].toDouble()
    high = constituates[2].toDouble()
    low = constituates[3].toDouble()
    close = constituates[4].toDouble()
  }

}