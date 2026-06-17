package it.unicam.cs.mpgc.rpg130398.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

/**
 * It manages a Conversation between two people,
 * One person makes the questions and the other answere
 * the conversation fallows the roules imposed by a given DialogNode linked-tree
 *
 * The conversation ollways starts on the DialogNode with the id 0
 * The 'current node' rappresent where in the tree is the conversation
 */
public interface Dialog {


    /**
     *  Get the choices you can make from the current node
     */
    ArrayList<DialogNode.Connection> getValidChoices();
    /**
     * @return the current node
     */
    DialogNode getCurrentNode ();
    /**
     * from the current node, it will follow the requested connection to a new node
     * The status of the conversation such as the flags or the trust can change when following the node
     *
     * @return true if the connection was valid and it has been followed, false otherwise
     */
    boolean makeChoices(DialogNode.Connection connection);
    /**
     * returns the current trust that the person answering has of the person making the questions
     * @return the trust
     */
    int getTrust();
    /**
     * returns a collection of flags that have been reached in the conversation
     * @return list of flags collected
     */
    Collection<String> getOpinionatedFlags();
}
