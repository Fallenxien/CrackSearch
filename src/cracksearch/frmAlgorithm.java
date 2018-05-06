// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;

/**
 * Algorithm form.
 *
 * Used to add or remove custom algorithms to the software.
 */
public class frmAlgorithm implements AlgorithmListUpdatedListener, WindowListener {

    private final JFrame frame;
    private JButton btnAdd;
    private JButton btnRemove;
    private JList lstAlgorithms;
    private JPanel pnlContainer;

    private DefaultListModel<String> listModel;
    private ExplorationAlgorithmList listLoader;

    public frmAlgorithm() {
        frame = new JFrame("Custom Algorithms");
        frame.setContentPane(this.pnlContainer);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();

        // setup cracksearch.algorithm list loader
        listLoader = ExplorationAlgorithmList.getInstance();
        listLoader.addAlgorithmListUpdatedListener(this);

        // create list model
        listModel = new DefaultListModel<>();
        for (String name:listLoader.getNames()) {
            listModel.addElement(name);
        }
        lstAlgorithms.setModel(listModel);

        // add listeners
        btnAdd.addActionListener(e -> addAlgorithm());
        btnRemove.addActionListener(e -> removeAlgorithm());

    }

    /**
     * Shows the form
     */
    public void show() {
        frame.setVisible(true);
    }

    /**
     * Allows the user to select a .class file they would like to load as a custom cracksearch.algorithm.
     * Algorithms must extend the ExplorationAlgorithm class.
     * Called when the add button is pressed.
     */
    private void addAlgorithm() {

        File f = getAlgorithmToAdd();

        if (f != null) {
            listLoader.addAlgorithm(f);
        }

    }

    /**
     * Prompts user to select a .class file they would like to add to the class list
     * @return File containing .class file to load (File has not been checked yet)
     */
    private File getAlgorithmToAdd() {

        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Class Files (*.class)", "class");
        fc.setFileFilter(filter);
        if (fc.showOpenDialog(pnlContainer) == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        } else {
            return null;
        }

    }

    /**
     * Removes the selected cracksearch.algorithm from the program. Called when the remove button is pressed.
     */
    private void removeAlgorithm() {
        listLoader.removeAlgorithm(lstAlgorithms.getSelectedIndex());
    }

    /**
     * Called when listLoader updates. Updates the list model used by the list box
     * to match the new set of Exploration Algorithms
     * @param names names of algorithms to display
     */
    @Override
    public void onAlgorithmListUpdatedListener(String[] names) {
        listModel.clear();
        for (String name:listLoader.getNames()) {
            listModel.addElement(name);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    /**
     * Unregisters the algorithm list update listener
     * @param e window event
     */
    @Override
    public void windowClosing(WindowEvent e) {
        listLoader.removeAlgorithmListUpdatedListener(this);
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
