package it.unicam.cs.mpgc.rpg130398.api;

import java.io.Serializable;

/**
 * Represents a single node in a dialog tree.
 * A node contains text to display and optional choices leading to other nodes.
 * When reached, it can have a flag representing this node branch
 */
public interface DialogNode extends Serializable {
    public record Child(int childrenId, String selectionMessage) {}

    /**
     * @return the text to display for this node
     */
    String getText();

    /**
     * @return the available id of this node children
     * the children are also DialogNode
     * can't be null, can be of size 0
     */
    Child[] getChildrens();

    /**
     * @return the flag of this node, can be null
     */
    String getFlag();

    /**
     * @return true if this node has no choices (end of a branch)
     */
    boolean isLeaf();

    /**
     * @return the unique identifier of this node
     */
    int getId();

    /**
     * @return true if this node has already been visited
     */
    boolean isVisited();

    /**
     * Marks this node as visited
     */
    void markVisited();
}