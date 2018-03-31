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
 * CrackRouteSection
 *
 * Describes a route section along a crack
 */
public class CrackRouteSection extends RouteSection {

    private Crack crack;

    /**
     * Creates a crack route section
     * @param crack crack travelled along
     * @param startLocation start position on the crack
     * @param endLocation end position on the crack
     */
    public CrackRouteSection(Crack crack, Point startLocation, Point endLocation) {
        this.crack = crack;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.type = RouteType.CRACK;
    }

    @Override
    public double getWeight() {
        return crack.getLength();
    }

    /**
     * get the associated crack
     * @return crack
     */
    public Crack getCrack() {
        return crack;
    }

}
