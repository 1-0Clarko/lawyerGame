package it.unicam.cs.mpgc.rpg130398.api.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * It manages a Conversation between two people,
 * One person makes the questions and the other answere
 * the conversation fallows the paths imposed by a given DialogNode array and the connections between nodes
 *
 * The conversation ollways starts on the DialogNode with the id 0
 * The CurrentNode rappresent where is the conversation inside the DialogNode array,
 * at the begining the CurrentNode will be the node with id 0.
 * Is not allower to use the same Choice/Connection more the once. This blocks any loops in the dialog
 */
public interface Dialog {
    /**
     *  Get the choices you can make from the current node
     */
    ArrayList<DialogNode.Connection> getValidChoices();

    /**
     * @return the current node
     */
    DialogNode getCurrentNode();

    /**
     * from the current node, it will follow the requested connection to a new node
     * The status of the conversation such as the opinionated flags or the trust can change when following the node
     *
     * @return true if the connection was valid and it has been followed, false otherwise
     */
    boolean makeChoices(DialogNode.Connection connection);

    /**
     * returns the node how has the given id
     * @return the found node or null if this id is not present in this dialog
     */
    DialogNode getNodeFromID(int id);

    /**
     * @return the set of node it has already visited in the conversation
     * in order of visit (0 is the first)
     */
    Set<Integer> getVisitedNodes();
    /**
     * @return the list of flags in order of discovery
     */
    List<String> getCollectedFlags();
}