package ck.ma.data.localdata

import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * Created by IntelliJ IDEA.
 * User: ck
 * Date: 12-May-2009
 * Time: 15:48:42
 * To change this template use File | Settings | File Templates.
 */

public class LocalFileData {

  //"EEE, MMM d, ''yy"  	Wed, Jul 4, '01
  private static final DateFormat DF = new SimpleDateFormat("d-MMM-yy");

  private def localRootFolder = "/home/ck/data/Trending Shares BiancaV2/storeWebPages";

  def getQuotes(dateFrom, dateTill, quote) {
    def candleData = []
    new File(localRootFolder+"/"+quote+".txt").eachLine{line ->
      if(!line.startsWith("-1")){
        def data = new LocalCandleData(line)
        if(data.date.after(dateFrom) && data.date.before(dateTill)){
          candleData << new LocalCandleData(line)
        }
      }
    }

    candleData.reverse()
  }

  static void main(args) {
    new LocalFileData().getQuotes(null,null,"BARC")
  }
}