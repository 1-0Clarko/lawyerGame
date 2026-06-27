package it.unicam.cs.mpgc.rpg130398.api.dialog;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Represents a single node in a DialogNode connected network.
 * A node contains text to display and connections leading to other nodes.
 * it can have a flag representing this node, which are used for important nodes
 */
public interface DialogNode extends Serializable {

    public record Connection(
            int idOther,                  // Nodo id del nodo collegato.
            String selectionMessage,      // Testo mostrato per andare da questo nodo al nodo collegato
            List<ConnectionRequirement> requirements, // Condizioni che il dialogo deve soddisfare per poter usare questo collegamento.
                                                      // Vuota o null significa nessuna condizione per questo collegamento oltre a quelle di base del dialogo.

            Map<String, Float> attributes // Attributi associati a questa connessione.
                                          // Esempio: ("trustDelta", 1f) può essere un attributo per un dialogo che
                                          // utilizza un sistema di fiducia. Per indicare l'incremento di 1 al parametro di
                                          // qunado percorso questo collegamento
                                          // gli attributi vengono definiti negli javadoc del dialogo
    ) {
        /**
         * @return true if every requirement of this connection is satisfied by the given dialog
         */
        public boolean isSatisfiedBy(Dialog dialog) {
            if (requirements == null)
                return true;
            for (ConnectionRequirement requirement : requirements)
                if (!requirement.isSatisfiedBy(dialog))
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