package ck.ma.publishing
/**
 * Created by IntelliJ IDEA.
 * User: ck
 * Date: 17-May-2009
 * Time: 10:30:35
 * To change this template use File | Settings | File Templates.
 */
/** TODO change name */
/** The Trending values for a paticular EMA for a stock */
public class EmaValues {

  /** The amount of time (days) this trend has been going for*/
  def trendLength

  /** The amount this trend has risen since the start of measurements
   * as a ratio of the current price */
  def trendRise
}