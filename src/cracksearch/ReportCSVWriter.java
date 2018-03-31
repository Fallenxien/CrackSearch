// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch;

import cracksearch.algorithm.Route;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * ReportCSVWriter
 *
 * Writes a route report to a CSV file. Matches a similar format to that displayed
 * in the report form.
 */
public class ReportCSVWriter {

    private Route route;
    private File file;

    /**
     * Constructs a report CSV writer with a route and file.
     * @param r Route to output
     * @param f File location to save too
     */
    public ReportCSVWriter(Route r, File f) {
        route = r;
        file = f;

        if (!checkFileExtension(f)) {
            file = new File(f.getPath() + ".csv");
        }
    }

    /**
     * Writes the route to the file in CSV format.
     * @return true if successful
     */
    public boolean writeToCSV() {

        try (FileWriter wr = new FileWriter(file)) {
            wr.write(String.format("Total Trajectory Length,%.2f\nTotal Crack Distance Walked,%.2f\n" +
                    "Total Intermediate Distance Walked,%.2f,\n\nIntermediate Breakdown\n" +
                    "Between Cracks,%.2f\nTo/From Base,%.2f",
                    route.getTotalLength(), route.getTotalCrackLength(), route.getTotalIntermediateLength(),
                    route.getTotalBetweenCrackLength(), route.getTotalBetweenBaseLength()));
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * Checks if the file extension is ".csv"
     * @param f File to check
     * @return true if extension is .csv, else false
     */
    private boolean checkFileExtension(File f) {
        String path = f.getPath();
        int extension_pos = path.lastIndexOf(".");

        if (extension_pos == -1) {
            // no extension present
            return false;
        } else {
            if (!path.substring(extension_pos).equals(".csv")) {
                // wrong extension
                return false;
            }
        }
        // correct extension
        return true;
    }

}
