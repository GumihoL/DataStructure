package es.datastructur.synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Double> arb = new ArrayRingBuffer(10);
        assertEquals(10, arb.capacity());
        assertEquals(0, arb.fillCount());
        assertTrue(arb.isEmpty());
        assertFalse(arb.isFull());
        arb.enqueue(1.1);
        assertEquals(1.1, arb.peek(), 0.01);
        arb.enqueue(2.2);
        arb.enqueue(3.3);
        arb.enqueue(4.4);
        assertEquals(1.1, arb.dequeue(), 0.01);
        assertEquals(2.2, arb.dequeue(), 0.01);
        assertEquals(3.3, arb.dequeue(), 0.01);
        assertEquals(4.4, arb.dequeue(), 0.01);
        assertTrue(arb.isEmpty());
        for (int i = 0; i < arb.capacity(); i++){
            arb.enqueue(1.11);
        }
        assertTrue(arb.isFull());
        for (Double d : arb){
            ;
        }
    }
}
