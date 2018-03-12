// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

import util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Route
 *
 * Contains methods and variables for storing the route taken by a robot
 */
public class Route {

    private List<RouteLocation> route;
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
    public void addSegment(Point start, Point end, Double weight, RouteLocation.RouteType type) {
        route.add(new RouteLocation(start, end, weight, type));
        numLocations++;
    }

    /**
     * Returns a list iterator with all locations in the route
     * @return ListIterator<RouteLocation> list of locations
     */
    public ListIterator<RouteLocation> getLocations() {
        return route.listIterator();
    }

    /**
     * Returns number of locations in the list
     * @return
     */
    public int getSize() {
        return numLocations;
    }


}
