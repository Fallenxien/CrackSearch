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
 * Abstract class for describing sections of routes
 */
public abstract class RouteSection {

    /**
     * Details the ground that the robot is travelling over, i.e. is it currently looking at a crack
     * or travelling between cracks. Mainly used for statistical data
     */
    public enum RouteType {
        FROM_BASE, CRACK, BETWEEN_CRACK, TO_BASE
    }

    protected Point startLocation;
    protected Point endLocation;
    protected RouteType type;
    protected double weight;

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
    public double getWeight() {
        return weight;
    }

    /**
     * Gets the start location of this section
     * @return Point containing start location
     */
    public Point getStartLocation() {
        return startLocation;
    }

    /**
     * Gets the end location of this section
     * @return Point containing end location
     */
    public Point getEndLocation() {
        return endLocation;
    }

}
