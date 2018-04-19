// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch;

/**
 * onAlgorithmListUpdatedListener
 *
 * Called by cracksearch.frmAlgorithm to notify listeners the list of algorithms has changed
 */
public interface AlgorithmListUpdatedListener {
    void onAlgorithmListUpdatedListener(String[] names);
}
