// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 * frmWorldDesigner
 *
 * Holds all code for creating and managing the World Designer form and all associated events.
 */
public class frmWorldDesigner implements ActionListener {

    private final static String NEW_COMMAND = "new";
    private final static String OPEN_COMMAND = "open";
    private final static String SAVE_COMMAND = "save";
    private final static String SAVE_AS_COMMAND = "save_as";

    private JFrame frame;
    private JPanel pnlContainer;
    private PanelWorldDesigner pnlDesigner;
    private JMenuItem menuItemSave;

    private File worldFile;

    private void setWorldFile(File f) {
        if (f != null) {
            menuItemSave.setEnabled(true);
        } else {
            menuItemSave.setEnabled(false);
        }

        worldFile = f;
    }

    private File getWorldFile() {
        return worldFile;
    }

    public frmWorldDesigner() {

        frame = new JFrame("Crack Search");
        frame.setContentPane(this.pnlContainer);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();

        buildMenuBar();

    }

    /**
     * Show the current form
     */
    public void show() {
        frame.setVisible(true);
    }

    /**
     * Builds the menu bar for the current form
     */
    private void buildMenuBar() {

        JMenuBar menuBar;
        JMenu menuFile;
        JMenuItem menuItemNew;
        JMenuItem menuItemOpen;
        JMenuItem menuItemSaveAs;

        // menu bar
        menuBar = new JMenuBar();

        // file menu
        menuFile = new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menuFile);

        // new menu item
        menuItemNew = new JMenuItem("New");
        menuItemNew.setActionCommand(NEW_COMMAND);
        menuItemNew.addActionListener(this);
        menuFile.add(menuItemNew);

        // open menu item
        menuItemOpen = new JMenuItem("Open...");
        menuItemOpen.setActionCommand(OPEN_COMMAND);
        menuItemOpen.addActionListener(this);
        menuFile.add(menuItemOpen);

        // save menu item
        // TODO: make save menu button disabled on startup, only enabled when worldFile != null
        menuItemSave = new JMenuItem("Save");
        menuItemSave.setActionCommand(SAVE_COMMAND);
        menuItemSave.addActionListener(this);
        menuItemSave.setEnabled(false);
        menuFile.add(menuItemSave);

        // save as menu item
        menuItemSaveAs = new JMenuItem("Save As...");
        menuItemSaveAs.setActionCommand(SAVE_AS_COMMAND);
        menuItemSaveAs.addActionListener(this);
        menuFile.add(menuItemSaveAs);

        // bind menu to j frame
        frame.setJMenuBar(menuBar);
    }

    /**
     * Handles all action events for the current form
     */
    @Override
    public void actionPerformed(ActionEvent e) {


        switch (e.getActionCommand()) {
            case NEW_COMMAND:
                newWorld();
                break;
            case OPEN_COMMAND:
                File f = getWorldFileToOpen();
                loadWorld(f);
                break;
            case SAVE_AS_COMMAND:
                worldFile = getWorldFileToSave();
            case SAVE_COMMAND:
                saveWorld(getWorldFile());
                break;
        }

    }

    /**
     * Routines for creating a new world
     */
    private void newWorld() {

        setWorldFile(null);
        pnlDesigner.setWorld(new World());

    }

    /**
     * Create a file chooser dialog for user to select the world file they wish to open
     * @return The world file to open
     */
    private File getWorldFileToOpen() {

        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("World files (*.world)", "world");
        fc.setFileFilter(filter);
        fc.showSaveDialog(pnlContainer);
        fc.setDialogTitle("Load");
        return fc.getSelectedFile();

    }

    /**
     * Create a file chooser dialog for user to select the file location and name they wish to save too
     * @return The world file to save too
     */
    private File getWorldFileToSave() {

        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("World files (*.world)", "world");
        fc.setFileFilter(filter);
        fc.showSaveDialog(pnlContainer);
        fc.setDialogTitle("Save");
        return fc.getSelectedFile();

    }

    /**
     * Attempts to load a world from the given file. Also saves a reference to the file being
     * loaded in the worldFile variable for later use (e.g. fast save)
     * @param f world file to load
     */
    private void loadWorld(File f) {
        try {
            setWorldFile(f);
            WorldReader reader = new WorldReader(f);
            pnlDesigner.setWorld(reader.getWorld());
        } catch (InvalidWorldFileException ex1) {
            ex1.printStackTrace();
            // TODO: prompt user with error
        } catch (java.io.FileNotFoundException ex2) {
            ex2.printStackTrace();
            // TODO: prompt user with error
        }
    }

    /**
     * Attempts to save a world to the given file.
     * @param f world file to save to
     */
    private void saveWorld(File f) {

        try {
            WorldWriter writer = new WorldWriter(f, pnlDesigner.world);
            writer.save();
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: insert error dialog to user
        }


    }
}
