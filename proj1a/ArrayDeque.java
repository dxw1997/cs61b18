
public class ArrayDeque<T>{
    private T[] darr;
    private int capacity;
    private int size;
    private int head, tail;

    public ArrayDeque(){
        head = tail = 0;
        capacity = 8;
        darr = (T[])new Object[capacity];
    }

    /**
     * extend the capacity of this deque by 2
     */
    private void extendCapacity(){
        if(size == 0) return;
        T[] ndarr = (T[])new Object[capacity*2];
        int h = head==0?capacity-1:head-1;
        int t = tail==capacity-1?0:tail+1;
        for(int idx = size-1;true;h = h==0?capacity-1:h-1){
            ndarr[idx] = darr[h];
            if(h == t) break;
            --idx;
        }
        head = size;
        tail = 2*capacity-1;
        capacity = 2*capacity;
        darr = ndarr;
        return;
    }

    private void shrinkCapacity(){
        if(capacity <= 8) return;
        T[] ndarr = (T[])new Object[capacity/2];
        int h = head==0?capacity-1:head-1;
        int t = tail==capacity-1?0:tail+1;
        for(int idx = size-1;true;h = h==0?capacity-1:h-1){
            ndarr[idx] = darr[h];
            if(h == t) break;
            --idx;
        }
        head = size;
        tail = capacity/2-1;
        capacity = capacity/2;
        darr = ndarr;
        return;
    }

    private void checkShrink(){
        if(capacity > 8 && size < capacity/4){
            shrinkCapacity();
        }
        return;
    }

    public void addFirst(T item){
        if(size == capacity){
            extendCapacity();
        }
        darr[head] = item;
        if(head == tail && size == 0) {
            tail = (tail == 0) ? capacity - 1 : tail - 1;
        }
        ++size;
        head = head<capacity-1?head+1:0;
    }
    public void addLast(T item){
        if(size == capacity){
            extendCapacity();
        }
        darr[tail] = item;
        if(head == tail && size == 0){
            head = (head == capacity-1)?0:head+1;
        }
        ++size;
        tail = (tail==0)?capacity-1:tail-1;
    }
    public boolean isEmpty(){
        return size == 0;
    }
    public int size(){
        return size;
    }
    public void printDeque(){
        if(size == 0) return;
        int h = head==0?capacity-1:head-1;
        int t = tail==capacity-1?0:tail+1;
        System.out.print(darr[h]);
        if(h == t) return;
        for(h = h==0?capacity-1:h-1;true;h = h==0?capacity-1:h-1){
            System.out.print(" "+darr[h]);
            if(h == t) break;
        }
        return;
    }
    public T removeFirst(){
        if(size == 0) return null;
        head = head==0?capacity-1:head-1;
        T res = darr[head];
        darr[head] = null;
        --size;
        checkShrink();
        return res;
    }
    public T removeLast(){
        if(size == 0) return null;
        tail = tail==capacity-1?0:tail+1;
        T res = darr[tail];
        darr[tail] = null;
        --size;
        checkShrink();
        return res;
    }
    public T get(int index){
        if(index < 0 || index >= size) return null;
        int tidx = head-1-index;
        if(tidx < 0) tidx += capacity;
        return darr[tidx];
    }
}
