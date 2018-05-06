// Crack Search Project
// Andrew Nickells
// 201123012
// u5an
// A.P.Nickells@student.liverpool.ac.uk
// University of Liverpool

package cracksearch;

import cracksearch.algorithm.ExplorationAlgorithm;
import cracksearch.algorithm.GreedyAlgorithm;
import cracksearch.algorithm.MSTAlgorithm;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * ExplorationAlgorithmListManager
 *
 * Singleton class used to manage the list of Exploration Algorithms.
 * The Greedy and MST algorithms will always be included in the list at indexes 0 and 1 respectively.
 * All other algorithms will placed at index 2+
 */
public class ExplorationAlgorithmListManager {

    private final static String ALGO_FOLDER_NAME = "ExplorationAlgorithms/";
    private final static String ALGO_FOLDER_PATH = System.getenv("APPDATA") + frmWorldDesigner.APP_DATA_FOLDER + ALGO_FOLDER_NAME;

    private static ExplorationAlgorithmListManager instance = new ExplorationAlgorithmListManager();

    private final List<AlgorithmListUpdatedListener> algorithmListChangedListeners;
    private final File algorithmFolder;

    private Class[] classes;
    private String[] names;

    /**
     * Returns the array of available ExplorationAlgorithms
     * @return array of ExplorationAlgorithm classes
     */
    public Class[] getClasses() {
        return classes;
    }

    /**
     * Returns the array of names of available classes
     * @return array of ExplorationAlgorithm classes
     */
    public String[] getNames() {
        return names;
    }

    /**
     * Returns the instance of ExplorationAlgorithmLoader
     * @return instance of class
     */
    public static ExplorationAlgorithmListManager getInstance() {
        return instance;
    }

    private ExplorationAlgorithmListManager() {

        algorithmListChangedListeners = new LinkedList<>();
        // set up cracksearch.algorithm folder
        algorithmFolder = new File (ALGO_FOLDER_PATH);
        if (!algorithmFolder.exists()) {
            algorithmFolder.mkdir();
        }

        updateAlgorithmList();
    }

    /**
     * Registers a simulation finished listener.
     * @param listener listener to register
     */
    public void addAlgorithmListUpdatedListener(AlgorithmListUpdatedListener listener) {
        algorithmListChangedListeners.add(listener);
    }

    /**
     * Unregisters a simulation finished listener.
     * @param listener listener to unregister
     */
    public void removeAlgorithmListUpdatedListener(AlgorithmListUpdatedListener listener) {
        algorithmListChangedListeners.remove(listener);
    }

    /**
     * Allows the user to select a .class file they would like to load as a custom cracksearch.algorithm.
     * Algorithms must extend the ExplorationAlgorithm class.
     * @param f File containing .class file.
     */
    public void addAlgorithm(File f) {

        if (f != null) {
            if (checkExplorationAlgorithm(f)) {
                if (copyFileToAppData(f)) {
                    updateAlgorithmList();
                }
            }
        }

    }

    /**
     * Checks if a given class (supplied as a File and loaded via class loader) has the
     * super class ExplorationAlgorithm
     * @param f location of class file to load
     * @return true if super class matches, else false
     */
    private boolean checkExplorationAlgorithm(File f) {

        File parent = new File(f.getParent());
        try {
            // load class
            ClassLoader cl = new URLClassLoader(new URL[]{parent.toURI().toURL()});
            Class c = cl.loadClass(stripExtension(f.getName()));
            // check super class matches
            return (checkForCorrectInterface(c.getInterfaces()));

        } catch (MalformedURLException e) {
            e.printStackTrace();
            // TODO: error dialog
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // TODO: error dialog
        }

        // exception occurred,
        return false;

    }

    /**
     * Checks if the ExplorationAlgorithm interface is present in the list of classes provoided
     * @param interfaces List of classes to check
     * @return true if ExplorationAlgorithm is present
     */
    private boolean checkForCorrectInterface(Class[] interfaces) {

        for (Class c: interfaces) {
            if (c.toString().equals(ExplorationAlgorithm.class.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Moves the given file to the folder: AppData/CrackSearch/ExplorationAlgorithm folder
     * @param f file to move
     * @return true if copy successful, else false
     */
    private boolean copyFileToAppData(File f) {

        String new_file_path = ALGO_FOLDER_PATH + f.getName();
        return copyFile(f, new File(new_file_path));

    }

    /**
     * Helper function for copying a file from 1 location to another
     * @param sourceFile file to copy
     * @param destFile file to paste
     * @return true if copy successful, else false
     */
    private boolean copyFile(File sourceFile, File destFile) {

        FileChannel src = null;
        FileChannel dest = null;
        boolean success = false;
        try {
            if(!destFile.exists()) {
                destFile.createNewFile();
            }

            src = new FileInputStream(sourceFile).getChannel();
            dest = new FileOutputStream(destFile).getChannel();
            if (dest.transferFrom(src, 0, src.size()) > 0) {
                success = true;
            }

        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
            // TODO: Error dialog
        } catch (IOException ex2) {
            ex2.printStackTrace();
            // TODO: Error dialog
        } finally {
            try {
                if (src != null) {
                    src.close();
                }
                if (dest != null) {
                    dest.close();
                }
            } catch (IOException ex3) {
                // couldnt close file
                ex3.printStackTrace();
                // TODO: Error dialog
            }
        }

        return success;
    }

    /**
     * Builds the list of custom algorithms found in the app data folder
     * TODO: error reporting
     */
    private void updateAlgorithmList() {
        String[] file_names = algorithmFolder.list();
        classes = new Class[file_names.length + 2];
        names = new String[file_names.length + 2];

        // set greedy and mst algorithms at 0 and 1
        classes[0] = GreedyAlgorithm.class;
        classes[1] = MSTAlgorithm.class;
        names[0] = GreedyAlgorithm.getAlgorithmName();
        names[1] = MSTAlgorithm.getAlgorithmName();
        try {
            ClassLoader cl = new URLClassLoader(new URL[]{algorithmFolder.toURI().toURL()});
            for (int i = 0;i<file_names.length;i++) {
                // get cracksearch.algorithm name & add to list
                classes[i+2] = cl.loadClass(stripExtension(file_names[i]));
                names[i+2] = (String) classes[i+2].getMethod("getAlgorithmName").invoke(null);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }  catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        sendAlgorithmListChangedEvent(names);

    }

    /**
     * Strips the file extension from a given string.
     * E.g. "abc.123" becomes "abc" and "abc.abc.abc" becomes "abc"
     * @param fileName File name to strip extension
     * @return String without ".*"
     */
    private String stripExtension(String fileName) {
        return fileName.substring(0, fileName.indexOf("."));
    }

    /**
     * Sends events to registered listeners
     * @param names array of Names matching classes
     */
    private void sendAlgorithmListChangedEvent(String[] names) {
        for (AlgorithmListUpdatedListener listener: algorithmListChangedListeners) {
            listener.onAlgorithmListUpdatedListener(names);
        }
    }

    /**
     * Removes the selected cracksearch.algorithm from the program.
     */
    public void removeAlgorithm(int index) {

        // make sure selected index is in correct range
        // (cant delete first two items)
        if (index > 1 && index < classes.length) {

            File f = algorithmFolder.listFiles()[index-2];
            if (f.delete()) {
                // update list
                updateAlgorithmList();

            } else {
                //TODO: Error: couldnt delete
            }
        }
    }

}
