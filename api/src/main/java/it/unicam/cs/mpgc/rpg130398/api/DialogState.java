package it.unicam.cs.mpgc.rpg130398.api;

import java.util.Set;

/**
 * Read-only view of the current conversation state, exposed to {@link ConnectionRequirement}
 * implementations so they can evaluate themselves without depending on the
 * concrete {@link Dialog} implementation.
 * <p>
 * This interface intentionally exposes no mutators: a Requirement must only be
 * able to inspect the state, never change it.
 */
public interface DialogState {

    /**
     * @return the current trust value accumulated so far in the conversation
     */
    int getTrust();

    /**
     * @return the set of flags collected so far in the conversation
     */
    Set<String> getCollectedFlags();

    /**
     * @return the set of node ids already visited in the conversation
     */
    Set<Integer> getVisitedNodes();
}