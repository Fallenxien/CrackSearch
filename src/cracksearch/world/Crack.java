// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch.world;

import cracksearch.util.CrackClickArea;
import cracksearch.util.Point;

/**
 * Crack Object
 *
 * Contains all data associated with a crack. Geometric data is stored as a list of points.
 *
 * The class acts almost as an interface between the GUI and the logic of the program.
 */
public class Crack {

    public final static int MAX_POINTS = 500;

    private Point[] points;
    private double length;
    private CrackClickArea clickArea;

    public Crack(Point[] points, double length) {
        this.points = points;
        this.length = length;

        clickArea = new CrackClickArea(getStart(), getEnd());
    }

    public Crack(Point[] points) {
        this.points = points;
        this.length = calcLength(points);

        clickArea = new CrackClickArea(getStart(), getEnd());
    }

    /**
     * Calculates the length of a given set of points.
     * @param crack Point array associated with the crack
     * @return length of crack as int
     */
    private Double calcLength(Point[] crack) {

        double length = 0;

        for (int i = 1;i < crack.length;i++) {
            length += calcDistanceBetween(crack[i-1], crack[i]);
        }

        return length;
    }

    /**
     * Calculates the distance between 2 points
     * @param p1 point 1
     * @param p2 point 2
     * @return distance between points
     */
    private Double calcDistanceBetween(Point p1, Point p2) {
        double xdiff = p1.x - p2.x;
        double ydiff = p1.y - p2.y;

        return Math.sqrt(xdiff*xdiff + ydiff*ydiff);
    }

    public Point getPoint(int index) {
        return points[index];
    }

    public Point getStart() {
        return points[0];
    }

    public Point getEnd() {
        return points[points.length-1];
    }

    public double getLength() {
        return length;
    }

    public int numPoints() {
        return points.length;
    }

    public boolean contains(Point p) {
        return clickArea.contains(p);
    }
}
