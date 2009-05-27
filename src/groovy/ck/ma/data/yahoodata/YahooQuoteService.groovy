package ck.ma.data.yahoodata

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

public class YahooQuoteService {
  private static final CACHE_FOLDER = '/tmp/trendCache'

  YahooQuoteService() {
    if (!new File(CACHE_FOLDER).exists()) {
      File newFile = new File(CACHE_FOLDER)
      def created = newFile.mkdir();
      println "created: $created"
    }
  }

  static void main(args) {
    def now = new Date()
    def quotes = new YahooQuoteService();
    def candleData = quotes.getQuotes(now.minus(60), now, "BARC")
    println "data is: $candleData"
  }

  private buildUrl(dateFrom, dateTill, quote) {
    return "http://ichart.finance.yahoo.com/table.csv?s=$quote&a=$dateFrom.month&b=" + (dateFrom.date + 1) + "&c=$dateFrom.calendarDate.year&d=" + dateTill.month + "&e=" + (dateTill.date + 1) + "&f=" + dateTill.calendarDate.year + "&ignore=.csv";
  }

  /**
   * Gets quotes from yahoo
   */
  def getQuotes(dateFrom, dateTill, quote) {
    //println "getQuotes"
    quote += ".L"
    URL url = new URL(buildUrl(dateFrom, dateTill, quote))
    def cacheKey = createCacheKey(url)

    final cacheFile = CACHE_FOLDER + "/" + cacheKey + ".tmp"
    final def text

    if (new File(cacheFile).exists()) {
      text = new FileReader(cacheFile).text
    } else {
      def now = new Date()
      BufferedReader reader;

      try {
      reader = new BufferedReader(new InputStreamReader(url.openStream()))
      text = reader.text;

      def fos = new FileWriter(cacheFile)
      fos.write(text)
      fos.flush()
      println "downloading prices for $quote from yahoo, took ${now - new Date()}"
      } catch(e){
        println "Failed to download prices for $quote from $url due to ${e.getMessage()}"
        return []
      }
    }

    def lines = text.tokenize("\n");
    //println "text is $text, size is $lines.size"

    def candleData = []

    (1..(lines.size() - 1)).each {i ->
      candleData << new YahooCandleData(lines[i])
    }

    candleData.reverse()
  }

  private def createCacheKey(url) {
    try {
      MessageDigest algorithm = MessageDigest.getInstance("MD5")
      algorithm.reset()
      algorithm.update(url.toString().getBytes())

      byte[] messageDigest = algorithm.digest()

      StringBuffer hexString = new StringBuffer();
      for (int i = 0; i < messageDigest.length; i++) {
        hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
      }
      hexString.toString();
    } catch (NoSuchAlgorithmException nsae) {
      nsae.printStackTrace()
      ""
    }
  }
}

//	def getBarcFromYahoo(){
//		def slurper = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser())
//		def html = slurper.parse("http://uk.finance.yahoo.com/q/hp?s=BARC.L")
//		//http://ichart.finance.yahoo.com/table.csv?s=BARC.L&a=0&b=1&c=2008&d=3&e=30&f=2008&ignore=.csv
//		///html/body/div/div[3]/table[2]/tbody/tr[2]/td/table[6]/tbody/tr/td/table/tbody/tr[68]
//		def pricesTable = html.body.div[0].div[2].table[1].tr[1].td[0].table[5].tr[0].td[0].table[0]
//		def list = []
//		// while loop instead
//		for ( i in 1..66 ) {
//		    def row = pricesTable.tr[i]
//			def date = row.td[0]
//			def price = row.td[6]
//			list << price
//		}
//
//		def list2 = list.reverse()
//		list2;
//	}}