package it.unicam.cs.mpgc.rpg130398.api;

/**
 * Represents an object that tracks whether it has changed externally.
 * The dirty flag signals that the implementation class has significantly changed.
 *
 * it is useful for another 'handler' object to update only when necessary
 */
public interface Dirtable {
    /**
     * @return true if this object has changed and needs to be re-readed
     */
    boolean isDirty();

    /**
     * Called by some handler when it has acknowledged the changes
     */
    void clearDirty();
}