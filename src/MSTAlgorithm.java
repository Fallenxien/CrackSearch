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

    /**
     * Calculates a route to navigate the cracks given via the constructor. The route created
     * is based upon finding a Minimal Spanning Tree.
     */
    @Override
    public Route calculateRoute() {

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
            return createRoute(t);

        }

        return new Route(0);
    }

    /**
     * Perform kruskals algorithm on a graph to create a minimal spanning tree
     * @param g Graph to use to create Minimal Spanning Tree
     * @return Minimal Spanning Tree
     */
    private Graph runKruskals(Graph g) {

        Graph t = new Graph();
        ListIterator<Edge> i = g.getSortedEdgeIterator();
        Edge e;

        while (i.hasNext()) {
            e = i.next();
            // check both verts are not already in graph
            if (!(t.contains(e.getStart()) && t.contains(e.getEnd()))) {
                // check which is missing and add it
                if (t.contains(e.getStart())) {
                    t.addVertex(e.getEnd());
                } else {
                    t.addVertex(e.getStart());
                }
                t.addEdge(e);
            }
        }

        return t;
    }

    /**
     * Creates the route for the robot to follow
     * @param t Tree containing Minimal Spanning tree of the cracks
     * @return Route finished route for robot to take
     */
    private Route createRoute(Graph t) {

        Vertex v;
        Route r = new Route(2* (cracks.size() + t.getNumEdges() + 1)); // number of segments in route is 2*(num cracks + num edges + 1)
        // robot starts at origin, so need to find the vertex in G closest to origin
        v = t.findClosestToOrigin();
        double distance_from_origin = Graph.calcDistanceFromOrigin(v);

        // add journey from base to tree
        r.addSegment(new Point(0,0), v.getPoint(), distance_from_origin, RouteLocation.RouteType.FROM_BASE);
        // loop through tree adding nodes to route
        runDFS(v, r);
        // add journey back to base
        r.addSegment(v.getPoint(), new Point(0,0), distance_from_origin, RouteLocation.RouteType.TO_BASE);

        return r;

    }

    /**
     * Runs a Depth First Search Algorithm to produce the route the robot will follow
     * @param v The current vertex the robot is at
     * @param r The route to write too
     */
    private void runDFS(Vertex v, Route r) {

        // 2x length of crack at v
        addCrackAtVertexToRoute(v,r);

        Vertex child;
        Edge e;
        ListIterator<Edge> i = v.getEdges();
        while (i.hasNext()) {
            e = i.next();
            child = Edge.getAssociatedVertex(e,v);
            r.addSegment(v.getPoint(), child.getPoint(), e.getWeight(), RouteLocation.RouteType.BETWEEN_CRACK); // add journey to next crack
            runDFS(child,r);
            r.addSegment(child.getPoint(), v.getPoint(), e.getWeight(), RouteLocation.RouteType.BETWEEN_CRACK); // add journey back
        }

    }

    /**
     * Finds the crack associated with vertex v, and adds it to route r twice
     * (once for each direction)
     * @param v Vertex of crack to add
     * @param r Route to add crack too
     */
    private void addCrackAtVertexToRoute(Vertex v, Route r) {

        // get crack details
        Crack c = findCrackByVertex(v);
        Point start = c.getPoint(0);
        Point end = c.getPoint(c.numPoints()-1);

        // add twice, once for each direction
        r.addSegment(start, end, c.getLength(), RouteLocation.RouteType.CRACK);
        r.addSegment(end, start, c.getLength(), RouteLocation.RouteType.CRACK);
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
     * Returns with algorithm name.
     * @return "MST"
     */
    @SuppressWarnings("SameReturnValue")
    public static String getAlgorithmName() {
        return "MST";
    }



}
