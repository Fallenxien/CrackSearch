// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch;

import cracksearch.world.*;
import cracksearch.io.WorldReader;
import cracksearch.io.WorldWriter;
import cracksearch.algorithm.Route;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.*;
import java.util.Properties;

/**
 * cracksearch.frmWorldDesigner
 *
 * Holds all code for creating and managing the World Designer form and all associated events.
 */
@SuppressWarnings("unchecked")
public class frmWorldDesigner implements SimulationFinishedListener, AlgorithmListUpdatedListener {

    // File Location / Extension Constants
    public final static String APP_DATA_FOLDER = "/CrackSearch/";
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
    private JButton btnEditAlg;
    private JButton btnRandomData;
    private JButton btnDeleteCrack;
    private JMenuItem menuItemSave;
    private Properties settings;

    private File worldFile;
    private ExplorationAlgorithmListLoader listLoader;

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
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onWindowClosing();
            }
        });
        frame.setResizable(false);

        // list loader
        listLoader = ExplorationAlgorithmListLoader.getInstance();
        listLoader.addAlgorithmListUpdatedListener(this);

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
     * @param fileName World file name
     */
    private void setWorldName(String fileName) {
        settings.setProperty(LAST_WORLD_PROPERTY, fileName);
        frame.setTitle("Crack Search - " + fileName);
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
     * @param settingsFile File location to create settings at
     */
    private void createDefaultSettings(File settingsFile) {

        // check folder exists
        File parent_folder = new File(settingsFile.getParent());
        if (!parent_folder.exists()) {
            parent_folder.mkdir();
        }

        // add empty values
        settings.putIfAbsent(LAST_WORLD_DIRECTORY_PROPERTY, "");
        settings.putIfAbsent(LAST_WORLD_PROPERTY, "");
        settings.putIfAbsent(LAST_WORLD_PROPERTY, 1);

        writeSettingsFile(settingsFile);

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
        btnDrawCrack.addActionListener(e -> pnlDesigner.enterDrawingMode());

        // cracksearch.algorithm selection combo box
        updateAlgorithmEntries(listLoader.getNames());
        cboAlgorithm.addActionListener(e -> onAlgorithmChanged());

        // run button
        btnRun.addActionListener(e -> onRunSimulationPressed());

        // clear route button
        btnClearRoute.addActionListener(e -> onClearRoutePressed());

        // show report button
        btnShowReport.addActionListener(e -> onShowReportPressed());

        // edit algorithm list button
        btnEditAlg.addActionListener(e -> onEditAlgorithmsPressed());

        // random world data
        btnRandomData.addActionListener(e -> onCreateRandomWorldPressed());

        // delete crack
        btnDeleteCrack.addActionListener(e -> onDeleteCrackPressed());
    }

    /**
     * Opens the edit cracksearch.algorithm form
     */
    private void onEditAlgorithmsPressed() {
        frmAlgorithm algorithm = new frmAlgorithm();
        algorithm.show();
    }

    /**
     * Deletes the selected crack
     */
    private void onDeleteCrackPressed() {
        pnlDesigner.deleteSelectedCrack();
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
        menuItemNew.addActionListener(e -> onNewWorldPressed());
        menuFile.add(menuItemNew);

        // open menu item
        menuItemOpen = new JMenuItem("Open...");
        menuItemOpen.addActionListener(e -> onOpenWorldPressed());
        menuFile.add(menuItemOpen);

        // save menu item
        menuItemSave = new JMenuItem("Save");
        menuItemSave.addActionListener(e -> onSaveWorldPressed());
        menuItemSave.setEnabled(false);
        menuFile.add(menuItemSave);

        // save as menu item
        menuItemSaveAs = new JMenuItem("Save As...");
        menuItemSaveAs.addActionListener(e -> onSaveWorldAsPressed());
        menuFile.add(menuItemSaveAs);

        // bind menu to j frame
        frame.setJMenuBar(menuBar);
    }

    /**
     * Saves the world to the specified world file
     */
    private void onSaveWorldAsPressed() {
        if (getWorldFileToSave()) {
            saveWorld(getWorldFile());
        }
    }

    /**
     * Saves the world to the previous world file
     */
    private void onSaveWorldPressed() {
        saveWorld(getWorldFile());
    }

    /**
     * Opens a world file of the user's choice. Old world is discarded without saving.
     */
    private void onOpenWorldPressed() {
        File f = getWorldFileToOpen();
        if (f != null) {
            loadWorld(f);
        }
    }

    /**
     * Creates a new world. Old world is discarded without saving.
     */
    private void onNewWorldPressed() {
        newWorld();
    }

    /**
     * Clear the current route from the designer. Also disable buttons
     * relying on a route.
     */
    private void onClearRoutePressed() {
        pnlDesigner.removeLastRoute();
    }

    /**
     * Runs the simulation and creates the report form
     * detailing the results of the simulation
     */
    private void onRunSimulationPressed() {
        onAlgorithmChanged();
        pnlDesigner.runSimulation();
    }

    /**
     * Send the exploration cracksearch.algorithm class to the world designer
     * panel
     */
    private void onAlgorithmChanged() {

        Class c;

        if (cboAlgorithm.getSelectedIndex() >= 0) {
            c = listLoader.getClasses()[cboAlgorithm.getSelectedIndex()];

            if (!(c == null)) {
                pnlDesigner.setExplorationAlgorithm(c);
                settings.put(SELECTED_ALGORITHM_PROPERTY, String.valueOf(cboAlgorithm.getSelectedIndex()));
            }
        }
    }

    /**
     * Updates the cboAlgorithm field with an update to date copy of entries
     */
    private void updateAlgorithmEntries(String[] names) {
        // attempt to preserve selected index
        int selected_index = cboAlgorithm.getSelectedIndex();

        // update list
        cboAlgorithm.removeAllItems();
        for (String s: names) {
            cboAlgorithm.addItem(s);
        }

        // check selected index is still ok
        if (selected_index >= names.length) {
            cboAlgorithm.setSelectedIndex(names.length - 1);
        } else {
            cboAlgorithm.setSelectedIndex(selected_index);
        }
    }

    /**
     * Routines for creating a new world
     */
    private void newWorld() {
        onClearRoutePressed();
        setWorldFile(null);
        pnlDesigner.setWorld(new World());
        onAlgorithmChanged();
    }

    /**
     * Prompts the user if they would like to make a random world. Then shows world generator dialog for user
     * to customize settings
     */
    private void onCreateRandomWorldPressed() {

        if (JOptionPane.showConfirmDialog(pnlContainer, "Creating random data will clear the current world data. Are you sure you wish to continue?")
                == JOptionPane.YES_OPTION) {
            dlgWorldGenerator dlg = new dlgWorldGenerator();
            dlg.setVisible(true);
            if (dlg.getResult() == dlgWorldGenerator.Result.OK) {
                pnlDesigner.clearStoredRoutes();
                setWorldFile(null);
                WorldGenerator generator;
                switch (dlg.getGeneratorType()) {
                    case FIXED_LENGTH:
                        generator = new FixedLengthWorldGenerator(dlg.getCrackLength(), World.MAX_WIDTH, World.MAX_HEIGHT);
                        break;
                    case RANDOM:
                    default:
                        generator = new RandomCoordinateWorldGenerator(World.MAX_WIDTH, World.MAX_HEIGHT);
                }
                pnlDesigner.setWorld(generator.generateWorld(dlg.getNumCracks()));
            }
        }

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
            onClearRoutePressed();
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
    private void onShowReportPressed(Route r) {
        report.showReport(r);
    }

    /**
     * Shows the report form.
     */
    private void onShowReportPressed() {
        report.showReport();
    }

    /**
     * Call back function for simulation finished. Creates the report dialog.
     * @param r Route taken
     */
    @Override
    public void onSimulationFinishedListener(Route r) {


        onShowReportPressed(r);
        btnShowReport.setEnabled(true);

    }

    /**
     * Saves the settings file properly and closes down any open forms.
     */
     private void onWindowClosing() {
        File settings_file = new File(System.getenv("APPDATA") + APP_DATA_FOLDER + PROPERTIES_FILE_NAME);
        writeSettingsFile(settings_file);

        report.close();
    }

    /**
     * Updates the cracksearch.algorithm list combo box.
     * Called when cracksearch.algorithm list changes.
     * @param names array of names
     */
    @Override
    public void onAlgorithmListUpdatedListener(String[] names) {
        updateAlgorithmEntries(names);
    }
}
