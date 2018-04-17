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

    private int numCracks;
    private int worldWidth;
    private int worldHeight;

    public RandomCoordinateWorldGenerator(int numCracks, int worldWidth, int worldHeight) {
        this.numCracks = numCracks;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public RandomCoordinateWorldGenerator(int worldWidth, int worldHeight) {
        numCracks = World.MAX_CRACKS;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    @Override
    public World generateWorld() {

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

