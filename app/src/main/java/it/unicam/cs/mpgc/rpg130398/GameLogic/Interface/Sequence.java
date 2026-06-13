package it.unicam.cs.mpgc.rpg130398.GameLogic.Interface;

/**
 * Displays a series of items one at a time.
 * Each item can have an optional animation.
 *
 * @param <T> the type of item to display
 */
public interface Sequence<T> {

    /**
     * Advances to the next item and starts its animation.
     */
    void showNext();

    /**
     * @return true if the current item has finished displaying
     */
    boolean hasFinishedDisplaying();

    /**
     * @return true if there are no more items to display
     */
    boolean hasFinished();

    /**
     * Called every frame to update the animation.
     */
    void update();
    /**
     * @return the current item being displayed
     */
    T getCurrent();
}