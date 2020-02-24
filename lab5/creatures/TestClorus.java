package creatures;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Color;
public class TestClorus {

    @Test
    public void testBasics(){
        Clorus c = new Clorus(2);
        assertEquals(2, c.energy(),0.01);
        assertEquals(new Color(34, 0, 231), c.color());
        c.move();
        assertEquals(1.97, c.energy(), 0.01);
        c.stay();
        assertEquals(1.96, c.energy(), 0.01);
        c.move();
        assertEquals(1.93, c.energy(), 0.01);
        c.stay();
        assertEquals(1.92, c.energy(), 0.01);
    }
    @Test
    public void testReplicate(){
        Clorus c = new Clorus(2);
        Clorus springOff = c.replicate();
        assertEquals(1, c.energy(), 0.01);
        assertEquals(1,springOff.energy(), 0.01);
    }

    @Test
    public void testChoose(){
        Clorus c = new Clorus(2);

    }

}
