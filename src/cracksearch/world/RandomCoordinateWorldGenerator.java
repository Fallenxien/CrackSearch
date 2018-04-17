// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool


package cracksearch.world;

import cracksearch.util.Point;

import java.util.Random;

/**
 * RandomCoordinateWorldGenerator
 *
 * Generates a fully random world. Each coordinate is randomly generated such that
 * length of lines is random.
 *
 */
public class RandomCoordinateWorldGenerator extends WorldGenerator {

    private int worldWidth;
    private int worldHeight;

    /**
     * Creates a random coordinate world generator. The generator will create
     * @param worldWidth width of world
     * @param worldHeight height of world
     */
    public RandomCoordinateWorldGenerator(int worldWidth, int worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    /**
     * Generates a world with random cracks.
     * @param numCracks number of cracks to generate
     * @return Random world
     */
    @Override
    public World generateWorld(int numCracks) {

        Random rand = new Random();
        Point[] p;
        World world = new World();

        for (int i = 0; i<numCracks;i++) {
            p = new Point[2];
            p[0] = new Point(rand.nextInt(worldWidth),rand.nextInt(worldHeight));
            p[1] = new Point(rand.nextInt(worldWidth),rand.nextInt(worldHeight));
            world.addCrack(new Crack(p));
        }

        return world;
    }
}

