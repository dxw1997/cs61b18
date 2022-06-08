package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator{
    private int period;
    private int state;
    public StrangeBitwiseGenerator(int per){
        period = per;
        state = 0;
    }
    public double next(){
//        int weirdState = state & (state >>> 3) % period;
        int weirdState = state & (state >> 3) & (state >> 8) % period;
        double r = -1+2.0/(period-1)*(weirdState%(period));
        state = state+1;
        return r;
    }
}
