// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch.world;

import cracksearch.algorithm.Route;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;

/**
 * Robot
 *
 * Contains methods and variables controlling the robot.
 */
public class Robot {

    private final static int IMG_WIDTH = 40;
    private final static int IMG_HEIGHT = 43;

    private Route r;
    private final Point location;

    public Robot() {
        location = new Point(0,0);
    }

    /**
     * Draw the robot to the given 2d graphics object
     * @param g Graphics object to draw too
     */
    public void drawRobot(Graphics2D g) {

        try {
            BufferedImage img = ImageIO.read(new File("C:\\Users\\Andy\\IdeaProjects\\CrackSearch\\src\\robot.png"));
            g.drawImage(img,location.x - IMG_WIDTH / 2,location.y - IMG_HEIGHT / 2,40,43,null);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
