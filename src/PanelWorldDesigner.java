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
import util.Point;

/**
 * PanelWorldDesigner
 *
 * Contains classes and methods for drawing and editing the world using the world designer panel
 */
public class PanelWorldDesigner extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    private enum WorldState {
        NOT_DRAWING, WAITING_FOR_INPUT, SINGLE_SEGMENT_MODE, MULTI_SEGMENT_MODE
    }

    World world;
    private WorldState worldState;
    private Point[] drawingPoints;
    private int currentDrawingPoint;
    private GeneralPath path;
    private util.Point mouseLocation;

    public PanelWorldDesigner() {
        super();
        world = new World();
        worldState = WorldState.NOT_DRAWING;
        mouseLocation = new util.Point();
        setDoubleBuffered(true);    // TODO: try improve double buffering with BufferStrategy
        // add listeners
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);

    }

    public void setWorld(World w){
        world = w;
        update(getGraphics());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        world.drawWorld(g2d);
        if (worldState == WorldState.SINGLE_SEGMENT_MODE) {
            paintDrawingCrack(g2d);
        }
    }

    private void paintDrawingCrack(Graphics2D g) {

        util.Point last_confirmed_point =  drawingPoints[currentDrawingPoint - 1];
        g.drawLine(last_confirmed_point.x, last_confirmed_point.y, mouseLocation.x, mouseLocation.y);
    }

    /**
     * Puts the designer into drawing mode, awaiting user input to detail which mode (Single or Multi)
     * it should draw in.
     */
    public void enterDrawingMode() {
        worldState = WorldState.WAITING_FOR_INPUT;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        switch (worldState) {
            case WAITING_FOR_INPUT:
                if (e.isShiftDown()) {
                    // TODO: multi segment mode
                    //startDrawing(e);
                    //addPoint(e);
                } else {
                    startSingleSegmentDrawing(e);
                }
                break;
            case MULTI_SEGMENT_MODE:
                if (e.isShiftDown()) {
                    addPoint(e);
                    break;
                }
            case SINGLE_SEGMENT_MODE:
                addPoint(e);
                finishDrawing();
                break;
        }

    }

    private void startDrawingMultiSegment(MouseEvent e) {
        worldState = WorldState.MULTI_SEGMENT_MODE;
        drawingPoints = new util.Point[Crack.MAX_POINTS];
        currentDrawingPoint = 0;
        path = new GeneralPath();
        path.moveTo(e.getX(),e.getY());
        addPoint(e);
    }

    /**
     * Starts single segment drawing mode. Marks the current mouse location as the start of the
     * crack and awaits a 2nd click to determine the end location. While the designer is in
     * SINGLE_SEGMENT_MODE it will draw a line between the start position and the cursor location.
     * @param e MouseEvent containing mouse position
     */
    private void startSingleSegmentDrawing(MouseEvent e) {
        worldState = WorldState.SINGLE_SEGMENT_MODE;
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
            world.addCrack(new Crack(tmp, 0));
        }
        update(getGraphics());
        worldState = WorldState.NOT_DRAWING;
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
        mouseLocation.x = e.getX();
        mouseLocation.y = e.getY();
        if (worldState == WorldState.SINGLE_SEGMENT_MODE) {
            mouseLocation.x = e.getX();
            mouseLocation.y = e.getY();
            update(getGraphics());
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

        // if we are drawing and shift is released, finish drawing
        if (worldState != WorldState.NOT_DRAWING && e.getKeyCode() == KeyEvent.VK_SHIFT) {
            finishDrawing();
        }

    }
}
