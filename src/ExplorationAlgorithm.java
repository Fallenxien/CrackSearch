// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

import java.util.List;
import java.util.ListIterator;

/**
 * ExplorationAlgorithm
 *
 * Abstract class defining the template for each Exploration Algorithm implementation, (i.e. MST or Greedy)
 * Each implementation is required at minimum to extend calculateRoute and calculateRouteLength, based upon
 * the algorithm they are describing.
 */
public abstract class ExplorationAlgorithm {

    protected List<Crack> cracks;
    protected Route route;

    public ExplorationAlgorithm(List<Crack> cracks) {
        setCrackList(cracks);
    }

    public void setCrackList(List<Crack> cracks) {
        this.cracks = cracks;

        route = calculateRoute();
        calculateRouteLength();
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
    protected abstract Route calculateRoute();

    /**
     * Calculates the length of the given route. The default implementation of this method assumes
     * that the length is equal to the weight or length of the route returned by getRoute
     * @return length of route
     */
    protected double calculateRouteLength() {
        double length = 0;

        ListIterator<RouteLocation> i = route.getLocations();
        while (i.hasNext()) {
            length += i.next().getWeight();
        }

        return length;
    }

}
