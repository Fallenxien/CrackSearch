package graph;

import java.util.HashMap;
import util.Point;

/**
 * Created by Andy on 13/02/2018.
 */
public class Vertex {

    Point position;
    HashMap<Vertex, Edge> connections;

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
