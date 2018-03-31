// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch.algorithm;

import cracksearch.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Route
 *
 * Contains methods and variables for storing the route taken by a robot
 */
public class Route {

    private final List<RouteSection> route;
    private int numLocations;
    private int currentIndex;

    public Route(int max_locations) {

        route = new ArrayList<>(max_locations);
        currentIndex = 0;
        numLocations = 0;

    }

    /**
     * Adds a segment to the route.
     * @param start Start of the segment
     * @param end End of the segment
     * @param weight Weight of the segment
     * @param type Type of segment
     */
    public void addSegment(Point start, Point end, Double weight, RouteSection.RouteType type) {
        route.add(new RouteSection(start, end, weight, type));
        numLocations++;
    }

    /**
     * Returns a list iterator with all locations in the route
     * @return ListIterator<RouteSection> list of locations
     */
    public ListIterator<RouteSection> getLocations() {
        return route.listIterator();
    }

    /**
     * Returns number of locations in the list
     * @return Size as int
     */
    public int getSize() {
        return numLocations;
    }

    /**
     * Calculates the total length of the route
     * @return total length of route
     */
    public double getTotalLength() {
        double length = 0;

        for (RouteSection step : route) {
            length += step.getWeight();
        }

        return length;
    }

    /**
     * Calculates the total length of the route
     * @return total length of route
     */
    public double getTotalCrackLength() {
        double length = 0;

        for (RouteSection rl : route) {
            if (rl.getType() == RouteSection.RouteType.CRACK)
                length += rl.getWeight();
        }

        return length;
    }

    /**
     * Gets the total crack length walked by the route
     * @return total crack length
     */
    public double getTotalBetweenCrackLength() {
        double length = 0;

        for (RouteSection rl : route) {
            if (rl.getType() == RouteSection.RouteType.BETWEEN_CRACK)
                length += rl.getWeight();
        }

        return length;
    }

    /**
     * Gets the total length between the base and the start/end nodes
     * @return total between base length
     */
    public double getTotalBetweenBaseLength() {
        double length = 0;

        for (RouteSection rl : route) {
            if (rl.getType() == RouteSection.RouteType.TO_BASE || rl.getType() == RouteSection.RouteType.FROM_BASE)
                length += rl.getWeight();
        }

        return length;
    }

    /**
     * Gets the total intermediate length walked by the route (Between cracks and to/from base)
     * @return total intermediate length
     */
    public double getTotalIntermediateLength() {
        return getTotalBetweenBaseLength() + getTotalBetweenCrackLength();
    }




}
