package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Using Union-find method.
 * Adding two virtual nodes, one is on the top and one is at the bottom.
 */
public class Percolation {
    private int N;
    private boolean[][] pMatrix;
    private WeightedQuickUnionUF wq;
    private int openSitesNum;

    /**
     * Converting a position(x,y) in a matrix to an interger
     * @param r row number
     * @param c column number
     * @return the converted number
     */
    private int xyTo1D(int r, int c){
        if(r == -1) return 0;
        else if(r == N) return N*N+1;
        return r*N+c+1;
    }

    /**
     * Checking whether a position is out of bound.
     * @param r row number
     * @param c column number
     */
    private void check(int r, int c){
        if(r < 0 || r >= N || c < 0 || c >= N) throw new IndexOutOfBoundsException();
    }

    public Percolation(int N){
        if(N <= 0) throw new IllegalArgumentException();
        this.N = N;
        pMatrix = new boolean[N][N];
        wq = new WeightedQuickUnionUF(N*N+2);
        openSitesNum = 0;
    }
    public void open(int row, int col){
        check(row, col);
        if(!pMatrix[row][col]){
            ++openSitesNum;
            pMatrix[row][col] = true;
            if(row == 0 || isOpen(row-1, col)) wq.union(xyTo1D(row, col), xyTo1D(row-1, col));
            if(row == N-1 || isOpen(row+1, col)) wq.union(xyTo1D(row, col), xyTo1D(row+1, col));
            if(col > 0 && isOpen(row, col-1)) wq.union(xyTo1D(row, col), xyTo1D(row, col-1));
            if(col < N-1 && isOpen(row, col+1)) wq.union(xyTo1D(row, col), xyTo1D(row, col+1));
        }
    }
    public boolean isOpen(int row, int col){
        check(row, col);
        return pMatrix[row][col];
    }
    public boolean isFull(int row, int col){
        check(row, col);
        return wq.connected(xyTo1D(row, col), xyTo1D(-1, col));
    }
    public int numberOfOpenSites(){
        return openSitesNum;
    }
    public boolean percolates(){
        return wq.connected(xyTo1D(-1, 0), xyTo1D(N, 0));
    }

    public static void main(String[] args){
        ///for testing
        Percolation p = new Percolation(3);
        p.open(0,0);
        p.open(1,1);
        if(p.numberOfOpenSites() != 2) System.out.print("wrong sites number.\n");
        if(p.isFull(1,1)) System.out.print("wrong is Full\n");
        p.open(1,0);
        if(!p.isFull(1,1)) System.out.print("wrong is Full\n");
        p.open(2,1);
        if(!p.percolates()) System.out.print("wrong percolate\n");
        if(p.numberOfOpenSites() != 4) System.out.print("wrong sites number.\n");
    }
}
