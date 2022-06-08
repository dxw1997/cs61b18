package lab14;

import lab14lib.Generator;


public class AcceleratingSawToothGenerator implements Generator{
    private int state;
    private int period;
    private double acc;
    private int mp;
    public AcceleratingSawToothGenerator(int pp, double aa){
        period = pp;
        acc = aa;
        state = mp = 0;
    }
    public double next(){
        double r = -1+2.0/(period-1)*(mp);
        mp++;
        if(mp >= period){
            period = (int)(period*acc);
            mp = 0;
        }
        state = state+1;
        return r;
    }
}
