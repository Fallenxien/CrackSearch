import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import graph.*;

/**
 * Created by Andy on 12/02/2018.
 */
public class MSTAlgorithm extends ExplorationAlgorithm {

    public MSTAlgorithm(List<Crack> cracks) {
        super(cracks);


    }

    @Override
    public void calculateRoute() {

        if (cracks.size() > 0) {

            Graph g = new Graph();

            // for every crack
            for (Crack crack : cracks) {

                // add the start position of each crack to graph g
                g.addVertex(crack.getPoint(0));
            }

            // then complete the graph
            Graph.completeGraph(g);

        }

    }

    public void Kruskals() {



    }

    @Override
    public double calculateRouteLength() {
        return 0;
    }

}
