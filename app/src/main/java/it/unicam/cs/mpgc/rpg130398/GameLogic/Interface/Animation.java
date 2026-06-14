package it.unicam.cs.mpgc.rpg130398.GameLogic.Interface;

/**
 * Represents a single animation that updates every frame until it finishes.
 */
public interface Animation {

    /**
     * Updates the animation by one frame. Does nothing if the animation has finished.
     */
    void update();

    /**
     * @return true if the animation has finished
     */
    boolean hasFinished();
}