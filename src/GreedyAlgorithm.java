// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

import util.Point;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * MSTAlgorithm
 *
 * Implementation of the Exploration following Greedy principles.
 *
 * Algorithm to create a route is as follows:
 *
 * 1) Navigate to closest unvisited crack
 * 2) Navigate along crack
 * 3) Mark crack as visited
 * 5) Repeat until all cracks are visited
 * 6) Navigate back to start position
 *
 */
public class GreedyAlgorithm extends ExplorationAlgorithm {

    public GreedyAlgorithm(List<Crack> cracks) {
        super(cracks);
    }

    @Override
    protected Route calculateRoute() {

        if (cracks.size() > 0) {

            LinkedList<Crack> cracks_copy = new LinkedList<>(cracks);

            // run algorithm
            return runGreedyAlgorithm(cracks_copy);
        }

        // no cracks, just return an empty route
        return new Route(0);
    }

    private Route runGreedyAlgorithm(List<Crack> cracks) {

        Route r = new Route(cracks.size() * 2 + 1);
        Point current_loc =  navigateToClosestCrackToOrigin(cracks, r);

        while (cracks.size() > 0) {
            current_loc = navigateToCosestCrack(cracks, current_loc, r);
        }

        // navigate back to origin
        r.addSegment(current_loc, new Point(0,0), calcDistanceFromOrigin(current_loc), RouteLocation.RouteType.TO_BASE);
        return r;
    }

    /**
     * Finds the closest crack to the origin, navigates to the crack and
     * along the crack before returning with the end point of the crack
     * that is furthest away from the origin. Also removes the crack from
     * the crack list (marking it as searched)
     * @param cracks list of cracks
     * @param r route
     * @return Point holding current location
     */
    private Point navigateToClosestCrackToOrigin(List<Crack> cracks, Route r) {

        ListIterator<Crack> i = cracks.listIterator();
        Crack c, min_c;
        double min_c_distance, c_start_distance, c_end_distance;
        boolean start_of_crack;

        // get first crack and store in min_c
        min_c = i.next();
        c_start_distance = calcDistanceFromOrigin(min_c.getStart());
        c_end_distance = calcDistanceFromOrigin(min_c.getEnd());
        // check if start/end is closer
        if (c_start_distance < c_end_distance) {
            // end is closer
            start_of_crack = true;
            min_c_distance = c_start_distance;
        } else {
            // start is closer
            start_of_crack = false;
            min_c_distance = c_end_distance;
        }

        // loop through all other cracks
        while (i.hasNext()) {
            c = i.next();
            c_start_distance = calcDistanceFromOrigin(c.getStart());
            c_end_distance = calcDistanceFromOrigin(c.getEnd());
            if (c_start_distance < min_c_distance) {
                min_c = c;
                min_c_distance = c_start_distance;
                start_of_crack = true;
            }
            if (c_end_distance < min_c_distance) {
                min_c = c;
                min_c_distance = c_end_distance;
                start_of_crack = false;
            }
        }

        // remove crack from the list, so we know it's been searched
        cracks.remove(min_c);

        // add route and return with current location
        // (current location is the opposite side of the crack closest to the origin
        //  as we move along the crack after getting to it)
        if (start_of_crack) {
            r.addSegment(new Point(0, 0), min_c.getStart(), min_c_distance, RouteLocation.RouteType.FROM_BASE);
            r.addSegment(min_c.getStart(), min_c.getEnd(), min_c.getLength(), RouteLocation.RouteType.CRACK);
            return min_c.getEnd();
        } else {
            r.addSegment(new Point(0, 0), min_c.getEnd(), min_c_distance, RouteLocation.RouteType.FROM_BASE);
            r.addSegment(min_c.getEnd(), min_c.getStart(), min_c.getLength(), RouteLocation.RouteType.CRACK);
            return min_c.getStart();
        }
    }

    /**
     * Finds the closest crack to the given point, navigates to the crack and
     * along the crack before returning with the end point of the crack
     * that is furthest away from the given point. Also removes the crack from
     * the crack list (marking it as searched)
     * @param cracks list of cracks
     * @param p Current location
     * @param r route
     */
    private Point navigateToCosestCrack(List<Crack> cracks, Point p, Route r) {

        ListIterator<Crack> i = cracks.listIterator();
        Crack c, min_c;
        double min_c_distance, c_start_distance, c_end_distance;
        boolean start_of_crack;

        // get first crack and store in min_c
        min_c = i.next();
        c_start_distance = calcDistanceBetween(min_c.getStart(), p);
        c_end_distance = calcDistanceBetween(min_c.getEnd(), p);
        // check if start/end is closer
        if (c_start_distance < c_end_distance) {
            start_of_crack = false;
            min_c_distance = c_end_distance;
        } else {
            start_of_crack = true;
            min_c_distance = c_start_distance;
        }

        // loop through all other cracks
        while (i.hasNext()) {
            c = i.next();
            c_start_distance = calcDistanceBetween(c.getStart(), p);
            c_end_distance = calcDistanceBetween(c.getEnd(), p);
            if (c_start_distance < min_c_distance) {
                min_c = c;
                min_c_distance = c_start_distance;
                start_of_crack = true;
            }
            if (c_end_distance < min_c_distance) {
                min_c = c;
                min_c_distance = c_end_distance;
                start_of_crack = false;
            }
        }

        // remove crack from the list, so we know it's been searched
        cracks.remove(min_c);

        // add route and return with current location
        if (start_of_crack) {
            r.addSegment(p, min_c.getStart(), min_c_distance, RouteLocation.RouteType.BETWEEN_CRACK);
            r.addSegment(min_c.getStart(), min_c.getEnd(), min_c.getLength(), RouteLocation.RouteType.CRACK);
            return min_c.getEnd();
        } else {
            r.addSegment(p, min_c.getEnd(), min_c_distance, RouteLocation.RouteType.BETWEEN_CRACK);
            r.addSegment(min_c.getEnd(), min_c.getStart(), min_c.getLength(), RouteLocation.RouteType.CRACK);
            return min_c.getStart();
        }

    }

    /**
     * Calculates the distance between two given points
     * @param p1 point 1
     * @param p2 point 2
     * @return Distance between points
     */
    private Double calcDistanceBetween(Point p1, Point p2) {
        double x,y;
        x = p1.x - p2.x;
        y = p1.y - p2.y;
        return Math.sqrt(x*x+y*y);
    }

    private Double calcDistanceFromOrigin(Point p) {
        return Math.sqrt(Math.pow(p.x,2) + Math.pow(p.y,2));
    }

    /**
     * Returns with algorithm name.
     * @return "Greedy"
     */
    @SuppressWarnings("SameReturnValue")
    public static String getAlgorithmName() {
        return "Greedy";
    }

}
