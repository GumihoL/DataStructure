package bearmaps.proj2ab;

import java.util.List;

public class KDTree implements PointSet{

    private enum Orientation{
        Vertical,
        Horizontal
    }

    private class Node{
        private Point coordinate;
        private Node left;
        private Node right;
        private Orientation orientation;

        Node(double x, double y, Orientation orientation){
            coordinate = new Point(x, y);
            this.orientation = orientation;
            right = null;
            left = null;
        }
    }

    private Node root;
    public KDTree(List<Point> points){
        for (Point p : points){  root = insert(p, root);  }
    }

    // insert Point into a subtree rooted at Node n
    private Node insert(Point p, Node n ){
        // if Node n is null, then p will be the root.
        if (n == null) {
            return new Node(p.getX(), p.getY(), Orientation.Vertical);
        }
        // duplicate is not allowed
        if (p.equals(n.coordinate)){
            return n;
        }
        if(n.orientation == Orientation.Horizontal){
            if (p.getY() >= n.coordinate.getY()){
                n.right = (n.right == null)? new Node(p.getX(), p.getY(), Orientation.Vertical) : insert(p, n.right);
            }
            else{
                n.left = (n.left == null)? new Node(p.getX(), p.getY(), Orientation.Vertical) : insert(p, n.left);
            }
        }
        else{
            // Node n is Vertical
            if (p.getX() >= n.coordinate.getX()){
                n.right = (n.right == null)? new Node(p.getX(), p.getY(), Orientation.Horizontal) : insert(p, n.right);
            }
            else{
                n.left = (n.left == null)? new Node(p.getX(), p.getY(), Orientation.Horizontal) : insert(p, n.left);
            }
        }
        return n;
    }

    @Override
    public Point nearest(double x, double y){
        Point ref = new Point(x, y);
        Node best = root;
        //double bestDist = Double.MAX_VALUE;
        best = nearest(root, ref, best);
        //best = nearest(root, ref, best, bestDist);
        return best.coordinate;
    }

    private Node nearest(Node n, Point ref, Node best){
        // Traverse the whole tree: done.
        // Decide Good side and bad side: done.
        // Decide whether bad side is valuable to go.
        if (n == null) return best;
        double curDist = Point.distance(ref, n.coordinate);
        double bestDist = Point.distance(ref, best.coordinate);
        if (curDist < bestDist){
            best = n;
        }
        Node goodSide = null;
        Node badSide = null;
        if (n.left == null && n.right == null){

        } else if ( n.left == null){
            goodSide = n.right;
        } else if (n.right == null){
            goodSide = n.left;
        }
        else if (n.orientation == Orientation.Vertical){
            goodSide = (ref.getX() < n.coordinate.getX()) ? n.left : n.right;//////////////////
            badSide = (ref.getX() < n.coordinate.getX()) ? n.right : n.left;
        }
        else{
            // n.orientation is Horizontal.
            goodSide = (ref.getY() < n.coordinate.getY()) ? n.left : n.right;///////////////////// 易错点
            badSide = (ref.getY() < n.coordinate.getY()) ? n.right : n.left;
        }

        best = (goodSide != null) ? nearest(goodSide, ref, best) : best;
        best = ((badSide != null) && shouldGoBadSide(n, ref, best)) ? nearest(badSide, ref, best) : best;
        return best;
    }

    /** Decide which child is a good side. */
    Node decideGoodSide(Node n, Point ref){
        Node goodSide;
        if (n.left == null && n.right == null){
            return null;
        } else if ( n.left == null){
            goodSide = n.right;
        } else if (n.right == null){
            goodSide = n.left;
        }
        else if (n.orientation == Orientation.Vertical){
            double dist1X = Math.abs(ref.getX() - n.right.coordinate.getX());
            double dist2X = Math.abs(ref.getX() - n.left.coordinate.getX());
            goodSide = (dist1X < dist2X) ? n.right : n.left;
        }
        else{
            // n.orientation is Horizontal.
            double dist1Y = Math.abs(ref.getY() - n.right.coordinate.getY());
            double dist2Y = Math.abs(ref.getY() - n.left.coordinate.getY());
            goodSide = (dist1Y < dist2Y) ? n.right : n.left;
        }
        return goodSide;
    }

    private Node decideBadSide(Node n, Node goodSide){
        return (goodSide == n.right) ? n.left : n.right;
    }

    /** Return true if it is valuable to go to bad side, assume bad side is not null.*/
    private boolean shouldGoBadSide( Node n, Point ref, Node best){
        double bestDist = Point.distance(ref, best.coordinate);

        if (n.orientation == Orientation.Horizontal){
            double possiDistY = Math.pow(n.coordinate.getY() - ref.getY(), 2);
            return Double.compare(possiDistY, bestDist) < 0;
        } else {
            // badSide is Vertical
            double possiDistX = Math.pow(n.coordinate.getX() - ref.getX(), 2);
            return Double.compare(possiDistX, bestDist) < 0;
        }
    }
}
