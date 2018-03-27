// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

import javax.swing.*;
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

    private final JFrame frame;

    @SuppressWarnings("FieldCanBeLocal")
    private Route r;

    public frmReport() {
        frame = new JFrame("Crack Search");
        frame.setContentPane(this.panel1);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
    }

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

    public void saveToCSV() {
        // TODO: save to csv function
    }

}
