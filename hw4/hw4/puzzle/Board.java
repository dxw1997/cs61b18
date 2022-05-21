package hw4.puzzle;

import java.util.Set;
import java.util.HashSet;

public class Board implements WorldState{

    private int N;
    private int[][] tiles;
    private int zx, zy;

    private int hash;
    public Board(int[][] _tiles){
        if(_tiles.length == 0 || _tiles[0].length==0 || _tiles.length != _tiles[0].length)
            throw new IllegalArgumentException();
        this.N = _tiles.length;
        tiles = new int[N][N];
        for(int i = 0;i < N;++i ){
            for(int j = 0;j < N;++j ){
                tiles[i][j] = _tiles[i][j];
                if(tiles[i][j] == 0){
                    zx = i;
                    zy = j;
                }
            }
        }
    }
    public int tileAt(int i, int j){
        if(i < 0 || j < 0 || i >= N || j >= N)
            throw new IndexOutOfBoundsException();
        return tiles[i][j];
    }
    public int size(){
        return N;
    }
    public int hamming(){
        int dist = 0;
        for(int i = 0, en = 1;i < N;++i ){
            for(int j = 0;j < N;++j,++en ){
                if(this.tiles[i][j] != en && this.tiles[i][j] != 0) ++dist;
            }
        }
        return dist;
    }
    public int manhattan(){
        int dist = 0;
        for(int i = 0, en = 1;i < N;++i ){
            for(int j = 0;j < N;++j,++en ){
                if(this.tiles[i][j] != en && this.tiles[i][j] != 0){
                    int r = (this.tiles[i][j]-1)/N;
                    int c = (this.tiles[i][j]-1)%N;
                    dist += Math.abs(r-i)+Math.abs(c-j);
                }
            }
        }
        return dist;
    }

    private void swap(int r1, int c1, int r2, int c2){
        int tmp = tiles[r1][c1];
        tiles[r1][c1] = tiles[r2][c2];
        tiles[r2][c2] = tmp;
    }

    @Override
    public Iterable<WorldState> neighbors(){
        Set<WorldState> neighbors = new HashSet<>();
        if(zx > 0){
            swap(zx, zy, zx-1, zy);
            neighbors.add(new Board(tiles));
            swap(zx, zy, zx-1, zy);
        }
        if(zx < N-1){
            swap(zx, zy, zx+1, zy);
            neighbors.add(new Board(tiles));
            swap(zx, zy, zx+1, zy);
        }
        if(zy > 0){
            swap(zx, zy, zx, zy-1);
            neighbors.add(new Board(tiles));
            swap(zx, zy, zx, zy-1);
        }
        if(zy < N-1){
            swap(zx, zy, zx, zy+1);
            neighbors.add(new Board(tiles));
            swap(zx, zy, zx, zy+1);
        }
        return neighbors;
    }
    @Override
    public int estimatedDistanceToGoal(){
        return manhattan();
    }
    @Override
    public boolean equals(Object y){
        if(y == this) return true;
        if(y == null) return false;
        if(this.getClass() != y.getClass()) return false;
        Board b = (Board)y;
        if(b.N != this.N) return false;
        for(int i = 0;i < N;++i ){
            for(int j = 0;j < N;++j ){
                if(b.tiles[i][j] != this.tiles[i][j])
                    return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode(){
        int base = N*N;
        if(hash == 0){
            for(int i = 0;i < N;++i ){
                for(int j = 0;j < N;++j ){
                    hash = hash*base + tiles[i][j];
                }
            }
        }
        return hash;
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
