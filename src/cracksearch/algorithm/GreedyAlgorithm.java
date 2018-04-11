// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch.algorithm;

import cracksearch.util.Point;
import cracksearch.world.Crack;

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

    /**
     * Calculates a route to navigate the cracks given via the constructor.
     */
    @Override
    protected Route calculateRoute() {

        if (cracks.size() > 0) {

            LinkedList<Crack> cracks_copy = new LinkedList<>(cracks);

            // run cracksearch.algorithm
            return runGreedyAlgorithm(cracks_copy);
        }

        // no cracks, just return an empty route
        return new Route(0);
    }

    /**
     * Implements the Greedy route selection algorithm. Algorithm is outlined in class description.
     */
    private Route runGreedyAlgorithm(List<Crack> cracks) {

        Route r = new Route(cracks.size() * 2 + 1);
        CrackRouteSection last_section =  navigateToClosestCrackToOrigin(cracks, r);

        while (cracks.size() > 0) {
            last_section = navigateToClosestCrack(cracks, last_section, r);
        }

        // navigate back to origin
        r.addToBaseSegment(last_section.getCrack(), last_section.getEndLocation(), calcDistanceFromOrigin(last_section.getEndLocation()));
        return r;
    }

    /**
     * Finds the closest crack to the origin, navigates to the crack and
     * along the crack before returning with the end point of the crack
     * that is furthest away from the origin. Also removes the crack from
     * the crack list (marking it as searched)
     * @param cracks list of cracks
     * @param r route
     * @return CrackRouteSection containing last route section added to route
     */
    private CrackRouteSection navigateToClosestCrackToOrigin(List<Crack> cracks, Route r) {

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
            r.addFromBaseSegment(min_c, min_c.getStart(), min_c_distance);
            return r.addCrackSegment(min_c, min_c.getStart(), min_c.getEnd());
        } else {
            r.addFromBaseSegment(min_c, min_c.getEnd(), min_c_distance);
            return r.addCrackSegment(min_c, min_c.getEnd(), min_c.getStart());
        }
    }

    /**
     * Finds the closest crack to the given point, navigates to the crack and
     * along the crack before returning with the end point of the crack
     * that is furthest away from the given point. Also removes the crack from
     * the crack list (marking it as searched)
     * @param cracks list of cracks
     * @param lastSection Current location
     * @param r route
     * @return CrackRouteSection containing last route section added to route
     */
    private CrackRouteSection navigateToClosestCrack(List<Crack> cracks, CrackRouteSection lastSection, Route r) {

        ListIterator<Crack> i = cracks.listIterator();
        Crack c, min_c;
        double min_c_distance, c_start_distance, c_end_distance;
        boolean start_of_crack;

        // get first crack and store in min_c
        min_c = i.next();
        c_start_distance = calcDistanceBetween(min_c.getStart(), lastSection.getEndLocation());
        c_end_distance = calcDistanceBetween(min_c.getEnd(), lastSection.getEndLocation());
        // check if start/end is closer
        if (c_start_distance < c_end_distance) {
            start_of_crack = true;
            min_c_distance = c_start_distance;
        } else {
            start_of_crack = false;
            min_c_distance = c_end_distance;
        }

        // loop through all other cracks
        while (i.hasNext()) {
            c = i.next();
            c_start_distance = calcDistanceBetween(c.getStart(), lastSection.getEndLocation());
            c_end_distance = calcDistanceBetween(c.getEnd(), lastSection.getEndLocation());
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
            r.addBetweenCrackSegment(lastSection.getCrack(), min_c, lastSection.getEndLocation(), min_c.getStart(), min_c_distance);
            return r.addCrackSegment(min_c, min_c.getStart(), min_c.getEnd());
        } else {
            r.addBetweenCrackSegment(lastSection.getCrack(), min_c, lastSection.getEndLocation(), min_c.getEnd(), min_c_distance);
            return r.addCrackSegment(min_c, min_c.getEnd(), min_c.getStart());
        }

    }

    /**
     * Calculates the distance between two given points
     * @param p1 point 1
     * @param p2 point 2
     * @return Distance between points
     */
    private double calcDistanceBetween(Point p1, Point p2) {
        double x,y;
        x = p1.x - p2.x;
        y = p1.y - p2.y;
        return Math.sqrt(x*x+y*y);
    }

    /**
     * Calculate the distance from the origin of a given point
     * @param p Point to calculate distance from origin
     * @return distance from origin
     */
    private double calcDistanceFromOrigin(Point p) {
        return Math.sqrt(Math.pow(p.x,2) + Math.pow(p.y,2));
    }

    /**
     * Returns with cracksearch.algorithm name.
     * @return "Greedy"
     */
    @SuppressWarnings("SameReturnValue")
    public static String getAlgorithmName() {
        return "Greedy";
    }

}
