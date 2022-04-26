
public class LinkedListDeque<T> {
    private class LNode<T>{
        public T data;
        public LNode<T> next;
        public LNode<T> prev;
        LNode(){
            prev = next = null;
        }
    }

    private int size;
    private LNode<T> head, tail;

    public LinkedListDeque(){
        size = 0;
        head = tail = new LNode<>();///setinel node
    }

    /**
     * add an item to the front of the LinedListDeque
     */
    public void addFirst(T item){
        head.data = item;
        LNode<T> nh = new LNode<>();
        nh.next = head;
        head.prev = nh;
        head = nh;
        ++size;
        return;
    }
    /**
     * add an item to the tail of this linked-deque.
     */
    public void addLast(T item){
        LNode<T> nt = new LNode<>();
        nt.data = item;
        nt.prev = tail;
        tail.next = nt;
        tail = nt;
        ++size;
        return;
    }

    /**
     *
     * @return whether this linked-deque is empty.
     */
    public boolean isEmpty(){
        return size==0;
    }

    /**
     *
     * @return the size of this linked-deque.
     */
    public int size(){
        return size;
    }

    /**
     * print the elements in the deque from front
     * to end , separated by a space.
     */
    public void printDeque(){
        if(head == tail) return;
        LNode<T> p = head.next;
        System.out.print(p.data);
        p = p.next;
        while(p != null){
            System.out.print(" "+p.data);
            p = p.next;
        }
        return;
    }

    /**
     * remove the first element of this linked-deque
     * @return the first element
     */
    public T removeFirst(){
        if(head == tail) return null;
        head = head.next;
        head.prev = null;
        --size;
        return head.data;
    }

    /**
     * remove the last element of this linked-deque
     * @return the last element
     */
    public T removeLast(){
        if(head == tail) return null;
        LNode<T> ot = tail;
        tail = tail.prev;
        tail.next = null;
        --size;
        return ot.data;
    }

    /**
     * get the element at the index of linked-deque
     * @param index positions of the element
     * @return the element if find or null
     */
    public T get(int index){
        if(index < 0 || index >= size) return null;
        LNode<T> mp = head.next;
        for(;index > 0 && mp != null;--index){
            mp = mp.next;
        }
        if(mp != null) return mp.data;
        else return null;
    }

    private T getRecursiveSub(int index, LNode<T> p){
        if(index == 0) return p.data;
        return getRecursiveSub(index-1, p.next);
    }

    /**
     * get the element in the index of the linked-deque
     * @param index the position of the deque
     * @return the element
     */
    public T getRecursive(int index){
        if(index < 0 || index >= size) return null;
        return getRecursiveSub(index, head.next);
    }
}
