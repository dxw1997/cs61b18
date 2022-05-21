package hw4.puzzle;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

import  edu.princeton.cs.algs4.MinPQ;

public class Solver {

    private class State{
        public int m;
        public int e;
        public WorldState s;
        public WorldState parent;
        public List<WorldState> lis;
        public State(int m, WorldState s, List<WorldState> pre, WorldState parent){
            this.m = m;
            this.s = s;
            this.e = s.estimatedDistanceToGoal();
            this.parent = parent;
            this.lis = new ArrayList<>();
            for(WorldState s1:pre) lis.add(s1);
            lis.add(s);
        }
    }

    private class Cmp implements Comparator<State>{
        @Override
        public int compare(State s1, State s2){
            return (s1.m+s1.e) - (s2.m+s2.e);
        }
    }

    private int moves;
    private List<WorldState> list;

    public Solver(WorldState initial){
        MinPQ<State> mpq = new MinPQ(10,new Cmp());
        list = new ArrayList<>();
        //HashSet<WorldState> vis = new HashSet<>();
        mpq.insert(new State(0,initial, new ArrayList<WorldState>(), null));
        while(!mpq.isEmpty()){
            State t = mpq.delMin();
           // if(vis.contains(t.s)) continue;
//            System.out.println(t.s.toString());
            if(t.s.isGoal()){
                moves = t.m;
//                System.out.println("final:"+t.m);
                list = t.lis;
                return;
            }
//            System.out.println("Main........");
//            System.out.println(t.s.toString());
            //vis.add(t.s);
//            System.out.println("NXT.........");
            for(WorldState nxt:t.s.neighbors()){
                //if(vis.contains(nxt) == false)
                if(nxt.equals(t.parent) == false)
                    mpq.insert(new State(t.m+1, nxt, t.lis, t.s));
//                System.out.println(nxt.toString());
            }
        }//while
    }

    public int moves(){
        return moves;
    }
    public Iterable<WorldState> solution(){
        return list;
    }
}
