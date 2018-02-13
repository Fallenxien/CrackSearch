import java.util.List;

/**
 * Created by Andy on 12/02/2018.
 */
public abstract class ExplorationAlgorithm {

    List<Crack> cracks;

    public ExplorationAlgorithm(List<Crack> cracks) {
        this.cracks = cracks;
    }

    public abstract Route calculateRoute();
    public abstract int calculateRouteLength();

}
