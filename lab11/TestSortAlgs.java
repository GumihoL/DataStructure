
import com.sun.jdi.IntegerType;
import edu.princeton.cs.algs4.Queue;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestSortAlgs {
    private Queue<Integer> makeUnSortedQueue(){
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(6);
        queue.enqueue(9);
        queue.enqueue(3);
        queue.enqueue(4);
        queue.enqueue(5);
        queue.enqueue(8);
        queue.enqueue(10);
        queue.enqueue(0);
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(7);
        return queue;
    }
    @Test
    public void testMergeSort() {
        Queue<Integer> unsortedQueue = makeUnSortedQueue();
        Queue<Integer> sortedQueue = MergeSort.mergeSort(unsortedQueue);
        while (sortedQueue.size() != 1 && !sortedQueue.isEmpty()){
            Integer prev = sortedQueue.dequeue();
            Integer next = sortedQueue.peek();
            int cmp = next.compareTo(prev);
            assertTrue(cmp >= 0);
        }
    }

    @Test
    public void testQuickSort() {
        Queue<Integer> unsorted = makeUnSortedQueue();
        assertTrue(isSorted(QuickSort.quickSort(unsorted)));
    }

    /**
     * Returns whether a Queue is sorted or not.
     *
     * @param items  A Queue of items
     * @return       true/false - whether "items" is sorted
     */
    private <Item extends Comparable> boolean isSorted(Queue<Item> items) {
        if (items.size() <= 1) {
            return true;
        }
        Item curr = items.dequeue();
        Item prev = curr;
        while (!items.isEmpty()) {
            prev = curr;
            curr = items.dequeue();
            if (curr.compareTo(prev) < 0) {
                return false;
            }
        }
        return true;
    }
}
