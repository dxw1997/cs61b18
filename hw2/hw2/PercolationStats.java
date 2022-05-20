package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int N;
    private int T;
    private PercolationFactory pf;
    private double mean;
    private double stddev;

    private void compute(){
        double[] x = new double[T];
        for(int i = 0;i < T;++i ){
            Percolation p = pf.make(N);
            while(!p.percolates()){
                int r = StdRandom.uniform(0,N);
                int c = StdRandom.uniform(0, N);
                if(p.isOpen(r,c)) continue;
                p.open(r,c);
            }
            x[i] = 1.0*p.numberOfOpenSites()/(N*N);
        }
//        for(int i = 0;i < T;++i ){
//            mean += x[i];
//        }
//        mean /= T;
        mean = StdStats.mean(x);
//        for(int i = 0;i < T;++i ){
//            stddev += (x[i]-mean)*(x[i]-mean);
//        }
//        stddev /= (T-1);
        stddev = StdStats.stddev(x);
    }

    public PercolationStats(int N, int T, PercolationFactory pf){
        if(N <= 0 || T <=0 || pf==null) throw new IllegalArgumentException();
        this.N = N;
        this.T = T;
        this.pf = pf;
        compute();
    }
    public double mean(){
        return mean;
    }
    public double stddev(){
        return stddev;
    }
    public double confidenceLow(){
        return mean - 1.96*stddev/Math.sqrt(T);
    }
    public double confidenceHigh(){
        return mean + 1.96*stddev/Math.sqrt(T);
    }
}
