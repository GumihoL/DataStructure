package bearmaps;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class KDTreeTest {
    /** random number generator.*/
    private static Random r = new Random(500);

    /** Generate a random point. */
    private Point randomPoint(){
        return new Point(r.nextDouble(), r.nextDouble());
    }

    /** Generate a list of N random points. */
    private List<Point> randomPoints(int N){
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < N; i++){
            points.add(randomPoint());
        }
        return points;
    }

    private List<Point> buildLectruePoints(){
        Point pA = new Point(2, 3);
        Point pB = new Point(4, 2);
        Point pC = new Point(4, 5);
        Point pD = new Point(3 ,3);
        Point pE = new Point(1, 5);
        Point pF = new Point(4, 4);
        return List.of(pA, pB, pC, pD, pE, pF);
    }

    @Test
    public void testKDTreeConstructor(){
        KDTree kd = new KDTree(buildLectruePoints());
    }

    @Test
    public void testDuplicatePoints(){
        Point pA = new Point(1,2);
        Point pB = new Point(1,2);
        KDTree kd = new KDTree(List.of(pA, pB));
    }

    @Test
    public void testNearestWithLectureDemo(){
        KDTree kd = new KDTree(buildLectruePoints());
        NaivePointSet nps = new NaivePointSet(buildLectruePoints());
        Point expected = nps.nearest(0, 7);
        Point actual = nps.nearest(0, 7);
        assertEquals(expected, actual);
    }

    @Test
    public void testNearestWithLectureDemo100Queries(){
        KDTree kd = new KDTree(buildLectruePoints());
        NaivePointSet nps = new NaivePointSet(buildLectruePoints());
        List<Point> queries = randomPoints(100);
        for (Point p : queries){
            Point expected = nps.nearest(p.getX() * 10, p.getY() * 10);
            Point actual = kd.nearest(p.getX() * 10, p.getY() * 10);
            assertEquals(expected, actual);
        }
    }


    @Test
    public void testWith1000Points(){
        List<Point> pointList = randomPoints(1000);
        NaivePointSet nps = new NaivePointSet(pointList);
        KDTree kd = new KDTree(pointList);

        List<Point> queries = randomPoints(500);
        for (Point p: queries){
            Point expected = nps.nearest(p.getX(), p.getY());
            Point actual = kd.nearest(p.getX(), p.getY());
            assertEquals(expected, actual);
        }

    }
    @Test
    public void compareTimingOfNaiveVsKDTreeLikeTheSpec() {
        List<Point> randomPoints = randomPoints(20000);
        KDTree kd = new KDTree(randomPoints);
        NaivePointSet nps = new NaivePointSet(randomPoints);
        List<Point> queryPoints = randomPoints(5000);
        long start = System.currentTimeMillis();
        for (Point p : queryPoints) {
            nps.nearest(p.getX(), p.getY());
        }
        long end = System.currentTimeMillis();
        System.out.println("Naive 10000 queries on 100000 points: " + (end - start) / 1000.0 + "seconds.");
        start = System.currentTimeMillis();

        for (Point p : queryPoints) {
            kd.nearest(p.getX(), p.getY());
        }
        end = System.currentTimeMillis();
        System.out.println("KDTree 10000 queries on 100000 points: " + (end - start) / 1000.0 + "seconds.");
    }
}
