// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import cracksearch.util.Point;

/**
 * cracksearch.graph.Graph
 *
 * Contains methods and variables describing a Graph.
 *
 * Also contains static methods for common cracksearch.graph functionality.
 */
public class Graph {

    private final List<Vertex> verts;
    private final List<Edge> edges;

    public Graph() {
        verts = new LinkedList<>();
        edges = new LinkedList<>();
    }

    /**
     * Add a vertex to the graph
     * @param v Vertex to add
     */
    public void addVertex(Vertex v) {
        verts.add(v);
    }

    /**
     * Add a vertex to the graph with the location supplied in p.
     * @param p Location to add vertex
     */
    public void addVertex(Point p) {
        verts.add(new Vertex(p));
    }

    /**
     * Remove a vertex from the graph
     * @param v Vertex to remove
     */
    public void removeVertex(Vertex v) {
        verts.remove(v);
    }

    /**
     * Add an edge to the graph by giving the two vertexes to connect.
     * Weight of edge is given as the distance between the two vertices.
     * @param v1 First vertex on edge
     * @param v2 Second vertex on edge
     */
    public void addEdge(Vertex v1, Vertex v2) {
        double weight = calcDistance(v1, v2);
        Edge e = new Edge(v1, v2, weight);
        v1.addEdge(e);
        v2.addEdge(e);
        edges.add(e);
    }

    /**
     * Add an edge to the graph.
     * @param e Edge to add
     */
    public void addEdge(Edge e) {
        e.getStart().addEdge(e);
        e.getEnd().addEdge(e);
        edges.add(e);
    }

    /**
     * Remove an edge from the graph.
     * @param e Edge to remove
     */
    public void removeEdge(Edge e) {
        e.getStart().removeEdge(e);
        e.getEnd().removeEdge(e);
        edges.remove(e);
    }

    /**
     * Gets a ListIterator containing edges of the graph
     * @return Iterator over edges
     */
    public ListIterator<Edge> getEdgeIterator() {
        return edges.listIterator();
    }

    /**
     * Retrieves a sorted list iterator (edges sorted in ascending order)
     * @return Iterator over sorted edges
     */
    public ListIterator<Edge> getSortedEdgeIterator() {
        edges.sort(null);
        return edges.listIterator();
    }

    /**
     * Gets a ListIterator containing vertices of the graph
     * @return Iterator over vertices
     */
    public ListIterator<Vertex> getVertexIterator() {
        return verts.listIterator();
    }

    /**
     * Gets a ListIterator containing vertices of the graph from a given offset
     * @return Iterator over vertices
     */
    public ListIterator<Vertex> getVertexIterator(int offset) {
        return verts.listIterator(offset);
    }

    /**
     * Gets the number of vertices in the graph
     * @return number of vertices
     */
    public int getNumVertices() {
        return verts.size();
    }

    /**
     * Gets the number of edges in the graph
     * @return number of edges
     */
    public int getNumEdges() {
        return edges.size();
    }

    /**
     * Gets the vertex in the cracksearch.graph that matches the given point
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
     * Checks to see if a given vertex is in the cracksearch.graph
     * @param v Vertex to check
     * @return true if vertex is in cracksearch.graph, else false
     */
    public boolean contains(Vertex v) {
        return verts.contains(v);
    }

    /**
     * Checks to see if a given point is represented by any vertex in the cracksearch.graph
     * @param p Point to check
     * @return true if point is in cracksearch.graph, else false
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
     * Finds the vertex that is closest to the origin for a given cracksearch.graph
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
