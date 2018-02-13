package graph;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Andy on 13/02/2018.
 */
public class Graph {

    List<Vertex> verts;

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

    }

    /**
     * adds all edges to the current graph such that when the function finishes
     * the graph is a 'complete' graph.
     */
    public void completeGraph() {

        // make sure graph has at least 2 verticies
        if (verts.size() >=  2) {

            ListIterator<Vertex> i, j;      // two iterators to walk through list
            int counter;                    // counts progression through iterator
            Vertex v1, v2;                  // comparison placeholders

            i = verts.listIterator();
            counter = 1;
            // for every vertex in list, connect it to every other vertex
            while (i.hasNext()) {
                v1 = i.next();
                j = verts.listIterator(counter);
                while (j.hasNext()) {
                    v2 = j.next();
                    addEdge(v1, v2);
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
