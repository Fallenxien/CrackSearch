// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

import java.util.List;
import java.util.ListIterator;

import util.*;
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
 *    3a) Add an edge to all other unconnected vertices in G (such that G becomes complete)
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

            // create MST tree T
            Graph t = runKruskals(g);

            // create route
            createRoute(t);

        }

    }

    /**
     * Perform kruskals algorithm on a graph to create a minimal spanning tree
     * @param g Graph to use to create Minimal Spanning Tree
     * @return Minimal Spanning Tree
     */
    public Graph runKruskals(Graph g) {

        Graph t = new Graph();
        ListIterator<Edge> i = g.getSortedEdgeIterator();
        Edge e;

        while (i.hasNext()) {
            e = i.next();
            if (!t.contains(e.getStart()) && !t.contains(e.getEnd())) {
                t.addVertex(e.getStart());
                t.addVertex(e.getEnd());
                t.addEdge(e);
            }
        }

        return t;
    }

    private void createRoute(Graph g) {

        Vertex v;
        Crack c;
        Edge e;
        Route r = new Route(2* (cracks.size() + g.getNumEdges() + 1)); // number of segments in route is 2*(num cracks + num edges + 1)
        // robot starts at origin, so need to find the vertex in G closest to origin
        v = findClosestToOrigin(g);
        c = findCrackByVertex(v);
        r.addSegment(new Point(0,0), v.getPoint(), RouteLocation.RouteType.FROM_BASE);

        // loop through tree adding nodes to route



    }

    /**
     * Finds the vertex that is closest to the origin for a given graph
     * @param g The graph to use
     * @return Vertex that is closest to the origin
     */
    private Vertex findClosestToOrigin(Graph g) {

        ListIterator<Vertex> verts = g.getVertexIterator();
        Vertex v, min_v;
        double min_v_distance, v_distance;

        min_v = verts.next();
        min_v_distance = calcDistanceFromOrigin(min_v);

        while (verts.hasNext()) {
            v = verts.next();
            v_distance = calcDistanceFromOrigin(v);
            if (v_distance < min_v_distance) {
                min_v = v;
                min_v_distance = v_distance;
            }
        }

        return min_v;

    }

    /**
     * Finds the crack via matching a vertex with the cracks starting point.
     * @param v Vertex to check with
     * @return Crack with starting point matching v.getPoint()
     */
    private Crack findCrackByVertex(Vertex v) {

        ListIterator<Crack> i = cracks.listIterator();
        Crack c;

        while (i.hasNext()) {
            c = i.next();
            if (v.getPoint() == c.getPoint(0)) {
                return c;
            }
        }

        return null;
    }

    /**
     * Calculates the distance from the origin of the given vertex.
     * @param v the vertex to get distance from origin
     * @return the distance from the origin
     */
    private double calcDistanceFromOrigin(Vertex v) {

        return Math.sqrt(Math.pow(v.getX(),2) + Math.pow(v.getY(),2));

    }

    @Override
    public double calculateRouteLength() {
        return 0;
    }

}
