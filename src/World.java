// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.*;
import java.util.List;

/**
 * World Object
 *
 * Contains all objects within the world (i.e. cracks and robots). Also contains logic to read/write files to save
 * world data to storage.
 *
 * The class acts almost as an interface between the GUI and the logic of the program.
 */
public class World {

    public static final int MAX_ROBOTS = 1;
    public static final int MAX_CRACKS = 200;

    private int width;
    private int height;
    private List<Robot> robotList;
    private List<Crack> crackList;
    private Graphics2D  graphics;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int length) {
        this.height = length;
    }

    public void setCrackList(List<Crack> list) {
        crackList = list;
    }

    public ListIterator<Crack> getCrackIterator() {
        return crackList.listIterator();
    }

    public void setRobotList(List<Robot> list) {
        robotList = list;
    }

    public int getNumCracks() {
        return crackList.size();
    }

    public World() {

        crackList = new LinkedList<>();
        robotList = new ArrayList<>(MAX_ROBOTS);
        robotList.add(new Robot(crackList));

    }

    public static World LoadWorld(String filePath) throws InvalidWorldFileException, java.io.FileNotFoundException {
        WorldReader reader = new WorldReader(filePath);
        return reader.getWorld();
    }

    /**
     * Adds the given crack to the world.
     * @param c Crack to add
     */
    public void addCrack(Crack c) {
        crackList.add(c);
    }

    public void drawWorld(Graphics2D g) {
        drawCracks(g);
        drawRobot(g);
    }

    public void drawCracks(Graphics2D g) {
        for (Iterator<Crack> i = crackList.listIterator(); i.hasNext();) {
            Crack c = i.next();
            GeneralPath path = new GeneralPath();
            // create path
            path.moveTo(c.getPoint(0).x, c.getPoint(0).y);
            for (int j = 1; j<c.numPoints();j++) {
                path.lineTo(c.getPoint(j).x, c.getPoint(j).y);
            }
            // draw
            g.draw(path);
        }
    }

    public void drawRobot(Graphics2D g) {
        robotList.get(0).drawRobot(g);
    }

}
