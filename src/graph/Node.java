// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool


package graph;

import util.Point;

import java.util.LinkedList;
import java.util.List;

/**
 * Node
 *
 * Contains methods and properties describing a node in a Tree
 */
public class Node {

    private Point position;
    private List<Edge> children;

    public Node(Point pos) {
        position = pos;
        children = new LinkedList<>();
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

    public void addEdge(Edge e) {
        children.add(e);
    }

    public void removeEdge(Edge e) {
        children.remove(e);
    }

}
