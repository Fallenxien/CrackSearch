// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch;

import javax.swing.*;
import java.awt.event.*;

/**
 * dlgWorldGenerator
 *
 * Holds all code for creating and managing the world generator dialog form and all associated events.
 */
public class dlgWorldGenerator extends JDialog {

    /**
     * Enumerator for type of random world generator.
     * RANDOM = RandomCoordinateWorldGenerator
     * FIXED_LENGTH = FixedLengthWorldGenerator
     */
    public enum GeneratorType {
        RANDOM, FIXED_LENGTH
    }

    /**
     * Enumerator for dialog result
     */
    public enum Result {
        CANCEL, OK
    }

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtNumCracks;
    private JLabel lblNumCracks;
    private JLabel lblType;
    private JRadioButton rbnFullRandomGenerator;
    private JRadioButton rbnFixedLengthGenerator;
    private JLabel lblCrackLength;
    private JTextField txtCrackLength;

    private int numCracks;
    private int crackLength;
    private GeneratorType generatorType;
    private Result result;

    /**
     * Returns the dialog result
     * @return result
     */
    public Result getResult() {
        return result;
    }

    /**
     * Gets the number of cracks user specified
     * @return num cracks
     */
    public int getNumCracks() {
        return numCracks;
    }

    /**
     * Gets the crack length user specified
     * @return crack length
     */
    public int getCrackLength() {
        return crackLength;
    }

    /**
     * Gets the generator the user specified
     * @return generator type
     */
    public GeneratorType getGeneratorType() {
        return generatorType;
    }

    /**
     * Creates a world generator dialog
     */
    public dlgWorldGenerator() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // add action listeners for input controls
        txtCrackLength.addActionListener(e -> onCrackLengthChanged());
        txtNumCracks.addActionListener(e -> onNumCracksChanged());
        rbnFixedLengthGenerator.addActionListener(e -> onGeneratorChanged());
        rbnFullRandomGenerator.addActionListener(e -> onGeneratorChanged());

        // fill values
        onCrackLengthChanged();
        onNumCracksChanged();
        onGeneratorChanged();

    }

    /**
     * Called when generator radio buttons are changed
     */
    private void onGeneratorChanged() {

        if (rbnFixedLengthGenerator.isSelected()) {
            generatorType = GeneratorType.FIXED_LENGTH;
            txtCrackLength.setEnabled(true);
            lblCrackLength.setEnabled(true);
        } else {
            generatorType = GeneratorType.RANDOM;
            txtCrackLength.setEnabled(false);
            lblCrackLength.setEnabled(false);
        }
    }

    /**
     * Called when crack length text box changes
     */
    private void onCrackLengthChanged() {

        if (isNumeric(txtCrackLength.getText())) {
            crackLength = Integer.parseInt(txtCrackLength.getText());
        }

    }

    /**
     * Called when num cracks text box changes
     */
    private void onNumCracksChanged() {

        if (isNumeric(txtNumCracks.getText())) {
            numCracks = Integer.parseInt(txtNumCracks.getText());
        }

    }

    /**
     * Checks if a given string is numeric
     * @param val string to test
     * @return true if numeric, else false
     */
    private boolean isNumeric(String val) {
        try {
            Double.parseDouble(val);
        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    /**
     * Called when OK button pressed
     */
    private void onOK() {
        result = Result.OK;
        dispose();
    }

    /**
     * Called when any cancel action is taken.
     * Cancel actions include: cancel button, pressing escape and closing the dialog.
     */
    private void onCancel() {
        result = Result.CANCEL;
        dispose();
    }

}
