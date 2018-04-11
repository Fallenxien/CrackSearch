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
public abstract class ExplorationAlgorithm {

    protected List<Crack> cracks;

    /**
     * Required constructor for implementations to work as intended. Setups the mnodular level crack list.
     * @param cracks List of cracks to explore
     */
    protected ExplorationAlgorithm(List<Crack> cracks) {
        setCrackList(cracks);
    }

    /**
     * Sets the crack list
     * @param cracks List to set
     */
    private void setCrackList(List<Crack> cracks) {
        this.cracks = cracks;
    }

    /**
     * Returns the route calculated by the exploration cracksearch.algorithm
     * @return Route between cracks
     */
    public Route getRoute() {
        return calculateRoute();
    }

    /**
     * Calculates a route to navigate the cracks given via the constructor. The route created
     * will depend upon the cracksearch.algorithm used by the implementation.
     */
    protected abstract Route calculateRoute();

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
