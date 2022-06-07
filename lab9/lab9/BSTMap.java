package lab9;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if(p == null || key == null) return null;
        int v = key.compareTo(p.key);
        if(v > 0) return getHelper(key, p.right);
        else if(v == 0) return p.value;
        else return getHelper(key, p.left);
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if(p == null){
            ++size;
            return new Node(key, value);
        }
        int v = key.compareTo(p.key);
        if(v == 0) p.value = value;
        else if(v > 0) p.right = putHelper(key, value, p.right);
        else p.left = putHelper(key, value, p.left);
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////


    private void keySetHelper(Node p, Set<K> s){
        if(p == null) return ;
        keySetHelper(p.left, s);
        keySetHelper(p.right, s);
        s.add(p.key);
    }

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> s = new HashSet<>();
        keySetHelper(root, s);
        return s;
    }

    private Node removeLeftRightmostChild(Node p){
        if(p.left == null) return null;
        if(p.left.right == null){
            Node r = p.left;
            p.left = r.left;
            r.left = null;
            return r;
        }
        Node pre = p.left, now = p.left.right;
        while(now.right != null){
            pre = now;
            now = now.right;
        }
        pre.right = now.left;
        now.left = null;
        return now;
    }

    private Node removeRightLeftmostChild(Node p){
        if(p.right == null) return null;
        if(p.right.left == null){
            Node r = p.right;
            p.right = r.right;
            r.right = null;
            return r;
        }
        Node pre = p.right, now = p.right.left;
        while(now.left != null){
            pre = now;
            now = now.left;
        }
        pre.left = now.right;
        now.right = null;
        return now;
    }

    private Node removeHelper(Node p, K key, V value, boolean testequal){
        if(p == null || key == null){
            value = null;
            return null;
        }
        int v = key.compareTo(p.key);
        if(v == 0){
            if(testequal && !value.equals(p.value)){
                value = null;
                return p;///don't delete.
            }
            Node n = removeLeftRightmostChild(p);
            if(n == null) n = removeRightLeftmostChild(p);///do delete
            if(n == null) return null;
            n.left = p.left;
            n.right = p.right;
            p.left = p.right = null;
            --size;
            value = p.value;
            return n;
        }else if(v > 0){
            p.right = removeHelper(p.right, key, value, testequal);
        }else{
            p.left = removeHelper(p.left, key, value, testequal);
        }
        return p;
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        V value = null;
        root = removeHelper(root, key, value, false);
        return value;
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        root = removeHelper(root, key, value, true);
        return value;
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
