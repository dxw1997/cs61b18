package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer(10);
        assertEquals(10,arb.capacity());
        arb.enqueue(1);
        assertEquals(1,arb.fillCount());
        arb.enqueue(2);
        arb.enqueue(3);
        arb.enqueue(4);
        arb.enqueue(5);
        assertEquals(5,arb.fillCount());
        assertEquals(1,arb.peek());
        assertEquals(1,arb.dequeue());
        assertEquals(4,arb.fillCount());
        assertEquals(2,arb.dequeue());
        assertEquals(3,arb.dequeue());
        assertEquals(4,arb.dequeue());
        assertEquals(5,arb.peek());
        assertEquals(1,arb.fillCount());
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
