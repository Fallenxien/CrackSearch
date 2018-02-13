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

    private enum CrackDrawingState {
        IDLE, DRAWING_EDGE
    }

    World world;
    private CrackDrawingState drawingState;
    private Point[] drawingPoints;
    private int currentDrawingPoint;
    private GeneralPath path;
    private util.Point mouseLocation;

    public PanelWorldDesigner() {
        super();
        world = new World();
        drawingState = CrackDrawingState.IDLE;
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
        if (drawingState != CrackDrawingState.IDLE) {
            paintDrawingCrack(g2d);
        }
    }

    private void paintDrawingCrack(Graphics2D g) {

        util.Point last_confirmed_point =  drawingPoints[currentDrawingPoint - 1];
        g.draw(path);
        g.drawLine(last_confirmed_point.x, last_confirmed_point.y, mouseLocation.x, mouseLocation.y);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        switch (drawingState) {
            case IDLE:
                if (e.isShiftDown()) {
                    startDrawing(e);
                    break;
                }
            case DRAWING_EDGE:
                if (e.isShiftDown()) {
                    addPoint(e);
                    break;
                }
        }

    }

    private void startDrawing(MouseEvent e) {
        drawingState = CrackDrawingState.DRAWING_EDGE;
        drawingPoints = new util.Point[Crack.MAX_POINTS];
        currentDrawingPoint = 0;
        path = new GeneralPath();
        path.moveTo(e.getX(),e.getY());
        addPoint(e);
    }

    private void addPoint(MouseEvent e) {
        this.grabFocus();
        drawingPoints[currentDrawingPoint++] = new util.Point(e.getX(),e.getY());
        path.lineTo(e.getX(),e.getY());
        update(getGraphics());
    }

    private void finishDrawing() {

        // make sure we have at least 1 segment
        if (currentDrawingPoint > 1) {
            // clean up point array
            util.Point[] tmp = new util.Point[currentDrawingPoint];
            System.arraycopy(drawingPoints, 0, tmp, 0, currentDrawingPoint);
            world.addCrack(new Crack(tmp, 0));
        }

        drawingState = CrackDrawingState.IDLE;
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
        if (drawingState == CrackDrawingState.DRAWING_EDGE) {
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
        if (drawingState != CrackDrawingState.IDLE && e.getKeyCode() == KeyEvent.VK_SHIFT) {
            finishDrawing();
        }

    }
}
