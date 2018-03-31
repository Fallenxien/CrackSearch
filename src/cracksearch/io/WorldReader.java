// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch.io;

import cracksearch.util.Point;
import cracksearch.world.Crack;
import cracksearch.world.InvalidWorldFileException;
import cracksearch.world.World;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * WorldReader.java
 *
 * Logic to read worlds to storage.
 *
 * World files are stored in the following format.
 * [SOH][width][height][num robots][num cracks][crack array]
 *
 * All numbers are stored as integer (4 bytes)
 *
 * Where crack array is continuous blocks each representing a crack. Each block
 * takes on the format:
 * [num points][crack length][x1][y1][x2][y2][xn][yn]
 */
public class WorldReader {

    private static final byte SOH = 0x1;

    private final File f;
    private final World w;

    /**
     * Creates a world reader object with the given file
     * @param f File to load
     * @throws InvalidWorldFileException F is not a valid world file
     * @throws java.io.FileNotFoundException IO Exception
     */
    public WorldReader(File f) throws InvalidWorldFileException, java.io.FileNotFoundException {
        this.f = f;
        w = new World();
        load();
    }

    /**
     * Creates a world reader object with the given file path
     * @param filepath File path to load
     * @throws InvalidWorldFileException F is not a valid world file
     * @throws java.io.FileNotFoundException IO Exception
     */
    public WorldReader(String filepath) throws InvalidWorldFileException, java.io.FileNotFoundException {
        f = new File(filepath);
        w = new World();
        load();
    }

    /**
     * Loads the world from the world file
     */
    private void load() throws InvalidWorldFileException, java.io.FileNotFoundException {

        DataInputStream reader;

        try {

            // create a buffered reader from an input stream
            reader = new DataInputStream(new FileInputStream(f));
            // read file header (a single SOH byte)
            if (reader.read() == SOH) {

                w.setWidth(reader.readInt());
                w.setHeight(reader.readInt());
                reader.readInt(); // only 1 robot allowed, so read number to move pointer but discard data
                int num_cracks = reader.readInt();
                w.setCrackList(readCracks(num_cracks, reader));

            } else {
                throw new InvalidWorldFileException();
            }

        } catch (java.io.FileNotFoundException ex1) {
            throw ex1;
        } catch (java.io.IOException ex2) {
            throw new InvalidWorldFileException();
        }

    }

    /**
     * Returns the world file
     * @return world file
     */
    public World getWorld() {
        return w;
    }

    /**
     * Reads the cracks from the given input stream
     * @param numCracks number of cracks to read
     * @param reader input stream to use
     * @return List of cracks in the file
     * @throws IOException IO Exception
     */
    private List<Crack> readCracks(int numCracks, DataInputStream reader) throws IOException {

        List<Crack> list = new LinkedList<>();   // list of cracks

        // sanity check
        if (numCracks > 0) {

            int num_points;                                    // number of points in crack
            Point[] p;                                         // array for storing points
            double crack_length;                                  // length of crack

            for (int i = 0; i < numCracks; i++) {
                num_points = reader.readInt();
                crack_length = reader.readDouble();

                p = new Point[num_points];

                // read points from stream into point array
                for (int j = 0; j < num_points; j++) {
                    p[j] = new Point();
                    p[j].x = reader.readInt();
                    p[j].y = reader.readInt();
                }
                // create crack & add it to the list
                list.add(new Crack(p, crack_length));
            }
        }
        return list;
    }
}
