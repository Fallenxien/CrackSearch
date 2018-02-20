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
import java.util.ListIterator;

/**
 * Created by Andy on 13/02/2018.
 */
public class Tree {


    private List<Node> nodes;

    public Tree() {
        nodes = new LinkedList<>();
    }

    public Tree(List<Node> vertices) {
        nodes = vertices;
    }

    public ListIterator<Node> getNodeIterator() {
        return nodes.listIterator();
    }

    public void addNode(Node v) {
        nodes.add(v);
    }

    public void addNode(Point p) {
        nodes.add(new Node(p));
    }

    public void removeNode(Node v) {
        nodes.remove(v);
    }


}
