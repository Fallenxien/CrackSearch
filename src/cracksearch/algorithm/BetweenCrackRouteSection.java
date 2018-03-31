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
 * BetweenCrackRouteSection
 *
 * Describes a route section between two cracks
 */
public class BetweenCrackRouteSection extends RouteSection {

    private Crack startCrack;
    private Crack endCrack;

    /**
     * Creates a between crack route section
     * @param startCrack crack we start from
     * @param endCrack crack we end at
     * @param startLocation point on startCrack we start from
     * @param endLocation point on endCrack we end at
     * @param weight weight/length of section
     */
    public BetweenCrackRouteSection(Crack startCrack, Crack endCrack, Point startLocation, Point endLocation, double weight) {
        this.startCrack = startCrack;
        this.endCrack = endCrack;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.weight = weight;
        this.type = RouteType.BETWEEN_CRACK;
    }

    /**
     * get the starting crack
     * @return start crack
     */
    public Crack getStartCrack() {
        return startCrack;
    }

    /**
     * get the ending crack
     * @return end crack
     */
    public Crack getEndCrack() {
        return endCrack;
    }

}
