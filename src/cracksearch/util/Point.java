// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch.util;

/**
 * cracksearch.util.Point
 *
 * Basic point class
 */
public class Point {

    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
        x = 0;
        y = 0;
    }

    public boolean equals(Point p) {
        return (p.x == x && p.y == y);
    }
}
