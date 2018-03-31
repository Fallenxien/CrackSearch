// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch.algorithm;

import cracksearch.util.Point;
import cracksearch.world.Crack;

/**
 * BetweenBaseRouteSection
 *
 * Describes a route section between the base (or origin) and a crack
 */
public class BetweenBaseRouteSection extends RouteSection {

    private Crack crack;

    /**
     * Creates a between base route section
     * @param crack crack we navigate to or from
     * @param crackPoint point on crack we navigate to or from
     * @param weight weight/length of segment
     * @param toBase flag indicating direction of travel (to or from base)
     */
    public BetweenBaseRouteSection(Crack crack, Point crackPoint, double weight, boolean toBase) {
        this.crack = crack;
        this.weight = weight;
        if (toBase) {
            this.startLocation = crackPoint;
            this.endLocation = new Point(0,0);
            this.type = RouteType.TO_BASE;
        } else {
            this.startLocation  = new Point(0,0);
            this.endLocation= crackPoint;
            this.type = RouteType.FROM_BASE;
        }
    }

    /**
     * Returns the crack associated with this section
     * @return Crack
     */
    public Crack getCrack() {
        return crack;
    }

    /**
     * Returns the point on the crack the route naviagates to or from.
     * @return Point on crack
     */
    public Point getCrackPoint() {
        if (type == RouteType.FROM_BASE) {
            return endLocation;
        } else {
            return startLocation;
        }
    }


}
