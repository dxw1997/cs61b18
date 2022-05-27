
import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private Picture pic;
    private int[][] energyMatrix;

    private void initEnergyMatrix(){
        int M = pic.width(), N = pic.height();
        energyMatrix = new int[M][N];
        for(int i = 0;i < M;i++ ){
            for(int j = 0;j < N;j++ ){
                Color preX = i==0?pic.get(M-1,j):pic.get(i-1,j);
                Color aftX = i==M-1?pic.get(0,j):pic.get(i+1,j);
                Color preY = j==0?pic.get(i,N-1):pic.get(i,j-1);
                Color aftY = j==N-1?pic.get(i,0):pic.get(i,j+1);
                energyMatrix[i][j] = (preX.getBlue()-aftX.getBlue())*(preX.getBlue()-aftX.getBlue())+
                        (preX.getGreen()-aftX.getGreen())*(preX.getGreen()-aftX.getGreen())+
                        (preX.getRed()-aftX.getRed())*(preX.getRed()-aftX.getRed())+
                        (preY.getRed()-aftY.getRed())*(preY.getRed()-aftY.getRed())+
                        (preY.getGreen()-aftY.getGreen())*(preY.getGreen()-aftY.getGreen())+
                        (preY.getBlue()-aftY.getBlue())*(preY.getBlue()-aftY.getBlue());
            }
        }
    }

    public SeamCarver(Picture picture){
        this.pic = new Picture(picture);
        initEnergyMatrix();
    }
    public Picture picture(){// current picture
        return new Picture(pic);
    }
    public int width() { // width of current picture
        return pic.width();
    }
    public int height(){ // height of current picture
        return pic.height();
    }
    public double energy(int x, int y){ // energy of pixel at column x and row y
        if(x < 0 || x >= width() || y < 0 || y >= height()) throw new IndexOutOfBoundsException();
        return energyMatrix[x][y];
    }
    public int[] findHorizontalSeam(){ // sequence of indices for horizontal seam
        int [][] e = new int[pic.height()][pic.width()];
        return helpGetSeam(true, e);
    }

    private int getE(int x, int y, boolean isT){
        if(isT) return energyMatrix[y][x];
        return energyMatrix[x][y];
    }

    private int[] helpGetSeam(boolean isT, int[][] e){
        if(e.length == 0) return null;
        int M = e.length, N = e[0].length;
        int[] a = new int[N];
        for(int i = 0;i < N;++i ){
            for(int j = 0;j < M;++j ){
                if(i == 0) e[j][i] = getE(j,i,isT);
                else{
                    e[j][i] = getE(j,i,isT) + e[j][i-1];
                    if(j > 0) e[j][i] = Math.min(e[j][i], getE(j,i,isT)+e[j-1][i-1]);
                    if(j < M-1) e[j][i] = Math.min(e[j][i], getE(j,i,isT)+e[j+1][i-1]);
                }
            }
        }
        int idx = 0;
        for(int j = 1;j < M;j++ ){
            if(e[j][N-1] < e[idx][N-1]) idx = j;
        }
        int ridx = N-1;
        a[ridx] = idx;
        while(ridx > 0){
            if(idx > 0 && e[idx-1][ridx-1]==e[idx][ridx]-getE(idx,ridx,isT)){
                idx = idx-1;
            }else if(idx < M-1 && e[idx+1][ridx-1]==e[idx][ridx]-getE(idx,ridx,isT)){
                idx = idx+1;
            }
            ridx--;
            a[ridx] = idx;
        }
        return a;
    }

    public int[] findVerticalSeam() { // sequence of indices for vertical seam
        int [][] e = new int[pic.width()][pic.height()];
        return helpGetSeam(false, e);
    }
    public void removeHorizontalSeam(int[] seam) { // remove horizontal seam from picture
        if(seam.length != pic.width()) throw new IllegalArgumentException();
        pic = SeamRemover.removeHorizontalSeam(pic, seam);
        initEnergyMatrix();
    }
    public void removeVerticalSeam(int[] seam) { // remove vertical seam from picture
        if(seam.length != pic.height()) throw new IllegalArgumentException();
        SeamRemover.removeVerticalSeam(pic, seam);
        initEnergyMatrix();
    }

}
