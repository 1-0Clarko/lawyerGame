package it.unicam.cs.mpgc.rpg130398.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Loads a dialog tree from a file and returns the node list.
 */
public interface DialogTreeLoader {

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
     * @return the list of read Nodes from the file or null
     */
    ArrayList<DialogNode> getNodes();
}