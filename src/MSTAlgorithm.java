import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Andy on 12/02/2018.
 */
public class MSTAlgorithm extends ExplorationAlgorithm {

    public MSTAlgorithm(List<Crack> cracks) {
        super(cracks);
    }

    @Override
    public Route calculateRoute() {

        if (cracks.size() > 0) {

            List<Crack> crack_array = new LinkedList<>(cracks);
            List<Crack> route = new ArrayList<>(cracks.size());

        // for every crack
        for (int i=0;i<route.size();i++) {

            Crack smallest_crack, current;
            ListIterator<Crack> iterator = crack_array.listIterator();
            smallest_crack = iterator.next();

            // find the smallest crack in the graph
            while (iterator.hasNext()) {
                current = iterator.next();
                if (smallest_crack.getLength() < current.getLength()) {
                    smallest_crack = current;
                }
            }

            // add crack to new list, remove from old list
            route.add(smallest_crack);
            crack_array.remove(smallest_crack);
        }

        }

        return null;
    }

    @Override
    public int calculateRouteLength() {
        return 0;
    }

}
