package cracksearch.world;// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;

import cracksearch.algorithm.ExplorationAlgorithm;
import cracksearch.algorithm.Route;
import cracksearch.util.Point;

/**
 * World Object
 *
 * Contains all objects within the world (i.e. cracks and robots). Also contains logic to read/write files to save
 * world data to storage.
 *
 * The class acts almost as an interface between the GUI and the logic of the program.
 */
public class World {

    @SuppressWarnings("WeakerAccess")
    public static final int MAX_ROBOTS = 1;
    public static final int MAX_CRACKS = 200;

    private int width;
    private int height;
    private List<Robot> robotList;
    private List<Crack> crackList;
    private ExplorationAlgorithm explorationAlgorithm;

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
        robotList.add(new Robot());

    }

    /**
     * Gets the route taken by the robot in the current world.
     * @return Route taken
     */
    public Route getRoute() {
        return explorationAlgorithm.getRoute();
    }

    /**
     * Set the exploration cracksearch.algorithm for the current world
     * @param explorationAlgo Algorithm to use
     */
    public void setExplorationAlgorithm(Class explorationAlgo) {

        try {
            explorationAlgorithm = (ExplorationAlgorithm) explorationAlgo.getDeclaredConstructors()[0].newInstance(crackList);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    /**
     * Adds the given crack to the world.
     * @param c Crack to add
     */
    public void addCrack(Crack c) {
        crackList.add(c);
    }

    /**
     * Draws the world in its current state
     * @param g Graphics2d object
     */
    public void drawWorld(Graphics2D g) {
        drawCracks(g);
        drawRobot(g);
    }

    /**
     * Draws the cracks in their current states
     * @param g Graphics2d object
     */
    private void drawCracks(Graphics2D g) {
        if (getNumCracks() > 0) {
            for (Crack c : crackList) {
                GeneralPath path = new GeneralPath();
                // create path
                path.moveTo(c.getPoint(0).x, c.getPoint(0).y);
                for (int j = 1; j < c.numPoints(); j++) {
                    path.lineTo(c.getPoint(j).x, c.getPoint(j).y);
                }
                // draw
                g.draw(path);
                drawCrackWeight(g, c);
            }
        }
    }

    /**
     * Draws the weight of a crack next to the crack.
     * @param g Graphics object to draw too
     * @param c Crack object to get weight/location info
     */
    private void drawCrackWeight(Graphics2D g, Crack c) {

        // find center point of crack
        Point center = new Point();
        center.x = (c.getStart().x + c.getEnd().x) / 2;
        center.y = (c.getStart().y + c.getEnd().y) / 2;

        String weight = String.format("%.2f",c.getLength());//String.valueOf(c.getLength());

        // draw weight at offset
        g.drawString(weight,center.x - 5,center.y - 5);

    }

    /**
     * Draws the robot in its current state
     * @param g Graphics2d object
     * TODO: decide if we want to draw robot or not (probably dependant on animation?)
     */
    private void drawRobot(Graphics2D g) {
        //robotList.get(0).drawRobot(g);
    }

}
