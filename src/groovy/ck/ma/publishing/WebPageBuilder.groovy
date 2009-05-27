package ck.ma.publishing

import java.text.NumberFormat

public class WebPageBuilder {
  private final static NumberFormat NUMBER_FORMATTER = NumberFormat.getInstance();
  
  private final static script = """<script language=\"javascript\">
<!--

var state = 'none';

function showhide(layer_ref) {

if (state == 'block') {
state = 'none';
}
else {
state = 'block';
}
if (document.all) { //IS IE 4 or 5 (or 6 beta)
eval( \"document.all.\" + layer_ref + \".style.display = state\");
}
if (document.layers) { //IS NETSCAPE 4 or below
document.layers[layer_ref].display = state;
}
if (document.getElementById &&!document.all) {
hza = document.getElementById(layer_ref);
hza.style.display = state;
}
}
//-->
</script> """

  def static fmt(bigDecimal){    
    NUMBER_FORMATTER.format(bigDecimal.doubleValue())
  }

               //another change
   /**
    * Take
    * list of EmaIndicatorEntry s
    */
  def static writeEmaIndicators(emaIndicatorEntries, emaLengths, title, outputFolder, charts){
    def webpage = "<html><head><title>${title}</title>${script}</head><body>\n"

    webpage += "<table id='${title}-trending-table'>\n"
    webpage += """<table id='title-trending-table-title'><tr><td width="100">Company</td>\n"""

    emaLengths.each(){emaLength -> webpage += """<td width="100">EMA ${emaLength}</td>"""}

    webpage += "</table>\n"

    emaIndicatorEntries.each(){entry ->
      webpage += """<table><td width="100">${entry.name}</td>"""

      emaLengths.each(){emaLength ->
        webpage += """<td width="100">${entry.emas[emaLength].trendLength}</td>"""        
      }

      webpage += """<td width="100"><a style="" href="#" onclick="showhide('${entry.name}-info');">info</a></td>"""
      webpage += addInofDiv(entry.name, charts[entry.name])

      webpage += "</table><br>\n"
    }

    ///webpage += "</table>\n"""

    webpage += "</body></html>"
    println webpage
    def writer = new FileWriter(outputFolder + '/trenders.html')
    writer.write(webpage)
    writer.flush()
  }
  
  def static addInofDiv(symbol, chartFile){
     def webpage = """<div style="display: none" id="${symbol}-info">
          <a href="http://uk.finance.yahoo.com/q?s=${symbol}.L">yahoo</a></br>\n
          <a href="http://www.iii.co.uk/investment/detail?code=cotn:${symbol}.L&display=discussion&it=le">III</a></br>\n
          <a href="http://www.google.co.uk/finance?client=ob&q=LON:${symbol}">Google</a></br>\n
          <a href="http://www.shareprice.co.uk/${symbol}/">Shareprice</a></br>\n
          <a href="http://www.bloomberg.com/apps/news?pid=conews&tkr=${symbol}%3ALN">Bloomberg News</a></br>\n"""

          if(chartFile != null){
            webpage += "<img src='./images/${chartFile}' alt='Could not load file' /></br>\n"
          }
      webpage += "</div>"
      webpage
  }

  /** charts are a mapping of symbols to charts, if a symbol has an entry it will be added */
  def static writeBestTrenders(/*TrendingEntry[] */trenders, outputFolder, title, charts){
    def webpage = "<html><head><title>${title}</title>${script}</head><body>\n"

    webpage += "<table id='${title}-trending-table'>\n"
    webpage += """<table id='title-trending-table-title'>
      <tr><td width="100">Company</td><td width="100">Magnitude Ratio</td><td width="100">PurityRatio</td></tr></table>\n"""


    trenders.each(){trender ->
      //webpage += "  <tr><td>${trender.name}</td><td>${fmt(trender.magnitudeRatio)}</td><td>${fmt(trender.purityRatio)}</td></tr>\n"
//      webpage += """<table><tr><td width="100">${trender.name}</td><td width="100">${fmt(trender.magnitudeRatio)}</td><td width="100">${fmt(trender.purityRatio)}</td><td width="100"><a style="" href="#" onclick="showhide('${trender.name}-info');">info</a></td></tr></table>
//        <div style="display: none" id="${trender.name}-info">
//          <a href="http://uk.finance.yahoo.com/q?s=${trender.name}.L">yahoo</a></br>\n
//          <a href="http://www.iii.co.uk/investment/detail?code=cotn:${trender.name}.L&display=discussion&it=le">III</a></br>\n
//          <a href="http://www.google.co.uk/finance?client=ob&q=LON:${trender.name}">Google</a></br>\n
//          <a href="http://www.shareprice.co.uk/${trender.name}/">Shareprice</a></br>\n
//          <a href="http://www.bloomberg.com/apps/news?pid=conews&tkr=${trender.name}%3ALN">Bloomberg News</a></br>\n"""
//
//          if(charts[trender.name] != null){
//            webpage += "<img src='${charts[trender.name]}' alt='Couldn't load file' /></br>\n"
//          }
//      webpage += "</div>"
      webpage += addInofDiv(trender.name, charts[trender.name])
    }

    webpage += "</body></html>"
    println webpage
    def writer = new FileWriter(outputFolder + '/trenders.html')
    writer.write(webpage)
    writer.flush()
  }

  def static writeCharts(marketInfos, outputFolder, title) {
    def webpage = "<html><head><title>${title}</head><body>\n"

    def cout = 0;

    marketInfos.each {marketInfo ->
      webpage += "<table id='${title}-table'>\n"

      webpage += "<tr><td>"
      marketInfo.chartFiles.each {chartFile ->
        webpage += "<img src='${chartFile}' alt='Couldn't load file' />\n"
      }

      webpage += "</tr></td>"

      webpage += "<tr><td>key2</td><td>val1</td></tr>"
      webpage += "<tr><td>key1</td><td>val2</td></tr>"

      webpage += "  </table>"
    }


//    webpage += "  <table>\n"
//
//    webpage += "<tr><td>key2</td><td>val1</td></tr>"
//    webpage += "<tr><td>key1</td><td>val2</td></tr>"
//
//    marketInfos.each {marketInfo ->
//      marketInfo.info.each {key, value ->
//        webpage += "<tr><td>$key</td><td>$value</td></tr>"
//
//      }
//    }

    //webpage += """  </table>
    webpage += "</body></html>"
    println webpage
    def writer = new FileWriter(outputFolder + '/info.html')
    writer.write(webpage)
    writer.flush()
  }
}