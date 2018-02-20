// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package graph;

/**
 * graph.Edge Object
 *
 * Represents an edge within a graph
 */
public class Edge implements Comparable<Edge> {

    private Vertex start;
    private Vertex end;
    private double weight;

    public Edge(Vertex start, Vertex end, double weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public Vertex getStart() {
        return start;
    }

    public Vertex getEnd() {
        return end;
    }

    @Override
    public int compareTo(Edge o) {

        if (getWeight() - o.getWeight() > 0) {
            return 1;
        } else if (getWeight() - o.getWeight() < 0) {
            return -1;
        } else {
            return 0;
        }

    }
}
