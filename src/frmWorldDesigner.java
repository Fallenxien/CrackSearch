// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.*;
import java.util.Properties;

/**
 * frmWorldDesigner
 *
 * Holds all code for creating and managing the World Designer form and all associated events.
 */
@SuppressWarnings("unchecked")
public class frmWorldDesigner implements ActionListener, SimulationFinishedListener, WindowListener {

    // Action Listener commands
    private final static String NEW_COMMAND = "new";
    private final static String OPEN_COMMAND = "open";
    private final static String SAVE_COMMAND = "save";
    private final static String SAVE_AS_COMMAND = "save_as";
    private final static String ENTER_DRAW_MODE_COMMAND = "draw_mode";
    private final static String SELECTED_ALGORITHM_CHANGED_COMMAND = "algo_changed";
    private final static String RUN_COMMAND = "run_changed";
    private final static String CLEAR_ROUTE_COMMAND = "clear_route";
    private final static String SHOW_REPORT_COMMAND = "show_report";

    // File Location / Extension Constants
    private final static String APP_DATA_FOLDER = "/CrackSearch/";
    private final static String PROPERTIES_FILE_NAME = "config.properties";
    private static final String WORLD_FILE_EXTENSION = ".world";

    // Settings File Keys
    private final static String LAST_WORLD_DIRECTORY_PROPERTY = "lastWorldFolderPath";
    private final static String LAST_WORLD_PROPERTY = "lastWorld";
    private final static String SELECTED_ALGORITHM_PROPERTY = "selectedExplorationAlgorithm";

    private final JFrame frame;
    private final frmReport report;
    private JPanel pnlContainer;
    private PanelWorldDesigner pnlDesigner;
    private JButton btnDrawCrack;
    private JComboBox cboAlgorithm;
    private JButton btnRun;
    private JButton btnClearRoute;
    private JButton btnShowReport;
    private JMenuItem menuItemSave;
    private Properties settings;

    private File worldFile;

    /**
     * Gets the directory path of the last world file from settings
     * @return String containing directory
     */
    private String getLastWorldDirectory() {
        if (settings.containsKey(LAST_WORLD_DIRECTORY_PROPERTY)) {
            return settings.getProperty(LAST_WORLD_DIRECTORY_PROPERTY);
        } else {
            // value not present in settings file
            // add blank placeholder
            settings.put(LAST_WORLD_DIRECTORY_PROPERTY, "");
            return "";
        }
    }

    /**
     * Sets the directory path of the last world file from settings
     * @param directory path of directory
     */
    private void setLastWorldDirectory(String directory) {
        settings.setProperty(LAST_WORLD_DIRECTORY_PROPERTY, directory);
    }

    /**
     * Gets the name of the world file in settings
     * @return String containing name
     */
    private String getWorldName() {

        if (settings.containsKey(LAST_WORLD_PROPERTY)) {
            return settings.getProperty(LAST_WORLD_PROPERTY);
        } else {
            // value not present in settings file
            // add blank placeholder
            settings.put(LAST_WORLD_PROPERTY, "");
            return "";
        }
    }

    /**
     * Sets the file name of the world in settings
     * @param file_name World file name
     */
    private void setWorldName(String file_name) {
        settings.setProperty(LAST_WORLD_PROPERTY, file_name);
        frame.setTitle("Crack Search - " + file_name);
    }

    /**
     * Sets the world file to use
     * @param f File to use
     */
    private void setWorldFile(File f) {
        if (f != null) {
            // check extension is correct
            if (!checkFileExtension(f)) {
                f = new File(f.getPath() + WORLD_FILE_EXTENSION);
            }
            menuItemSave.setEnabled(true);
            setLastWorldDirectory(f.getParent() + "\\");
            setWorldName(f.getName());
        } else {
            menuItemSave.setEnabled(false);
            setLastWorldDirectory("");
            setWorldName("");
        }

        worldFile = f;
    }

    /**
     * Gets the current world file
     * @return file of current world
     */
    private File getWorldFile() {
        return worldFile;
    }

    /**
     * Initial Constructor, called from main.java
     */
    public frmWorldDesigner() {

        // load any program variables
        loadSettings();

        // create jframe
        frame = new JFrame("Crack Search");
        frame.setContentPane(this.pnlContainer);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.addWindowListener(this);
        frame.setResizable(false);

        // report frame
        report = new frmReport();

        // register for sim finished events
        pnlDesigner.addSimulationFinishedListener(this);

        // build UI
        buildMenuBar();
        buildToolBox();
        loadInitialWorld();
        getExplorationAlgorithmFromSettings();

    }

