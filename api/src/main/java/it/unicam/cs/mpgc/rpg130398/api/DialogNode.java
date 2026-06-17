package it.unicam.cs.mpgc.rpg130398.api;

import java.io.Serializable;

/**
 * Represents a single node in a dialog tree.
 * The tree is made by linking more DialogNode.
 * A node contains text to display and optional choices leading to other nodes.
 * When reached, it can have a flag representing this branch has been reached
 */
public interface DialogNode extends Serializable {
    public record Connection(
            int id,                           // Nodo di destinazione.
            String selectionMessage,          // Testo mostrato come scelta del giocatore.
            int requiredTrust                 // Fiducia minima richiesta per rendere
                                              // disponibile questa scelta.
    ) {}
    /**
     * @return the text to display for this node
     */
    String getText();

    /**
     * @return the available id of this node children
     * the children are also DialogNode
     * can't be null, can be of size 0
     */
    Connection[] getConnection();

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
    /**
     * Return how much reputation should be gained by the person that has made the question if he reaches this node
     * Can be negative
     *
     * @return reputation delta
     */
    int getReputationGain();
}