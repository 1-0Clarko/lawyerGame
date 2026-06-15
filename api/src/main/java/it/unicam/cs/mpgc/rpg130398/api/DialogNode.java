package it.unicam.cs.mpgc.rpg130398.api;

import java.util.Map;

/**
 * Represents a single node in a dialog tree.
 * A node contains text to display and optional choices leading to other nodes.
 * When reached, it can set boolean flags in the game state.
 */
public interface DialogNode {

    /**
     * @return the text to display for this node
     */
    String getText();

    /**
     * @return the available choices from this node, empty if it is a leaf node
     */
    DialogNode[] getChoices();

    /**
     * @return the flags to set when this node is reached, can be empty
     */
    Map<String, Boolean> getFlags();

    /**
     * @return true if this node has no choices (end of a branch)
     */
    boolean isLeaf();
}