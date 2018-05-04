// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch.algorithm;

import cracksearch.world.Crack;

import java.util.List;

/**
 * ExplorationAlgorithm
 *
 * Abstract class defining the template for each Exploration Algorithm implementation, (i.e. MST or Greedy)
 * Each implementation is required at minimum to extend calculateRoute and calculateRouteLength, based upon
 * the cracksearch.algorithm they are describing.
 */
public interface ExplorationAlgorithm {



    public Route calculateRoute(List<Crack> cracks);

    /**
     * Should be overridden by child class to return with the name
     * of their implementation.
     * @return returns the name of the cracksearch.algorithm
     */
    @SuppressWarnings("SameReturnValue")
    public static String getAlgorithmName() {
        return "Exploration Algorithm";
    }

}
