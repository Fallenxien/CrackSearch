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
 * Graph
 *
 * Contains methods and properties describing a vertex in a Graph
 */
public class Vertex {

    private final Point position;
    private List<Edge> relations;

    public Vertex(Point pos) {
        position = pos;
        relations = new LinkedList<>();
    }

    /**
     * Gets the X position of the vertex
     * @return X position
     */
    public int getX() {
        return position.x;
    }

    /**
     * Gets the X position of the vertex
     * @return Y position
     */
    public int getY() {
        return position.y;
    }

    /**
     * Gets the position of the vertex
     * @return Point containing position
     */
    public Point getPoint() {
        return position;
    }

    /**
     * Gets an iterator containing the edges connected to the vertex
     * @return Iterator of connected edges
     */
    public ListIterator<Edge> getEdges() {
        return relations.listIterator();
    }

    /**
     * Adds an edge to the vertex
     * @param e edge to add to vertex
     */
    public void addEdge(Edge e) {
        if (!relations.contains(e)) {
            relations.add(e);
        }
    }

    /**
     * Removes an edge from the vertex
     * @param e edge to remove
     */
    public void removeEdge(Edge e) {
        relations.remove(e);
    }

}
