package it.unicam.cs.mpgc.rpg130398.api;

import java.io.Serializable;

/**
 * Represents a condition that mast be true for a {@link DialogNode.Connection} to be used.
 * <p>
 * Implementations encapsulate one kind of check, for example (dialog trust in range, flag present, ...).
 *
 */
public interface ConnectionRequirement extends Serializable {

    /**
     * @param state the status of the conversation
     * @return true if this requirement is satisfied by the given status
     */
    boolean isSatisfied(DialogState state);

    /**
     * returns an unique identifier of this Requirement
     * for example 'trust' for an implementation which uses the trust of the conversation  or 'flag' for an implementation which uses the flags obtained
     * this is used for serialization and deserialization
     * @return the type
     */
    String getType();
}