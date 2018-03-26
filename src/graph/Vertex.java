// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool


package graph;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import util.Point;

/**
 * Graph
 *
 * Contains methods and properties describing a vertex in a Graph
 */
public class Vertex {

    private final Point position;
    private HashMap<Vertex, Edge> connections;
    private List<Edge> relations;

    public Vertex(Point pos) {
        position = pos;
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    public Point getPoint() {
        return position;
    }

    public ListIterator<Edge> getEdges() {
        return relations.listIterator();
    }

    public void addEdge(Edge e) {
        if (!relations.contains(e)) {
            relations.add(e);
        }
    }

    public void removeEdge(Edge e) {
        relations.remove(e);
    }

}
