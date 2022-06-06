
import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Collections;
import edu.princeton.cs.introcs.In;


public class Boggle {

    private static final int R = 128;

    private static class Node{
        boolean exists;
//        Node[] links;
        HashMap<Character, Node> links2;
        Node(){
            exists = false;
//            links = new Node[R];
            links2 = new HashMap<>();
        }
    }
    private static class Trie{
        Node root = new Node();

        Node put(Node x, String k, int idx){
//            if(x == null) x = new Node();
            if(idx == k.length()){
                x.exists = true;
                return x;
            }
            char c = k.charAt(idx);
            boolean e = x.links2.containsKey(c);
            if(e == false) x.links2.put(c, new Node());
            put(x.links2.get(c),k,idx+1);
//            x.links2[c] = put(x.links[c], k, idx+1);
            return x;
        }

        void put(String k){
            put(root, k, 0);
        }

        static Node searchDele(Node r, char c){
            return r.links2.get(c);
        }
    }

    private static class Cmp implements Comparator<String>{
        @Override
        public int compare(String s1, String s2){
            if(s1.length() == s2.length()){
                return s2.compareTo(s1);
            }
            return s1.length()-s2.length();
        }
    }

    // File path of dictionary file
//    static String dictPath = "words.txt";
    static String dictPath = "words.txt";
    static int K;

    private static void solvesub(List<String> boards, int i, int j, PriorityQueue<String> pq, HashSet<Integer> vis, Node n, String pre){
        int M = boards.size(), N = boards.get(0).length();
        if(i < 0 || i >= M || j < 0 || j >= N || vis.contains(i*N+j)) return;
        Node n2 = Trie.searchDele(n, boards.get(i).charAt(j));
        if(n2 == null) return;
        String now = pre+boards.get(i).charAt(j);
        if(n2.exists){
            n2.exists = false;
//            System.out.println(now);
            pq.add(now);
            if(pq.size() > K) pq.poll();
        }
        vis.add(i*N+j);
        solvesub(boards, i-1, j, pq, vis, n2, now);
        solvesub(boards, i+1, j, pq, vis, n2, now);
        solvesub(boards, i, j-1, pq, vis, n2, now);
        solvesub(boards, i, j+1, pq, vis, n2, now);
        solvesub(boards, i-1, j-1, pq, vis, n2, now);
        solvesub(boards, i-1, j+1, pq, vis, n2, now);
        solvesub(boards, i+1, j-1, pq, vis, n2, now);
        solvesub(boards, i+1, j+1, pq, vis, n2, now);
        vis.remove(i*N+j);
    }

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        // YOUR CODE HERE
        ///UNDO. cases that you need to throw exceptions.
        K = k;
        In in1 = new In(dictPath);
        //List<String> words = new ArrayList<String>();
        Trie t = new Trie();
        while(in1.hasNextLine()){
            String w = in1.readLine();
            w = w.trim();
            if(w.length() > 0){
             //   System.out.println(w);
                t.put(w);
            }
        }
        in1 = new In(boardFilePath);
        List<String> boards = new ArrayList<String>();
        while(in1.hasNextLine()){
            String b = in1.readLine();
            boards.add(b);
        }
        for(int i = 1;i < boards.size();i++ ){
            ///need to throw an exception here.
            if(boards.get(i).length() != boards.get(i-1).length()){
                System.out.println("exception happened!");
            }
        }
        PriorityQueue<String> pq = new PriorityQueue(new Cmp());
//        pq.add("a");pq.add("b");pq.add("c");pq.add("d");
//        pq.add("ab");pq.add("bcc");pq.add("abcd");
//        pq.add("ac");pq.add("ad");
//        while(!pq.isEmpty()){
//            System.out.println(pq.poll());
//        }
        for(int i = 0;i < boards.size();i++ ){
            for(int j = 0;j < boards.get(i).length();j++ ){
                HashSet<Integer> vis = new HashSet<>();
                String pre = new String();
                solvesub(boards, i, j, pq, vis, t.root, pre);
            }
        }

        List<String> res = new ArrayList<>(pq.size());
        while(!pq.isEmpty()){
            res.add(pq.poll());
        }
        Collections.reverse(res);
        return res;
    }

    public static void main(String[] args){
//        List<String> r = Boggle.solve(7,"exampleBoard.txt");
//        List<String> r = Boggle.solve(20,"exampleBoard2.txt");
        long startTime = System.currentTimeMillis(); //程序开始记录时间
//        List<String> r = Boggle.solve(20,"smallBoard.txt");//451
        List<String> r = Boggle.solve(20,"smallBoard2.txt");//451
        long endTime = System.currentTimeMillis(); //程序结束记录时间
        long TotalTime = endTime - startTime; //总消耗时间
        for(int i = 0;i < r.size();i++ )
            System.out.println(r.get(i));
        System.out.println("total running time:"+TotalTime);
    }
}
