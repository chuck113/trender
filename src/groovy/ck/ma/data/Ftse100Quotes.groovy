package ck.ma.data
/**
 * Created by IntelliJ IDEA.
 * User: ck
 * Date: 13-May-2009
 * Time: 19:33:49
 * To change this template use File | Settings | File Templates.
 */

public class Ftse100Quotes {

  private file ="/home/ck/data/projects/trender/IntelliJTender/ftse100.txt"

  def get(){
     def result = []
     new FileReader(file).eachLine {line -> result << line}
     result
  }

  public static void main(args) {
      println new Ftse100Quotes().get()
  }
}