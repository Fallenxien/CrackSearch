// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool


package graph;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import util.Point;

/**
 * graph.Graph
 *
 * Contains methods and variables describing a Graph.
 *
 * Also contains static methods for common graph functionality.
 */
public class Graph {

    private final List<Vertex> verts;
    private List<Edge> edges;

    public Graph() {
        verts = new LinkedList<>();
        edges = new LinkedList<>();
    }

    public Graph(List<Vertex> vertices) {
        verts = vertices;
    }

    public void addVertex(Vertex v) {
        verts.add(v);
    }

    public void addVertex(Point p) {
        verts.add(new Vertex(p));
    }

    public void removeVertex(Vertex v) {
        verts.remove(v);
    }

    public void addEdge(Vertex v1, Vertex v2) {
        double weight = calcDistance(v1, v2);
        Edge e = new Edge(v1, v2, weight);
        v1.addEdge(e);
        v2.addEdge(e);
        edges.add(e);
    }

    public void addEdge(Edge e) {

        e.getStart().addEdge(e);
        e.getEnd().addEdge(e);
        edges.add(e);
    }

    public void removeEdge(Edge e) {
        e.getStart().removeEdge(e);
        e.getEnd().removeEdge(e);
        edges.remove(e);
    }

    public ListIterator<Edge> getEdgeIterator() {
        return edges.listIterator();
    }

    public ListIterator<Edge> getSortedEdgeIterator() {
        edges.sort(null);
        return edges.listIterator();
    }

    public ListIterator<Vertex> getVertexIterator() {
        return verts.listIterator();
    }

    public int getNumVertices() {
        return verts.size();
    }

    public int getNumEdges() {
        return edges.size();
    }

    /**
     * Gets the vertex in the graph that matches the given point
     * @param p Point to search for
     * @return Vertex matching p. If no vertex matches returns null
     */
    public Vertex getVertex(Point p) {
        for (Vertex v: verts) {
            if (v.getPoint().equals(p)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Checks to see if a given vertex is in the graph
     * @param v Vertex to check
     * @return true if vertex is in graph, else false
     */
    public boolean contains(Vertex v) {
        return verts.contains(v);
    }

    /**
     * Checks to see if a given point is represented by any vertex in the graph
     * @param p Point to check
     * @return true if point is in graph, else false
     */
    public boolean contains(Point p) {
        for (Vertex v: verts) {
            if (v.getPoint().equals(p)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * adds all edges to the current graph such that when the function finishes
     * the graph is a 'complete' graph.
     */
    public static void completeGraph(Graph g) {

        // make sure graph has at least 2 vertices
        if (g.getNumVertices() >=  2) {

            ListIterator<Vertex> i, j;      // two iterators to walk through list
            int counter;                    // counts progression through iterator
            Vertex v1, v2;                  // comparison placeholders

            i = g.verts.listIterator();
            counter = 1;
            // for every vertex in list, connect it to every other vertex
            while (i.hasNext()) {
                v1 = i.next();
                j = g.verts.listIterator(counter);
                while (j.hasNext()) {
                    v2 = j.next();
                    g.addEdge(v1, v2);
                }
                counter++;
            }
        }
    }

    /**
     * Calculates the euclidean distance between the given vertices
     * @param v1 first vertex to measure
     * @param v2 second vertex to measure
     */
    private double calcDistance(Vertex v1, Vertex v2) {

        double xdiff, ydiff;

        xdiff = Math.pow(v1.getX() - v2.getX(), 2);
        ydiff = Math.pow(v1.getY() - v2.getY(), 2);
        return Math.sqrt(xdiff + ydiff);
    }

    /**
     * Finds the vertex that is closest to the origin for a given graph
     * @return Vertex that is closest to the origin
     */
    public Vertex findClosestToOrigin() {

        ListIterator<Vertex> nodes = getVertexIterator();
        Vertex v, min_v;
        double min_v_distance, v_distance;

        min_v = nodes.next();
        min_v_distance = calcDistanceFromOrigin(min_v);

        while (nodes.hasNext()) {
            v = nodes.next();
            v_distance = calcDistanceFromOrigin(v);
            if (v_distance < min_v_distance) {
                min_v = v;
                min_v_distance = v_distance;
            }
        }

        return min_v;

    }

    /**
     * Calculates the distance from the origin of the given vertex.
     * @param v the vertex to get distance from origin
     * @return the distance from the origin
     */
    public static double calcDistanceFromOrigin(Vertex v) {

        return Math.sqrt(Math.pow(v.getX(),2) + Math.pow(v.getY(),2));

    }

}
