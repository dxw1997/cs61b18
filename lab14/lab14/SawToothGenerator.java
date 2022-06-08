package lab14;

import lab14lib.Generator;


public class SawToothGenerator implements Generator{
    private int period;
    private int state;
    public SawToothGenerator(int per){
        period = per;
        state = 0;
    }
    public double next(){
        double r = -1+2.0/(period-1)*(state%(period));
        state = state+1;
        return r;
    }
}
