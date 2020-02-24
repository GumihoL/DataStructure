package bearmaps;

import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

public class NaivePointSet implements PointSet{

    private HashSet<Point> pointSet;

    public NaivePointSet(List<Point> points){
        pointSet = new HashSet<Point>();
        for(Point p: points) pointSet.add(p);
    }

    @Override
    public Point nearest(double x, double y){
        Point ref = new Point(x, y);
        Point nearestPoint = null;
        double nearsetDist = Double.MAX_VALUE;
        for(Point p : pointSet){
            double pDist = Point.distance(p, ref);
            if (pDist < nearsetDist){
                nearestPoint = p;
                nearsetDist = pDist;
            }
        }
        return nearestPoint;
    }
}