    /**
     *
     */
    private void getExplorationAlgorithmFromSettings() {

        int selected_index = -1;

        if (settings.containsKey(SELECTED_ALGORITHM_PROPERTY)) {
            Object val = settings.get(SELECTED_ALGORITHM_PROPERTY);
            // data validation checks, first check data type is a match
            try {
                int val_int = Integer.parseInt((String) val);
                if (val_int >= 0 && val_int < cboAlgorithm.getItemCount()) {
                    // success
                    selected_index = val_int;
                }
            } catch (NumberFormatException ex1) {
                // bad format (not a number), do nothing here. We'll fix it at the end
            }
        }

        // error reading index, default to first item
        if (selected_index == -1) {
            selected_index = 0;
        }

        // set index on combo box
        cboAlgorithm.setSelectedIndex(selected_index);
    }

    /**
     * Attempts to load the last world used, if no world is present loads an empty world
     */
    private void loadInitialWorld() {
        // check if last world exists, load if possible
        if (!(getLastWorldDirectory().isEmpty() && getWorldName().isEmpty())) {
            File world_file = new File(getLastWorldDirectory() + getWorldName());
            if (world_file.exists()) {
                loadWorld(world_file);
                return;
            }
        }

        // no world file, create a blank world
        newWorld();

    }

    /**
     * Load any program settings before initializing the forms
     */
    private void loadSettings() {

        settings = new Properties();
        File settings_file = new File(System.getenv("APPDATA") + APP_DATA_FOLDER + PROPERTIES_FILE_NAME);
        if (settings_file.exists()) {
            try {
                InputStream input = new FileInputStream(settings_file);
                settings.load(input);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                // no settings, create a default copy
                // code shouldn't ever reach here, but redundancy for safety is never bad
                createDefaultSettings(settings_file);
            } catch (IOException e) {
                // error while reading settings
                createDefaultSettings(settings_file);
                e.printStackTrace();
            }
        } else {
            // no settings, create a default copy
            createDefaultSettings(settings_file);
        }

    }

    /**
     * Create a default settings file at the given location
     * @param settings_file File location to create settings at
     */
    private void createDefaultSettings(File settings_file) {

        // check folder exists
        File parent_folder = new File(settings_file.getParent());
        if (!parent_folder.exists()) {
            parent_folder.mkdir();
        }

        // add empty values
        settings.putIfAbsent(LAST_WORLD_DIRECTORY_PROPERTY, "");
        settings.putIfAbsent(LAST_WORLD_PROPERTY, "");
        settings.putIfAbsent(LAST_WORLD_PROPERTY, 1);

        writeSettingsFile(settings_file);

    }

