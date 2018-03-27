// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.util.*;
import java.util.List;

import util.Point;

/**
 * PanelWorldDesigner
 *
 * Contains classes and methods for drawing and editing the world using the world designer panel
 */
public class PanelWorldDesigner extends JPanel implements MouseListener, MouseMotionListener, KeyListener {


    private static final int NOT_DRAWING = 0;
    private static final int WAITING_FOR_INPUT = 1;
    private static final int SINGLE_SEGMENT_MODE = 2;
    private static final int MULTI_SEGMENT_MODE = 3;
    private static final int SHOW_LAST_ROUTE_MODE = 4;
    private static final int NUM_STATE_BITS = SHOW_LAST_ROUTE_MODE;

    private static final int FIRST_CRACK_DRAWING_MODE = NOT_DRAWING;
    private static final int LAST_CRACK_DRAWING_MODE = MULTI_SEGMENT_MODE;

    World world;
    private BitSet worldState;
    private Point[] drawingPoints;
    private int currentDrawingPoint;
    private GeneralPath path;
    private final util.Point mouseLocation;
    private final List<SimulationFinishedListener> simFinishedListeners;
    private Route lastRoute;

    public PanelWorldDesigner() {
        super();
        world = new World();
        worldState = new BitSet(NUM_STATE_BITS);
        worldState.set(NOT_DRAWING);
        mouseLocation = new util.Point();
        setDoubleBuffered(true);    // TODO: try improve double buffering with BufferStrategy
        simFinishedListeners = new LinkedList<>();
        // add listeners
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);

    }

    /**
     * Registers a simulation finished listener.
     * @param listener listener to register
     */
    public void addSimulationFinishedListener(SimulationFinishedListener listener) {
        simFinishedListeners.add(listener);
    }

    /**
     * Sets the world object.
     * @param w world object to use
     */
    public void setWorld(World w){
        world = w;
        update(getGraphics());      // send draw request
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        world.drawWorld(g2d);
        if (worldState.get(SINGLE_SEGMENT_MODE)) {
            paintDrawingCrack(g2d);
        }
        if (worldState.get(SHOW_LAST_ROUTE_MODE)) {
            paintLastRoute(g2d);
        }
    }

    /**
     * paints the crack that is currently being drawn to the graphics object
     * @param g Graphics object to paint too
     */
    private void paintDrawingCrack(Graphics2D g) {
        util.Point last_confirmed_point =  drawingPoints[currentDrawingPoint - 1];
        g.drawLine(last_confirmed_point.x, last_confirmed_point.y, mouseLocation.x, mouseLocation.y);
    }

    /**
     * Draws the route stored in lastRoute variable
     * @param g Graphics object to draw too
     */
    private void paintLastRoute(Graphics2D g) {

        // sanity check
        if (lastRoute.getSize() > 2) {
            ListIterator<RouteLocation> i = lastRoute.getLocations();
            RouteLocation location;
            // add all other points to route
            while (i.hasNext()) {
                location = i.next();
                // create next step in path
                if (location.getType() == RouteLocation.RouteType.CRACK) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.BLUE);
                }
                g.drawLine(location.getStart().x, location.getStart().y, location.getEnd().x, location.getEnd().y);

            }
        }
        // make sure colour is back on black
        g.setColor(Color.BLACK);
    }

    /**
     * Puts the designer into drawing mode, awaiting user input to detail which mode (Single or Multi)
     * it should draw in.
     */
    public void enterDrawingMode() {
        worldState.set(FIRST_CRACK_DRAWING_MODE, LAST_CRACK_DRAWING_MODE, false);
        worldState.set(WAITING_FOR_INPUT);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (worldState.get(WAITING_FOR_INPUT)) {
                if (e.isShiftDown()) {
                    // TODO: multi segment mode
                    //startDrawing(e);
                    //addPoint(e);
                } else {
                    startSingleSegmentDrawing(e);
                }
        } else if (worldState.get(MULTI_SEGMENT_MODE)) {
                if (e.isShiftDown()) {
                    addPoint(e);
                }
        } else if (worldState.get(SINGLE_SEGMENT_MODE)) {
                addPoint(e);
                finishDrawing();
        }

    }

    /*
     * Starts multi segment drawing mode. Marks the current mouse location as the start of the
     * crack and awaits another click to determine the next location, until the enter key is
     * pressed. While the designer is in MULTI_SEGMENT_MODE it will draw a line between the last
     * point position and the cursor location.
     *
     * *** NOT CURRENTLY IMPLEMENTED ***
     * TODO: implement multi segment feature
     * @param e MouseEvent containing mouse position
     */
    /*private void startDrawingMultiSegment(MouseEvent e) {
        worldState.set(FIRST_CRACK_DRAWING_MODE, LAST_CRACK_DRAWING_MODE, false);
        worldState.set(MULTI_SEGMENT_MODE);
        drawingPoints = new util.Point[Crack.MAX_POINTS];
        currentDrawingPoint = 0;
        path = new GeneralPath();
        path.moveTo(e.getX(),e.getY());
        addPoint(e);
    }*/

    /**
     * Starts single segment drawing mode. Marks the current mouse location as the start of the
     * crack and awaits a 2nd click to determine the end location. While the designer is in
     * SINGLE_SEGMENT_MODE it will draw a line between the start position and the cursor location.
     * @param e MouseEvent containing mouse position
     */
    private void startSingleSegmentDrawing(MouseEvent e) {
        worldState.set(FIRST_CRACK_DRAWING_MODE, LAST_CRACK_DRAWING_MODE, false);
        worldState.set(SINGLE_SEGMENT_MODE);
        drawingPoints = new util.Point[Crack.MAX_POINTS];
        currentDrawingPoint = 0;
        path = new GeneralPath();
        path.moveTo(e.getX(),e.getY());
        addPoint(e);
    }

    /**
     * Adds the point to the current crack at the location of the given mouse event.
     * @param e MouseEvent containing mouse position
     */
    private void addPoint(MouseEvent e) {
        this.grabFocus();
        drawingPoints[currentDrawingPoint++] = new util.Point(e.getX(),e.getY());
        path.lineTo(e.getX(),e.getY());
        update(getGraphics());
    }

    /**
     * Ends the drawing mode and saves any finished crack to the world.
     */
    private void finishDrawing() {

        // make sure we have at least 1 segment
        if (currentDrawingPoint > 1) {
            // clean up point array
            util.Point[] tmp = new util.Point[currentDrawingPoint];
            System.arraycopy(drawingPoints, 0, tmp, 0, currentDrawingPoint);
            double length = calcLength(tmp);
            world.addCrack(new Crack(tmp, length));
        }
        update(getGraphics());
        worldState.set(FIRST_CRACK_DRAWING_MODE, LAST_CRACK_DRAWING_MODE, false);
        worldState.set(NOT_DRAWING);
    }

    /**
     * Calculates the length of a given set of points.
     * @param crack Point array associated with the crack
     * @return length of crack as int
     */
    private Double calcLength(Point[] crack) {

        double length = 0;

        for (int i = 1;i < crack.length;i++) {
            length += calcDistanceBetween(crack[i-1], crack[i]);
        }

        return length;
    }

    /**
     * Calculates the distance between 2 points
     * @param p1 point 1
     * @param p2 point 2
     * @return distance between points
     */
    private Double calcDistanceBetween(Point p1, Point p2) {
        double xdiff = p1.x - p2.x;
        double ydiff = p1.y - p2.y;

        return Math.sqrt(xdiff*xdiff + ydiff*ydiff);
    }

    /**
     * Runs simulation for the current world / algorithm. When simulation has finished calls
     * sim finished listeners to notify of completion.
     */
    public void runSimulation() {

        // currently no animation, so simulation can finish immediately.
        // call the sim finished event to notify all listeners
        lastRoute = world.getRoute();
        // draw the route on screen
        worldState.set(SHOW_LAST_ROUTE_MODE);
        update(getGraphics());

        for (SimulationFinishedListener listener : simFinishedListeners) {
            listener.simulationFinished(lastRoute);
        }

    }

    /**
     * Stops drawing the last route
     */
    public void clearRoute() {
        worldState.set(SHOW_LAST_ROUTE_MODE, false);
        update(getGraphics());
    }

    /**
     * Set the exploration algorithm for the current world
     * @param exploration_algo Algorithm to use
     */
    public void setExplorationAlgorithm(Class exploration_algo) {
        world.setExplorationAlgorithm(exploration_algo);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // update mouse locations for drawing cracks in progress
        mouseLocation.x = e.getX();
        mouseLocation.y = e.getY();
        // only send updates when in drawing mode
        if (worldState.get(SINGLE_SEGMENT_MODE)) {
            update(getGraphics());
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }

}
