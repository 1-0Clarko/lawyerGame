package it.unicam.cs.mpgc.rpg130398.api;

import java.io.Serializable;

/**
 * Represents a single node in a dialog tree.
 * The tree is made by linking more DialogNode.
 * A node contains text to display and optional choices leading to other nodes.
 * it can have a flag representing this branch, if is an important node
 */
public interface DialogNode extends Serializable {
    public record Connection(
            int idOther,                      // Nodo id del nodo collegato.
            String selectionMessage,          // Testo mostrato per andare da questo nodo al nodo collegato
            int minRequiredTrust,             // Fiducia minima richiesta per andare da questo nodo al nodo collegato
            int maxRequiredTrust,             // Fiducia massima richiesta per andare da questo nodo al nodo collegato
            boolean RequireTrust,             // Dice se controllare o meno che siano rispettati i campi minRequiredTrust e maxRequiredTrust
            int TrustDelta                    // Modifica di fiducia da applicare quando si usa il collegamento
    ) {}
    /**
     * @return the text to display of this node
     */
    String getText();

    /**
     * Returns the connections starting from this node
     * @return Connection array
     * can't be null, can be of size 0
     */
    Connection[] getConnection();

    /**
     * @return the flag of this node, can be null
     */
    String getFlag();

    /**
     * @return true if there aren't any connections starting from this node (end of a branch)
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