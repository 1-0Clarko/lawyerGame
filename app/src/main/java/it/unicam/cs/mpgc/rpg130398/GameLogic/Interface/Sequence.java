package it.unicam.cs.mpgc.rpg130398.GameLogic.Interface;

/**
 * Displays a series of items one at a time.
 * Each item can have an optional animation.
 *
 * update will continue the animation of the current item,
 * or it will start the animation of the next
 * @param <T> the type of item to display
 */
public interface Sequence<T> extends Animation {

    /**
     * Advances to the next item and starts its animation.
     * may skip the animation of the current item
     */
    void showNext();

    /**
     * @return true if the current item has finished displaying
     */
    boolean hasCurrentItemFinished();

    /**
     * @return true if there are no more items to display
     */
    @Override
    boolean hasFinished();

    /**
     * Called every frame to update the animation of the current item.
     */
    @Override
    void update();
    /**
     * @return the current item being displayed
     */
    T getCurrent();
}