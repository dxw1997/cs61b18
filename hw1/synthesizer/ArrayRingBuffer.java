// TODO: Make sure to make this class a part of the synthesizer package
// package <package name>;
package synthesizer;

import java.util.Iterator;

//TODO: Make sure to make this class and all of its methods public
//TODO: Make sure to make this class extend AbstractBoundedQueue<t>
public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T>{

    private class KIterator<T> implements Iterator<T>{
        private int ptr;
        public KIterator() { ptr = first; }
        @Override
        public boolean hasNext() { return (ptr != last); }
        @Override
        public T next() {
            return (T) rb[ptr++];
        }
    }

    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        // TODO: Create new array with capacity elements.
        //       first, last, and fillCount should all be set to 0.
        //       this.capacity should be set appropriately. Note that the local variable
        //       here shadows the field we inherit from AbstractBoundedQueue, so
        //       you'll need to use this.capacity to set the capacity.
        this.capacity = capacity;
        this.fillCount = this.first = this.last = 0;
        rb = (T[])new Object[capacity+1];
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        // TODO: Enqueue the item. Don't forget to increase fillCount and update last.
        if(fillCount < capacity){
            rb[last] = x;
            last = (last+1)%capacity;
            fillCount++;
        }else{
            throw new RuntimeException("Ring Buffer Overflow");
        }
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        // TODO: Dequeue the first item. Don't forget to decrease fillCount and update
        if(fillCount > 0){
            T res = rb[first];
            rb[first] = null;
            first = (first+1)%capacity;
            fillCount--;
            return res;
        }else{
            throw new RuntimeException("Ring Buffer Underflow");
        }
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        // TODO: Return the first item. None of your instance variables should change.
        if(fillCount > 0){
            return rb[first];
        }else{
            throw new RuntimeException("Ring Buffer Underflow");
        }
    }

    // TODO: When you get to part 5, implement the needed code to support iteration.
    @Override
    public Iterator<T> iterator(){
        return new KIterator<T>();
    }

}
