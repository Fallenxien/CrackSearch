// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch.algorithm;

import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import cracksearch.graph.*;
import cracksearch.world.Crack;

/**
 * MSTAlgorithm
 *
 * Implementation of the Exploration following the idea of using a minimal spanning tree
 * to navigate between cracks.
 *
 * Algorithm to create a route is as follows:
 *
 * First create a minimal spanning tree, following Kruskal's cracksearch.algorithm as a base:
 *
 * 1) Create an empty cracksearch.graph G
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
public class MSTAlgorithm implements ExplorationAlgorithm {

    private List<Crack> cracks;
    /**
     * Calculates a route to navigate the cracks given via the constructor. The route created
     * is based upon finding a Minimal Spanning Tree.
     */
    @Override
    public Route calculateRoute(List<Crack> cracks) {

        this.cracks = cracks;

        if (cracks.size() > 0) {

            Graph g = new Graph();

            // for every crack
            for (Crack crack : cracks) {
                // add the start position of each crack to cracksearch.graph g
                g.addVertex(crack.getPoint(0));
            }
            // then complete the cracksearch.graph
            completeGraph(g);
            // create MST tree T
            Graph t = runKruskals(g);
            // create route
            return createRoute(t);

        }

        return new Route(0);
    }

    /**
     * adds all edges to the current cracksearch.graph such that when the function finishes
     * the graph is a 'complete' graph.
     */
    public static void completeGraph(Graph g) {

        // make sure cracksearch.graph has at least 2 vertices
        if (g.getNumVertices() >=  2) {

            ListIterator<Vertex> i, j;      // two iterators to walk through list
            int counter;                    // counts progression through iterator
            Vertex v1, v2;                  // comparison placeholders

            i = g.getVertexIterator();
            counter = 1;
            // for every vertex in list, connect it to every other vertex
            while (i.hasNext()) {
                v1 = i.next();
                j = g.getVertexIterator(counter);
                while (j.hasNext()) {
                    v2 = j.next();
                    g.addEdge(v1, v2);
                }
                counter++;
            }
        }
    }

    /**
     * Perform kruskals cracksearch.algorithm on a cracksearch.graph to create a minimal spanning tree
     * @param g Graph to use to create Minimal Spanning Tree
     * @return Minimal Spanning Tree
     */
    private Graph runKruskals(Graph g) {

        Graph t = new Graph();

        if (g.getNumVertices() < 2) {
            // only 1 vertex in g, so only 1 vertex (0 edges) in t
            t.addVertex(g.getVertexIterator().next());
            return t;
        } else {
            // at least 2 verts (therefore, 1 edge)
            ListIterator<Edge> i = g.getSortedEdgeIterator();
            Edge e;
            Vertex v1,v2;

            // add first edge to tree
            e = i.next();
            // clone objects before adding
            v1 = new Vertex(e.getStart().getPoint());
            v2 = new Vertex(e.getEnd().getPoint());
            t.addVertex(v1);
            t.addVertex(v2);
            t.addEdge(new Edge(v1,v2,e.getWeight()));

            // add rest of edges if no cycle
            while (i.hasNext()) {
                e = i.next();

                // first check if both points are present in the cracksearch.graph,
                // if at least 1 is missing then there can not be a cycle
                if (!(t.contains(e.getStart().getPoint()) && t.contains((e.getEnd().getPoint())))) {
                    // at least 1 vertex is missing, find and add it to the cracksearch.graph and add edge
                    if (t.contains(e.getStart().getPoint())) {
                        // only end is missing
                        v1 = t.getVertex(e.getStart().getPoint());
                        v2 = new Vertex(e.getEnd().getPoint());
                        t.addVertex(v2);
                    } else {
                        // start is missing
                        v1 = new Vertex(e.getStart().getPoint());
                        t.addVertex(v1);
                        // next check if end is missing
                        if (!t.contains(e.getEnd().getPoint())) {
                            v2 = new Vertex(e.getEnd().getPoint());         // end is missing
                            t.addVertex(v2);
                        } else {
                            v2 = t.getVertex(e.getEnd().getPoint());        // end is present
                        }
                    }
                    t.addEdge(new Edge(v1,v2,e.getWeight()));
                } else {
                    if (!checkForCycles(t, e)) {
                        // no cycles, get the correct vertices for the cloned cracksearch.graph and add this edge to cracksearch.graph
                        v1 = t.getVertex(e.getStart().getPoint());
                        v2 = t.getVertex(e.getEnd().getPoint());
                        t.addEdge(new Edge(v1, v2, e.getWeight()));
                    }
                }
            }
        }

        return t;
    }

    /**
     * Checks if a cracksearch.graph will have cycles when the edge e is added.
     * @param g Graph to check
     * @param e Edge to check
     * @return true if cycles exist, otherwise false
     */
    private boolean checkForCycles(Graph g, Edge e) {

        Stack<Vertex> visted_verts = new Stack<>();
        // mark end node of new edge as visited
        // perform DFS on start node to look for end node
        visted_verts.push(g.getVertex(e.getEnd().getPoint()));

        return checkForCyclesUtil(g.getVertex(e.getStart().getPoint()),visted_verts, null);
    }

    /**
     * Checks for cycles in a cracksearch.graph by recursively performing a DFS on the given vertex.
     * @param v Vertex to search from
     * @param visited stack containing visited nodes
     * @param parent Parent node, used to make sure we arent trying to walk back to parent
     * @return true if cracksearch.graph contains cycles
     */
    private boolean checkForCyclesUtil(Vertex v, Stack<Vertex> visited, Vertex parent) {

        // mark current node as visited
        if (!visited.contains(v)) {
            visited.push(v);
        } else {
            // node is visited already, cycle exists
            return true;
        }

        // check if child nodes are visited
        ListIterator<Edge> i = v.getEdges();
        Edge e;
        Vertex child;
        while (i.hasNext()) {
            e = i.next();
            child = Edge.getAssociatedVertex(e,v);
            if (child != parent && checkForCyclesUtil(child,visited, v)) {
                    return true;
            }
        }

        return false;
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
        Crack crack = findCrackByVertex(v);
        double distance_from_origin = Graph.calcDistanceFromOrigin(v);

        // add journey from base to tree
        r.addFromBaseSegment(crack, v.getPoint(), distance_from_origin);
        // loop through tree adding nodes to route
        runDFS(v, crack, r, null);
        // add journey back to base
        r.addToBaseSegment(crack, v.getPoint(), distance_from_origin);
        return r;

    }

    /**
     * Runs a Depth First Search Algorithm to produce the route the robot will follow
     * @param v The current vertex the robot is at
     * @param c Crack matching vertex v
     * @param r The route to write too
     * @param parent Parent vertex of v
     */
    private void runDFS(Vertex v, Crack c, Route r, Vertex parent) {

        addCrackToRoute(c,r);

        Vertex child;
        Edge e;
        Crack child_crack;
        ListIterator<Edge> i = v.getEdges();
        while (i.hasNext()) {
            e = i.next();
            child = Edge.getAssociatedVertex(e,v);
            child_crack = findCrackByVertex(child);
            if (child != parent) {
                r.addBetweenCrackSegment(c, child_crack, v.getPoint(), child.getPoint(), e.getWeight());
                runDFS(child, child_crack, r, v);
                r.addBetweenCrackSegment(child_crack, c, child.getPoint(), v.getPoint(), e.getWeight());
            }
        }
    }

    /**
     * Adds a crack to the route r twice
     * (once for each direction)
     * @param c Crack to add
     * @param r Route to add crack too
     */
    private void addCrackToRoute(Crack c, Route r) {
        // add twice, once for each direction
        r.addCrackSegment(c, c.getStart(), c.getEnd());
        r.addCrackSegment(c, c.getEnd(), c.getStart());
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
     * Returns with cracksearch.algorithm name.
     * @return "MST"
     */
    @SuppressWarnings("SameReturnValue")
    public static String getAlgorithmName() {
        return "MST";
    }



}
