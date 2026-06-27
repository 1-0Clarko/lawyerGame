package it.unicam.cs.mpgc.rpg130398.api.dialog;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Loads a dialog from a file and returns the node list.
 */
public interface DialogLoader {

    /**
     * @param path path to the dialog file to read, relative to the working directory
     */
    void setPath(String path);

    /**
     * Reads the file and saves the DialogNodes in ram
     *
     * @throws IOException if the file cannot be read
     * @throws UnsupportedOperationException if the file format is not supported
     */
    void read() throws IOException, UnsupportedOperationException;

    /**
     * Returns the list of read Nodes from the file or null if the file is not bean read
     * @return list of read Nodes or null
     */
    ArrayList<DialogNode> getNodes();
    /**
     * @return the concrete Dialog implementation class declared by the loaded file
     */
    Class<? extends Dialog> getDialogClass();
}