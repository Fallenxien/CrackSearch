// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch.graph;

/**
 * cracksearch.graph.Edge Object
 *
 * Represents an edge within a cracksearch.graph
 */
public class Edge implements Comparable<Edge> {

    private final Vertex start;
    private final Vertex end;
    private final double weight;

    public Edge(Vertex start, Vertex end, double weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    /**
     * Gets the weight of the edge
     * @return Weight of edge
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Gets the start vertex of this section
     * @return start vertex
     */
    public Vertex getStart() {
        return start;
    }

    /**
     * Gets the end vertex of this section
     * @return end vertex
     */
    public Vertex getEnd() {
        return end;
    }

    /**
     * Compare two edges to see which is smaller.
     * @param o Edge to compare too
     * @return 1 if edge o is larger, -1 if edge o is smaller and 0 if edges are the same length
     */
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

    /**
     * Gets the vertex associated with the given vertex, based upon edge
     * @param e Edge containing to verticies
     * @param v Vertex you do not wish to retrieve
     * @return Other vertex on edge
     */
    public static Vertex getAssociatedVertex(Edge e, Vertex v) {
        if (e.getStart() == v) {
            return e.getEnd();
        } else {
            return e.getStart();
        }
    }

}
