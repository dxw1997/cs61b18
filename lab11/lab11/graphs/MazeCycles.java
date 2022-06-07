package lab11.graphs;

import java.util.List;
import java.util.ArrayList;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private boolean[] visited;
    private int[] steps;

    boolean end;
    int dnode;
    boolean draw;

    public MazeCycles(Maze m) {
        super(m);
        end = false;
        visited = new boolean[m.V()];
        steps = new int[m.V()];
    }

    @Override
    public void solve() {
        // TODO: Your code here!
        int N = maze.N();
        List<Integer> lis = new ArrayList<>();

        for(int i = 1;i <= N;i++ ){
            for(int j = 1;j <= N;j++ ){
                int v = maze.xyTo1D(i,j);
                if(visited[v]) continue;
                dfs(v, v, 0);
                if(end) return;
            }
        }
    }

    void dfs(int v, int pre, int depth){
        if(marked[v] && depth-steps[v]>2){
            edgeTo[v] = pre;
            dnode = v;
            draw = true;
            end = true;
            announce();
            return;
        }
        if(marked[v]) return;
        marked[v] = true;
        visited[v] = true;
        steps[v] = depth;
        for(int nxt:maze.adj(v)){
            dfs(nxt, v, depth+1);
            if(end){
                if(v == dnode) draw = false;
                if(draw) edgeTo[v] = pre;
                announce();
                return;
            }
        }
        marked[v] = false;
    }

    // Helper methods go here
}

