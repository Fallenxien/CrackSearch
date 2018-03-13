// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import javax.imageio.*;
import java.util.List;
/**
 * Robot
 *
 * Contains methods and variables controlling the robot.
 */
public class Robot {

    private ExplorationAlgorithm algorithm;
    private Route r;

    public Robot(List<Crack> cracks) {


    }

    /**
     * Draw the robot to the given 2d graphics object
     * @param g
     */
    public void drawRobot(Graphics2D g) {

        try {
            BufferedImage img = ImageIO.read(new File("C:\\Users\\Andy\\IdeaProjects\\CrackSearch\\src\\robot.png"));
            g.drawImage(img,100,100,40,43,null);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
