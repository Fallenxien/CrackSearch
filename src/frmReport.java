// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

import javax.swing.*;

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
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.pack();
    }

    public void showReport(Route r) {

        this.r = r;

        String s = "Total Trajectory Length: " +
                r.getTotalLength() +
                "\nTotal Crack Distance Walked: " +
                r.getTotalCrackLength() +
                "\nTotal Intermediate Distance Walked: " +
                r.getTotalIntermediateLength() +
                "\n\nIntermediate Breakdown:" +
                "\nBetween Cracks: " +
                r.getTotalBetweenCrackLength() +
                "\nTo/From Base: " +
                r.getTotalBetweenBaseLength();

        txtReport.setText(s);

        frame.setVisible(true);
    }

    public void saveToCSV() {
        // TODO: save to csv function
    }

}
