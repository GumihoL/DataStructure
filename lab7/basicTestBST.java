import org.junit.Test;
import static org.junit.Assert.*;

public class basicTestBST {

    @Test
    public void testPut(){
        BSTMap<String, Double> b = new BSTMap<>();
        b.put("a", 1.0);
        b.put("b", 3.3);
        b.put("c", 3.4);
        b.put("123", 9.2);
        assertEquals(b.get("a"), 1.0, 0.01);
        assertTrue(b.containsKey("123"));
        assertEquals(b.min(), "123");
        b.deleteMin();
        b.printInOrder();
        for (String key: b){
            System.out.println(key);
        }
    }
}
