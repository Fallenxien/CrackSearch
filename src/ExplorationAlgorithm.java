// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

import java.util.List;

/**
 * ExplorationAlgorithm
 *
 * Abstract class defining the template for each Exploration Algorithm implementation, (i.e. MST or Greedy)
 * Each implementation is required at minimum to extend calculateRoute and calculateRouteLength, based upon
 * the algorithm they are describing.
 */
public abstract class ExplorationAlgorithm {

    protected List<Crack> cracks;
    private Route route;

    public ExplorationAlgorithm(List<Crack> cracks) {
        setCrackList(cracks);
    }

    public void setCrackList(List<Crack> cracks) {
        this.cracks = cracks;

        calculateRoute();
    }

    /**
     * Returns the route calculated by the exploration algorithm
     * @return Route between cracks
     */
    public Route getRoute() {
        return route;
    }

    /**
     * Calculates a route to navigate the cracks given via the constructor. The route created
     * will depend upon the algorithm used by the implementation.
     */
    protected abstract void calculateRoute();

    /**
     * Calculates the length of the given route. The default implementation of this method assumes
     * that the length is equal to
     * @return the weight or length of the route returned by getRoute
     */
    protected abstract double calculateRouteLength();

}
