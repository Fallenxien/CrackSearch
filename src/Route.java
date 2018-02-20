// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

import util.Point;

import java.util.ArrayList;
import java.util.List;

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

    public void addSegment(Point start, Point end, RouteLocation.RouteType type) {
        route.add(new RouteLocation(start, end, type));
    }


}
