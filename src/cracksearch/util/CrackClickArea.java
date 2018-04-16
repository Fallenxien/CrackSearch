// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch.util;

/**
 * CrackClickArea
 *
 * Contains methods used for determining if a given point is inside the area
 * around the crack that allows it to be selected (as cracks are only a single
 * pixel, we want to make it easier to click on them)
 *
 * The click area is given to be a quadrilateral drawn around the crack, which is at most LEEWAY_PIXELS
 * pixels away from the crack at any given point.
 *
 * To create this area we first create a vector from the crack co-ordinates in the format
 * of a line equation (y = mx + b). We then shift b by LEEWAY_PIXELS pixels in either direction to create
 * a total of 3 parallel vectors. The crack vector can be discarded.
 *
 * We then plug in either the X or Y coordinate (dependant on the slope of the line) into the equations
 * to get the x,y co-ordinates for each corner of the polygon.
 */
public class CrackClickArea {

    private final static int MAX_COMPARED_SIDES = 2;
    private final static int LEEWAY_PIXELS = 15;
    private final static double LEEWAY_PERCENT = 0.1;

    private Line[] sides = new Line[MAX_COMPARED_SIDES];
    private int xMin;
    private int xMax;
    private int yMin;
    private int yMax;

    public CrackClickArea(Point p1, Point p2) {

        Line crack = new Line(p1,p2);
        double b_leeway = crack.b * LEEWAY_PERCENT;
        if (b_leeway < LEEWAY_PIXELS) {
            b_leeway = LEEWAY_PIXELS;
        }
        sides[0] = new Line (crack.m, crack.b + b_leeway);
        sides[1] = new Line (crack.m, crack.b - b_leeway);

        // setup min/max coords for the bounding crack
        if (p1.x < p2.x) {
            xMin = p1.x;
            xMax = p2.x;
        } else {
            xMin = p2.x;
            xMax = p1.x;
        }
        if (p1.y < p2.y) {
            yMin = p1.y;
            yMax = p2.y;
        } else {
            yMin =p2.y;
            yMax =p1.y;
        }

        xMin -= LEEWAY_PIXELS;
        xMax += LEEWAY_PIXELS;
        yMin -= LEEWAY_PIXELS;
        yMax += LEEWAY_PIXELS;
    }

    /**
     * Checks if a given point is inside the click area.
     * First step is to check the bounding square to see if the point
     * If the point is within the square then perform the ray casting algorithm
     * to check the exact geometry
     * @param p Point to check
     * @return True if p is inside the area, else false
     */
    public boolean contains(Point p) {

        if (checkBoundingSquare(p)) {
            // within bounding square, now perform ray cast
            return runRayCasting(p);
        } else {
            // not within bounding square, definitely not within the right area!
            return false;
        }
    }

    /**
     * Check if a given point is within the bounding square around the crack.
     * Bounding square is the square drawn around the start/end points on the crack.
     * @param p Point to check
     * @return true if inside square, else false
     */
    private boolean checkBoundingSquare(Point p) {
        return (p.x > xMin && p.x < xMax && p.y > yMin && p.y < yMax);
    }

    private boolean runRayCasting(Point p) {
        int intersections = 0;

        // we will use the two points p and (0,p.y) to create our 'ray'
        Point p2 = new Point(0,p.y);

        for (int i = 0; i < MAX_COMPARED_SIDES; i++) {

            if (sides[i].intersects(p, p2)) {
                intersections++;
            }

        }

        // as crack click areas are quadrilaterals with top and bottom sides parallel to x=0 (which is also parallel to
        // our ray) we know that if intersections != 1 then the p is not inside, and if intersections = 1 then p is inside
        // if the top/bottom sides where not parallel to x=0 then we would have to check if intersections was odd instead
        return intersections == 1;
    }


}
