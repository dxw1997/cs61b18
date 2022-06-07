package lab11.graphs;

import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Comparator;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v){
        int tx = maze.toX(t), ty = maze.toY(t);
        int vx = maze.toX(v), vy = maze.toX(v);
        return Math.abs(tx-vx) + Math.abs(ty-vy);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        // TODO
        Queue<Node> pq = new PriorityQueue<>(new Cmp());
//        pq.add(1);pq.add(2);pq.add(3);pq.add(4);
//        while(!pq.isEmpty()){
//            System.out.println(pq.poll());
//        }
//        return;
        pq.add(new Node(s, h(s)));
        while(!pq.isEmpty()){
            Node n = pq.poll();
            if(marked[n.v]) continue;
            marked[n.v] = true;
            announce();
            System.out.println(distTo[n.v]+n.h);
            if(n.v == t) return;
            for(int nxt:maze.adj(n.v)){
                if(marked[nxt]) continue;
                edgeTo[nxt] = n.v;
                distTo[nxt] = distTo[n.v]+1;
                announce();
                pq.add(new Node(nxt, h(nxt)));
            }
        }
        return;
    }

    @Override
    public void solve() {
        astar(s);
    }

    private class Node{
        int v;
        int h;
        Node(int vv, int hh){
            v = vv;
            h = hh;
        }
    }

    private class Cmp implements Comparator<Node>{
        @Override
        public int compare(Node n1, Node n2){
            return distTo[n1.v]+n1.h-distTo[n2.v]-n2.h;
        }
    }
}

