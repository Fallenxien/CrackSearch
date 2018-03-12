// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

import util.Point;

/**
 * RouteLocation
 *
 * Contains methods and variables describing a Graph.
 *
 * Also contains static methods for common graph functionality.
 */
public class RouteLocation {

    /**
     * Details the ground that the robot is travelling over, i.e. is it currently looking at a crack
     * or travelling between cracks. Mainly used for statistical data
     */
    public enum RouteType {
        FROM_BASE, CRACK, BETWEEN_CRACK, TO_BASE
    }

    private Point start_location;
    private Point end_location;
    private RouteType type;
    private Double weight;

    public RouteLocation(Point start, Point end, Double weight, RouteType type) {
        this.start_location = start;
        this.end_location = end;
        this.weight = weight;
        this.type = type;
    }

}
