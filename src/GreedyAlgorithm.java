import graph.Graph;

import java.util.List;

/**
 * Created by Andy on 12/03/2018.
 */
public class GreedyAlgorithm extends ExplorationAlgorithm {

    public GreedyAlgorithm(List<Crack> cracks) {
        super(cracks);

    }

    @Override
    protected Route calculateRoute() {

        if (cracks.size() > 0) {

            Graph g = new Graph();

            // for every crack
            for (Crack crack : cracks) {

                // add the start position of each crack to graph g
                g.addVertex(crack.getPoint(0));
            }

            // run algorithm



        }

        return new Route(0);
    }

    private Route runGreedyAlgorithm(Graph g) {



    }

}
