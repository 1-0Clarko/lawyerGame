package it.unicam.cs.mpgc.rpg130398.api;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a single node in a dialog tree.
 * The tree is made by linking more DialogNode.
 * A node contains text to display and optional choices leading to other nodes.
 * it can have a flag representing this branch, if is an important node
 */
public interface DialogNode extends Serializable {

    public record Connection(
            int idOther,                  // Nodo id del nodo collegato.
            String selectionMessage,      // Testo mostrato per andare da questo nodo al nodo collegato
            List<ConnectionRequirement> requirements,// Precondizioni da soddisfare per poter usare questo collegamento.
                                                     // Vuota o null significa nessuna condizione per questo collegamento oltre a quelle di base del dialogo.
            int trustDelta                 // Modifica di fiducia da applicare quando si usa il collegamento
    ) {
        /**
         * @return true if every requirement of this connection is satisfied given the current state
         */
        public boolean isSatisfiedBy(DialogState state) {
            if (requirements == null)
                return true;
            for (ConnectionRequirement requirement : requirements)
                if (!requirement.isSatisfied(state))
                    return false;
            return true;
        }
    }

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