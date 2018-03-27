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
@SuppressWarnings("ALL")
public class RouteLocation {

    /**
     * Details the ground that the robot is travelling over, i.e. is it currently looking at a crack
     * or travelling between cracks. Mainly used for statistical data
     */
    public enum RouteType {
        FROM_BASE, CRACK, BETWEEN_CRACK, TO_BASE
    }

    private Point startLocation;
    private Point endLocation;
    private RouteType type;
    private Double weight;

    /**
     * Get the type for the current route section
     * @return RouteType
     */
    public RouteType getType() {
        return type;
    }

    /**
     * Returns the weigh associated with the Route Location
     * @return weight as a double
     */
    public Double getWeight() {
        return weight;
    }

    public Point getStart() {
        return startLocation;
    }

    public Point getEnd() {
        return endLocation;
    }


    public RouteLocation(Point start, Point end, Double weight, RouteType type) {
        this.startLocation = start;
        this.endLocation = end;
        this.weight = weight;
        this.type = type;
    }




}
