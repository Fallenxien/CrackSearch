// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch;

import cracksearch.algorithm.Route;
import cracksearch.io.ReportCSVWriter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Report form. Generated upon simulation completed.
 *
 * Displays statistical information about the completed route.
 */
public class frmReport {
    private JTextArea txtReport;
    private JPanel panel1;
    private JButton btnSaveCSV;

    private final JFrame frame;

    @SuppressWarnings("FieldCanBeLocal")
    private Route r;

    public frmReport() {
        frame = new JFrame("Crack Search");
        frame.setContentPane(this.panel1);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.pack();

        btnSaveCSV.addActionListener(e -> saveToCSV());
    }

    /**
     * Shows the report form using the supplied route
     * @param r Route to report
     */
    public void showReport(Route r) {

        this.r = r;
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        String s = "Total Trajectory Length: " +
                df.format(r.getTotalLength()) +
                "\nTotal Crack Distance Walked: " +
                df.format(r.getTotalCrackLength()) +
                "\nTotal Intermediate Distance Walked: " +
                df.format(r.getTotalIntermediateLength()) +
                "\n\nIntermediate Breakdown:" +
                "\nBetween Cracks: " +
                df.format(r.getTotalBetweenCrackLength()) +
                "\nTo/From Base: " +
                df.format(r.getTotalBetweenBaseLength());

        txtReport.setText(s);

        frame.setVisible(true);
    }

    /**
     * Shows the report form with the last supplied route
     */
    public void showReport() {
        frame.setVisible(true);
    }

    /**
     * Tells the report form to close down & dispose properly
     */
    public void close() {
        frame.dispose();
    }

    /**
     * Saves the report to a CSV file of the users choice
     */
    private void saveToCSV() {

        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV (*.csv)", "csv");
        fc.setFileFilter(filter);
        if (fc.showSaveDialog(panel1) == JFileChooser.APPROVE_OPTION) {
            ReportCSVWriter writer = new ReportCSVWriter(r,fc.getSelectedFile());
            writer.writeToCSV();
        }

    }

}
