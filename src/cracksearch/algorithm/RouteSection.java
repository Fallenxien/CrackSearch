// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch.algorithm;

import cracksearch.util.Point;

/**
 * RouteSection
 *
 * Contains methods and variables describing a Graph.
 *
 * Also contains static methods for common cracksearch.graph functionality.
 */
@SuppressWarnings("ALL")
public class RouteSection {

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

    /**
     * Gets the start location of this section
     * @return Point containing start location
     */
    public Point getStart() {
        return startLocation;
    }

    /**
     * Gets the end location of this section
     * @return Point containing end location
     */
    public Point getEnd() {
        return endLocation;
    }

    public RouteSection(Point start, Point end, Double weight, RouteType type) {
        this.startLocation = start;
        this.endLocation = end;
        this.weight = weight;
        this.type = type;
    }




}
