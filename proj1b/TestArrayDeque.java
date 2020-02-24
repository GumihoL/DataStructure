import org.junit.Test;

public class TestArrayDeque {
    @Test
    public void testArrayDeque(){
        ArrayDeque<Integer> deque = new ArrayDeque<Integer>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);
        deque.addFirst(4);
        deque.addFirst(5);
        deque.addFirst(6);
        deque.addFirst(7);
        deque.addFirst(8);
        deque.addFirst(9);
        deque.addFirst(10);
        deque.addFirst(11);
        deque.addFirst(12);
        deque.addFirst(13);
        deque.addFirst(14);
        deque.addFirst(15);
        deque.addFirst(16);
        deque.removeFirst();
        deque.removeFirst();
        deque.removeFirst();
        deque.removeFirst();
        deque.removeFirst();
        deque.removeFirst();
        deque.removeFirst();
        deque.removeFirst();
        deque.removeFirst();
        deque.removeFirst();
        deque.removeFirst();
        deque.removeFirst();
        deque.removeFirst();
        deque.removeFirst();
        deque.removeFirst();
        deque.removeFirst();
        deque.printDeque();
    }
}
