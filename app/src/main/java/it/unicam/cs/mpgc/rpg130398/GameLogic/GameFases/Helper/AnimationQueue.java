package it.unicam.cs.mpgc.rpg130398.GameLogic.GameFases.Helper;

import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Animation;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Sequence;

import java.util.ArrayList;
import java.util.List;

/**
 * Executes a queue of Animation.
 * Advances to the next item automatically when the current one finishes.
 */
public class AnimationQueue implements Sequence<Animation> {
    private final List<Animation> queue = new ArrayList<>();
    private int currentIndex = 0;
    private boolean hasFinish;

    /**
     * Adds an animation to the end of the queue.
     * @param animation the animation to add
     */
    public void add(Animation animation) {
        queue.add(animation);
    }

    /**
     * Adds an animation to the end of the queue.
     * @param animation the animation to add
     * @param onStart a method to run on the start of this animation
     */
    public void add(Animation animation, Runnable onStart) {
        if (onStart == null)
            throw new NullPointerException();
        queue.add(new AnimationWithCallback(animation, onStart));
    }

    @Override
    public void update() {
        if (hasFinished()) return;
        if (getCurrent().hasFinished()) {
            if (currentIndex == queue.size()-1)
                hasFinish = true;
            showNext();
        }
        getCurrent().update();
    }

    @Override
    public void showNext() {
        if (hasFinished())
            return;

        currentIndex++;
        Animation A = queue.get(currentIndex);
        if (A instanceof AnimationWithCallback)
            ((AnimationWithCallback)A).onStart().run();
    }

    @Override
    public boolean hasCurrentItemFinished() {
        return hasFinished() || getCurrent().hasFinished();
    }

    @Override
    public boolean hasFinished() {
        return hasFinish;
    }

    @Override
    public Animation getCurrent() {
        return queue.get(currentIndex);
    }

    private record AnimationWithCallback(Animation animation, Runnable onStart) implements Animation {
        @Override
        public void update() { animation.update(); }
        @Override
        public boolean hasFinished() { return animation.hasFinished(); }
    }
}