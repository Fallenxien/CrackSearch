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

    private List<Vertex> verts;
    private List<Edge> edges;

    public Graph() {
        verts = new LinkedList<>();
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
        v1.addEdge(v2, e);
        v2.addEdge(v1, e);
        edges.add(e);
    }

    public void addEdge(Edge e) {
        e.getStart().addEdge(e.getEnd(), e);
        e.getEnd().addEdge(e.getStart(), e);
        edges.add(e);
    }

    public void removeEdge(Edge e) {
        e.getStart().removeEdge(e.getEnd(), e);
        e.getEnd().removeEdge(e.getStart(), e);
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

    public boolean contains(Vertex v) {
        return verts.contains(v);
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


}
