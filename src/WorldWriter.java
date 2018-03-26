// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

import util.Point;

import java.io.*;
import java.util.ListIterator;

/**
 * WorldWriter.java
 *
 * Logic to write worlds to storage.
 *
 * World files are stored in the following format.
 * [SOH][width][height][num robots][num cracks][crack array]
 *
 * Where crack array is continuous blocks each representing a crack. Each block
 * takes on the format:
 * [num points][crack length][x1][y1][x2][y2][xn][yn]
 *
 * All numbers except crack length are stored as integers. (4 bytes)
 * Crack length is stored as a double. (8 bytes)
 *
 */
public class WorldWriter {

    private static final byte SOH = 0x1;

    private final File f;
    private final World w;

    public WorldWriter(File f, World w) {
        this.f = f;
        this.w = w;
    }

    public void save() throws IOException {

        DataOutputStream writer = new DataOutputStream(new FileOutputStream(f));

        writer.write(SOH);
        writer.writeInt(w.getWidth());
        writer.writeInt(w.getHeight());
        writer.writeInt(1);
        writer.writeInt(w.getNumCracks());
        writeCracks(writer);

        writer.flush();
        writer.close();

    }

    private void writeCracks(DataOutputStream writer)throws IOException {

        ListIterator<Crack> cracks = w.getCrackIterator();
        Crack c;
        Point p;

        while (cracks.hasNext()) {
            c = cracks.next();
            writer.writeInt(c.numPoints());
            writer.writeDouble(c.getLength());

            for (int j = 0; j < c.numPoints(); j++) {
                p = c.getPoint(j);
                writer.writeInt(p.x);
                writer.writeInt(p.y);
            }
        }

    }


}
