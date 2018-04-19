// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch;

import cracksearch.algorithm.Route;

/**
 * Simple listener interface. Called when a simulation has finished running.
 */
public interface SimulationFinishedListener {

    void onSimulationFinishedListener(Route r);
}
