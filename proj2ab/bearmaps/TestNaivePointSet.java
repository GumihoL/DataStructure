package bearmaps;

import org.junit.Test;

import java.util.List;

public class TestNaivePointSet {
    @Test
    public void testNaivePointSet(){
        Point p1 = new Point(1.1, 2.2);
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        NaivePointSet nps = new NaivePointSet(List.of(p1, p2, p3));
        Point ret = nps.nearest(3.0, 4.0);
        System.out.println("ret point is (" + ret.getX() + "," + ret.getY() + ")");
    }
}
