
import java.io.Serializable;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;

public class BinaryTrie implements Serializable {
    private class Node implements Serializable{
        boolean isend;
        char c;
        Node[] links;
        int v;
        public Node(Character cc, Integer vv, boolean ise){
            c = cc;
            v = vv;
            isend = ise;
            if(isend == false) links = new Node[2];
        }
    }

    private class Cmp implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2){
            return n1.v-n2.v;
        }
    }

    private Node root;

    public BinaryTrie(Map<Character, Integer> frequencyTable){
        PriorityQueue<Node> pq = new PriorityQueue<>(new Cmp());
        for(Map.Entry<Character, Integer> e:frequencyTable.entrySet()){
            pq.add(new Node(e.getKey(), e.getValue(), true));
        }
        while(pq.size() > 1){
            Node n1 = pq.poll();
            Node n2 = pq.poll();
            Node n = new Node(' ', n1.v+n2.v, false);
            n.links[0] = n1;
            n.links[1] = n2;
            pq.add(n);
        }
        root = pq.poll();
    }
    public Match longestPrefixMatch(BitSequence querySequence){
        BitSequence bits = new BitSequence();
        Node t = root;///root should not isend(being true!!).
        for(int i = 0;i < querySequence.length();i++ ){
            int bit = querySequence.bitAt(i);
            t = t.links[bit];
            bits = bits.appended(bit);
            if(t.isend){
                return new Match(bits, t.c);
            }
        }
        return new Match(querySequence, '@');///failed.
    }

    private void createLTable(Node n, Map<Character, BitSequence> res, BitSequence bits){
        if(n.isend){
            res.put(n.c, bits);
            return;
        }
        createLTable(n.links[0], res, bits.appended(0));
        createLTable(n.links[1], res, bits.appended(1));
    }

    public Map<Character, BitSequence> buildLookupTable(){
        Map<Character, BitSequence> res = new HashMap<>();
        createLTable(root, res, new BitSequence());
        return res;
    }

}
