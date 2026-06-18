package it.unicam.cs.mpgc.rpg130398.api;

/**
 * Provides input state from the user.
 * Abstracts the input system to allow different platforms (desktop, mobile, web) and different framework.
 *
 */
public interface InputManager {

    /**
     * @return true if the user is left-clicking with the mouse or tapping
     */
    boolean isCursorPressed();
    /**
     * @return true if the user is left-clicking with the mouse or tapping in this logic frame
     */
    boolean isCursorJustPressed();

    /**
     * @return the position [x, y] of the Cursor in world space, or null if not available
     */
    float[] getCursorPos();
}