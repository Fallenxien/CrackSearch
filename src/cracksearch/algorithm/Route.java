// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch.algorithm;

import cracksearch.util.Point;
import cracksearch.world.Crack;

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

    public Route(int max_locations) {

        route = new ArrayList<>(max_locations);
        numLocations = 0;

    }

    /**
     * Adds a crack segment to the route.
     * @param crack associated with segment
     * @param start Start point on the crack of the segment
     * @param end End point on the crack of the segment
     * @return Created route section
     */
    public CrackRouteSection addCrackSegment(Crack crack, Point start, Point end) {
        CrackRouteSection section = new CrackRouteSection(crack, start, end);
        route.add(section);
        numLocations++;

        return section;
    }

    /**
     * Adds a between crack segment to the route.
     * @param startCrack start crack
     * @param endCrack end crack
     * @param start Start point on the crack of the segment
     * @param end End point on the crack of the segment
     * @param weight weight of segment
     * @return Created route section
     */
    public BetweenCrackRouteSection addBetweenCrackSegment(Crack startCrack, Crack endCrack, Point start, Point end, double weight) {
        BetweenCrackRouteSection section = new BetweenCrackRouteSection(startCrack, endCrack, start, end, weight);
        route.add(section);
        numLocations++;

        return section;
    }

    /**
     * Adds a from base segment to the route.
     * @param crack associated with segment
     * @param crackPos position on crack we end at
     * @return Created route section
     */
    public BetweenBaseRouteSection addFromBaseSegment(Crack crack, Point crackPos, double weight) {
        BetweenBaseRouteSection section = new BetweenBaseRouteSection(crack, crackPos, weight, false);
        route.add(section);
        numLocations++;

        return section;
    }

    /**
     * Adds a to base segment to the route.
     * @param crack associated with segment
     * @param crackPos position on crack we start from
     * @return Created route section
     */
    public BetweenBaseRouteSection addToBaseSegment(Crack crack, Point crackPos, double weight) {
        BetweenBaseRouteSection section = new BetweenBaseRouteSection(crack, crackPos, weight, true);
        route.add(section);
        numLocations++;

        return section;
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
