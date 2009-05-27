import ck.ma.algorithms.MovingAverageJava;
import junit.framework.TestCase;


public class EmaTest extends TestCase{

    private MovingAverageJava movingAve = new MovingAverageJava();
    private double[] prices = new double[]{64.75  , 63.79  , 63.73  , 63.73  , 63.55  , 63.19  , 63.91  , 63.85  ,
                    62.95  , 63.37  , 61.33  , 61.51  , 61.87  , 60.25  , 59.35  , 59.95  , 58.93  , 57.68  , 58.82  , 58.87};


    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void testSimpleMA() throws Exception{
        //double[] prices = new double[]{11 , 12 , 13 , 14 ,15};

        //movingAve.simpleMA(prices, 3);
    }

    public static void main(String[] args) {
        //double[] prices = new double[]{447.3, 456.8, 451.0, 452.5, 453.4, 455.5, 456.0, 454.7, 453.5, 456.5, 459.5, 465.2, 460.8, 460.8};

        //new EmaTest().movingAve.ema(prices, 10);
    }

}
