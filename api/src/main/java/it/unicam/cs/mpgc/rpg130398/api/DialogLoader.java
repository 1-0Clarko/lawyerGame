package it.unicam.cs.mpgc.rpg130398.api;

import java.io.IOException;

/**
 * Loads a dialog tree from a file and returns the root node.
 */
public interface DialogLoader {

    /**
     * @param path path to the dialog file relative to the working directory
     */
    void setPath(String path);

    /**
     * Reads the file and builds the dialog tree in memory.
     *
     * @throws IOException if the file cannot be read
     * @throws UnsupportedOperationException if the file format is not supported
     */
    void read() throws IOException, UnsupportedOperationException;

    /**
     * @return the root node of the dialog tree,
     * null if the file has not been read successfully
     */
    DialogNode getRoot();
}