// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool


package graph;

import java.util.HashMap;
import util.Point;

/**
 *
 */
public class Vertex {

    private Point position;
    private HashMap<Vertex, Edge> connections;

    public Vertex(Point pos) {
        position = pos;
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    public void addEdge(Vertex target, Edge e) {
        if (!connections.containsKey(target)) {
            connections.put(target, e);
        }
    }

    public void removeEdge(Vertex target, Edge e) {
            connections.remove(target, e);
    }

}
