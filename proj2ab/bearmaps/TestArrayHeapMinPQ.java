package bearmaps;

import org.junit.Test;

public class TestArrayHeapMinPQ {

    @Test
    public void testBasics(){
         ArrayHeapMinPQ<String> a = new ArrayHeapMinPQ<>();
         a.add("a", 1.1);
         a.add("b", 2.2);
         a.add("c", 0.2);
         a.add("abcd", 0.3);
         a.add("123134", 9.9);
         a.add("oper", 6.6);

         a.changePriority("a", 19.0);
    }
}
