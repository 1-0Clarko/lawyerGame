package it.unicam.cs.mpgc.rpg130398.api;

import java.io.IOException;
import java.util.Map;

/**
 * Loads a dialog tree from a file and returns the root node.
 */
public interface DialogTreeLoader {

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
     * @return the map < id, Node >
     * id = Node.getId().
     * id = 0 is the root of the tree
     */
    Map<Integer, DialogNode> getNodes();
}