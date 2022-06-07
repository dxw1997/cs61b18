package lab11.graphs;

import java.util.Queue;
import java.util.LinkedList;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    int s,t;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
        Queue<Integer> q = new LinkedList<>();
        q.add(s);
        while(!q.isEmpty()){
            int n = q.poll();
            if(marked[n])continue;
            marked[n] = true;
            announce();
            if(n == t) return;
            for(int nxt:maze.adj(n)){
                if(marked[nxt]) continue;
                edgeTo[nxt] = n;
                distTo[nxt] = distTo[n]+1;
                q.add(nxt);
                announce();
            }
        }
        return;
    }


    @Override
    public void solve() {
        bfs();
    }
}