    /**
     * Write settings to the given file
     * @param file File to write settings too
     */
    private void writeSettingsFile(File file) {

        try {
            OutputStream writer = new FileOutputStream(file);
            settings.store(writer,null);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Builds the toolbox controls
     */
    private void buildToolBox() {

        // add crack button
        btnDrawCrack.setActionCommand(ENTER_DRAW_MODE_COMMAND);
        btnDrawCrack.addActionListener(this);

        // algorithm selection combo box
        cboAlgorithm.addItem(GreedyAlgorithm.getAlgorithmName());
        cboAlgorithm.addItem(MSTAlgorithm.getAlgorithmName());
        cboAlgorithm.setActionCommand(SELECTED_ALGORITHM_CHANGED_COMMAND);
        cboAlgorithm.addActionListener(this);

        // run button
        btnRun.setActionCommand(RUN_COMMAND);
        btnRun.addActionListener(this);

        // clear route button
        btnClearRoute.setActionCommand(CLEAR_ROUTE_COMMAND);
        btnClearRoute.addActionListener(this);

        // show report button
        btnShowReport.setActionCommand(SHOW_REPORT_COMMAND);
        btnShowReport.addActionListener(this);

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
                if (f != null) {
                    loadWorld(f);
                }
                break;
            case SAVE_AS_COMMAND:
                if (!getWorldFileToSave()) {
                    break;
                }
            case SAVE_COMMAND:
                saveWorld(getWorldFile());
                break;
            case ENTER_DRAW_MODE_COMMAND:
                pnlDesigner.enterDrawingMode();
                break;
            case SELECTED_ALGORITHM_CHANGED_COMMAND:
                changeAlgorithm();
                break;
            case RUN_COMMAND:
                runSimulation();
                break;
            case CLEAR_ROUTE_COMMAND:
                clearRoute();
                break;
            case SHOW_REPORT_COMMAND:
                showReport();
        }

    }

    /**
     * Clear the current route from the designer. Also disable buttons
     * relying on a route.
     */
    private void clearRoute() {
        pnlDesigner.clearRoute();
        btnClearRoute.setEnabled(false);
        btnShowReport.setEnabled(false);
    }

    /**
     * Runs the simulation and creates the report form
     * detailing the results of the simulation
     */
    private void runSimulation() {
        pnlDesigner.runSimulation();
    }

    /**
     * Send the exploration algorithm class to the world designer
     * panel
     */
    private void changeAlgorithm() {

        Class c = null;

        switch (cboAlgorithm.getSelectedIndex()) {
            case 0:
                c = GreedyAlgorithm.class;
                break;
            case 1:
                c = MSTAlgorithm.class;
                break;
        }

        if (!(c == null)) {
            pnlDesigner.setExplorationAlgorithm(c);
            settings.put(SELECTED_ALGORITHM_PROPERTY, String.valueOf(cboAlgorithm.getSelectedIndex()));
        }
    }

    /**
     * Routines for creating a new world
     */
    private void newWorld() {
        setWorldFile(null);
        pnlDesigner.setWorld(new World());
        changeAlgorithm();
    }

    /**
     * Create a file chooser dialog for user to select the world file they wish to open
     * @return The world file to open
     */
    private File getWorldFileToOpen() {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(getLastWorldDirectory()));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("World files (*" + WORLD_FILE_EXTENSION + ")", "world");
        fc.setFileFilter(filter);
        if (fc.showOpenDialog(pnlContainer) == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        }
        return null;
    }

    /**
     * Create a file chooser dialog for user to select the file location and name they wish to save too
     * @return True if successfully updated worldFile, else false
     */
    private boolean getWorldFileToSave() {

        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(getLastWorldDirectory()));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("World files (*" + WORLD_FILE_EXTENSION + ")", "world");
        fc.setFileFilter(filter);
        if (fc.showSaveDialog(pnlContainer) == JFileChooser.APPROVE_OPTION) {
            setWorldFile(fc.getSelectedFile());
            return true;
        }

        // no file selected
        return false;
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
            JOptionPane.showMessageDialog(frame,"File format is not supported.");
        } catch (FileNotFoundException ex2){
            ex2.printStackTrace();
            JOptionPane.showMessageDialog(frame,"File not found.");
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
            JOptionPane.showMessageDialog(frame,"Error saving world file.");
        }
    }

    /**
     * Checks if a given file has the correct file extension (.world)
     * @param f File to check
     * @return true if extension is correct, else false
     */
    private boolean checkFileExtension(File f) {
        String path = f.getPath();
        int extension_pos = path.lastIndexOf(".");

        if (extension_pos == -1) {
            // no extension present
            return false;
        } else {
            if (!path.substring(extension_pos).equals(WORLD_FILE_EXTENSION)) {
                // wrong extension
                return false;
            }
        }
        // correct extension
        return true;
    }

    /**
     * Shows the report form and tells it to update the report to the
     * supplied route
     * @param r Route to report
     */
    private void showReport(Route r) {
        report.showReport(r);
    }

    /**
     * Shows the report form.
     */
    private void showReport() {
        report.showReport();
    }

    /**
     * Call back function for simulation finished. Creates the report dialog.
     * @param r Route taken
     */
    @Override
    public void simulationFinished(Route r) {


        showReport(r);
        btnClearRoute.setEnabled(true);
        btnShowReport.setEnabled(true);

    }

    @Override
    public void windowOpened(WindowEvent e) { }

    @Override
    public void windowClosing(WindowEvent e) {
        File settings_file = new File(System.getenv("APPDATA") + APP_DATA_FOLDER + PROPERTIES_FILE_NAME);
        writeSettingsFile(settings_file);

        report.close();
    }

    @Override
    public void windowClosed(WindowEvent e) { }

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
