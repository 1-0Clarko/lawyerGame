package it.unicam.cs.mpgc.rpg130398.api.dialog;

import java.io.Serializable;

/**
 * Represents a condition that mast be true for a {@link DialogNode.Connection} to be used.
 * <p>
 * It requires a specific DialogState to function
 * For example, can check if the state has a trust value in range, or if the state of the dialog has a flag present, ...
 */
public interface ConnectionRequirement extends Serializable {

    /**
     * @param dialog the dialog form which will be cast the needed DialogState
     * @return true if this requirement is satisfied by the given status, false otherwise
     * @throws IllegalArgumentException if the dialog dose not have the necessary DialogState
     */
    boolean isSatisfiedBy(Dialog dialog);

    /**
     * need to be called from implementations if the dialog to evaluate
     * does not have the necessary DialogState
     */
    default void DialogStateNotPresent () {
        throw new IllegalArgumentException("the given dialog dose not have the necessary DialogState for this connection to be evaluate");
    }

    /**
     * returns un unique identifier of this Requirement
     * for example 'trust' for an implementation which uses the trust of the conversation  or 'flag' for an implementation which uses the flags obtained
     * this is used for serialization and deserialization
     * @return the type
     */
    String getType();
}