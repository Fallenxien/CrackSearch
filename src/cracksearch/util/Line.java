// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch.util;

/**
 * Line
 *
 * Stores an object representing a Line.
 *
 * Lines are stored in the form y = mx + b. Where m represent the gradient of the slope and b is the y intercept
 */
public class Line {

    /**
     * Gradient of line
     */
    public double m;
    /**
     * Offset (y intercept) of line
     */
    public double b;

    /**
     * Creates a line from 2 given points
     * @param p1 first point
     * @param p2 second point
     */
    public Line(Point p1, Point p2) {

        // next calc m
        // m = (y2 - y1) / (x2 - x1)
        m = ((double)p2.y - (double)p1.y) / ((double)p2.x - (double)p1.x);

        // and b
        // b = y - mx
        b = (double)p1.y - m*(double)p1.x;

    }

    /**
     * Creates a line in the format y = mx + b from a specified m and b value
     * @param m m value to use
     * @param b b value to use
     */
    public Line(double m, double b) {
        this.m = m;
        this.b = b;
    }

    /**
     * Creates a line that passes through the point p with gradient m
     * @param p point line passes through
     * @param m gradient of line
     */
    public Line(Point p, double m) {
        this.m = m;
        // b = y - mx
        b = p.y - m * p.x;
    }

    /**
     * Gets the co ordinate of the x intercept (when y=0)
     * @return Point of x intercept
     */
    public Point xIntercept() {
        return new Point((int) ((-b) / m), 0);
    }

    /**
     * Checks to see if line between the two given points intersects the current line
     * Done by rearranging the line formula to the form mx - y + b = 0. If the two points
     * give results with different signs (i.e. 3 and -3) then the line must intersect at some
     * point
     * @param p1 First point on line to check
     * @param p2 Second point on line to check
     * @return true if intersects
     */
    public boolean intersects(Point p1, Point p2) {

        double result1, result2;

        // get result from rearranged formula
        result1 = m * p1.x - p1.y + b;
        result2 = m * p2.x - p2.y + b;

        //
        if ((result1 > 0 && result2 < 0) || (result1 < 0 && result2 > 0)) {
            return true;
        }

        // no intersection
        return false;
    }

}
