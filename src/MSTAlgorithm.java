// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

import java.util.List;
import graph.*;

/**
 * MSTAlgorithm
 *
 * Implementation of the Exploration following the idea of using a minimal spanning tree
 * to navigate between cracks.
 *
 * Algorithm to create a route is as follows:
 *
 * First create a minimal spanning tree, following Kruskal's algorithm as a base:
 *
 * 1) Create an empty graph G
 * 2) For each crack in the world
 *    2a) Add start point to G as a vertex
 * 3) For each vertex in G
 *    3a) Add an edge to all other unconnected vertices in G
 * 4) Create empty Tree T
 * 5) Add the smallest edge in G to T as long as it does not form a cycle when added to T
 * 6) Repeat until all vertices in G are also in T
 *
 * Once we have the minimal spanning tree T, we can traverse the tree using DFS, stopping at each
 * vertex in T to navigate along the edge and then back again. Once we have finished travelling along
 * the tree we return to the start position
 */
public class MSTAlgorithm extends ExplorationAlgorithm {

    public MSTAlgorithm(List<Crack> cracks) {
        super(cracks);


    }

    @Override
    public void calculateRoute() {

        if (cracks.size() > 0) {

            Graph g = new Graph();

            // for every crack
            for (Crack crack : cracks) {

                // add the start position of each crack to graph g
                g.addVertex(crack.getPoint(0));
            }

            // then complete the graph
            Graph.completeGraph(g);

        }

    }

    public void Kruskals() {



    }

    @Override
    public double calculateRouteLength() {
        return 0;
    }

}
