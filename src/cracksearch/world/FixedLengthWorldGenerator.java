// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch.world;

import cracksearch.util.Line;
import cracksearch.util.Point;

import java.util.Random;

/**
 * FixedLengthWorldGenerator
 *
 * Generates a random world with each crack having a fixed length. A single coordinate is randomly
 * generated, and we choose a point a fixed distance away as the second point.
 *
 */
public class FixedLengthWorldGenerator extends WorldGenerator {

    private int worldWidth;
    private int worldHeight;
    private int crackLength;

    /**
     * Creates a fixed length world generator. The generator will create
     * @param crackLength fixed crack length
     * @param worldWidth width of world
     * @param worldHeight height of world
     */
    public FixedLengthWorldGenerator(int crackLength, int worldWidth, int worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.crackLength = crackLength;
    }

    /**
     * Generates a world with random cracks. All cracks will have the length specified in the
     * crackLength argument of the constructor
     * @param numCracks number of cracks to generate
     * @return Random world
     */
    @Override
    public World generateWorld(int numCracks) {
        Random rand = new Random();
        Point[] p;
        double theta;
        Line l;
        World world = new World();

        for (int i = 0; i < numCracks; i++) {
            p = new Point[2];

            while (true) {
                p[0] = new Point(rand.nextInt(worldWidth), rand.nextInt(worldHeight));

                // make sure we have cracks in both directions
                if (rand.nextDouble() < 0.5) {
                    l = new Line(p[0], rand.nextDouble());
                } else {
                    l = new Line(p[0], -rand.nextDouble());
                }
                theta = calculateAngle(p[0], l.xIntercept());
                int adjacent = (int) (crackLength * Math.cos(theta));
                int opposite = (int) (crackLength * Math.sin(theta));
                p[1] = new Point(p[0].x - opposite, p[0].y - adjacent);

                if (p[1].x < worldWidth && p[1].x > 0 && p[1].y < worldHeight && p[1].y > 0) {
                    break;
                }
            }

            world.addCrack(new Crack(p, crackLength));
        }

        return world;
    }

        /**
         * Calculates the angle theta given two points on the line
         * @param p1 First point to use
         * @param p2 Second point to use
         * @return Theta in rads
         */
        private double calculateAngle(Point p1, Point p2) {

            // theta is angle between the line between 2 points and the line x = (p1.x)
            // calculate this by tan-1(opp/adj)
            double adjacent = p1.y - p2.y;
            double opposite = p1.x - p2.x;
            double theta = Math.atan((opposite)/(adjacent));

            return theta;

        }
}
