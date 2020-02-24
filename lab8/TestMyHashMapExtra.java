import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Iterator;

/** Tests of optional parts of lab 9. */
public class TestMyHashMapExtra {

    /* Remove Test 
     */
    @Test
    public void testRemove() {
        MyHashMap<String, String> q = new MyHashMap<String, String>();
        q.put("c", "a");
        q.put("b", "a");
        q.put("a", "a");
        q.put("d", "a");
        q.put("e", "a"); // a b c d e
        assertTrue(null != q.remove("c"));
        assertFalse(q.containsKey("c"));
        assertTrue(q.containsKey("a"));     
        assertTrue(q.containsKey("b"));
        assertTrue(q.containsKey("d"));
        assertTrue(q.containsKey("e"));
    }
    
    /* Remove Test 2 
     * test the 3 different cases of remove
     */
    @Test
    public void testRemoveThreeCases() {
        MyHashMap<String, String> q = new MyHashMap<String, String>();
        q.put("c", "a");
        q.put("b", "a");
        q.put("a", "a");
        q.put("d", "a");
        q.put("e", "a");                         // a b c d e
        assertTrue(null != q.remove("e"));      // a b c d
        assertTrue(q.containsKey("a"));     
        assertTrue(q.containsKey("b"));
        assertTrue(q.containsKey("c"));
        assertTrue(q.containsKey("d"));
        assertTrue(null != q.remove("c"));      // a b d
        assertTrue(q.containsKey("a"));     
        assertTrue(q.containsKey("b"));
        assertTrue(q.containsKey("d"));
        q.put("f", "a");                         // a b d f
        assertTrue(null != q.remove("d"));      // a b f
        assertTrue(q.containsKey("a"));     
        assertTrue(q.containsKey("b"));
        assertTrue(q.containsKey("f"));
    }
    @Test
    public void testIterable(){
        MyHashMap<String, String> q = new MyHashMap<String, String>();
        q.put("c", "a");
        q.put("b", "a");
        q.put("a", "a");
        q.put("d", "a");
        q.put("e", "a");                         // a b c d e
        // I don't know how to use enhanced-for loop to iterate on q...
        Iterator iter = q.iterator();
        while (iter.hasNext()){
            iter.next();
        }

    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(TestMyHashMapExtra.class);
    }
}
