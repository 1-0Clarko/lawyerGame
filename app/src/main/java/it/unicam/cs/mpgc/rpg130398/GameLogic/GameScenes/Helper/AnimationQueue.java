package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper;

import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Animation;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Sequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Executes a queue of Animation.
 * Advances to the next item automatically when the current one finishes.
 */
public class AnimationQueue implements Sequence<Animation> {
    private final List<Animation> queue = new ArrayList<>();
    private int currentAnimationIndex = -1;
    private boolean hasFinish = true;

    /**
     * Adds an animation/animations to the end of the queue.
     * @param animations the animation to add
     */
    public void add(Animation... animations) {
        for (Animation animation : animations)
            add(animation, null);
    }
    /**
     * Adds an animation to the end of the queue.
     * @param animation the animation to add
     * @param onStart a method to run on the start of this animation
     */
    public void add(Animation animation, Runnable onStart) {
        if (onStart == null)
            queue.add(animation);
        else
            queue.add(new AnimationWithCallback(animation, onStart));
        hasFinish = false;
    }

    @Override
    public void update() {
        if (hasFinished()) return;
        if (currentAnimationIndex == -1 || getCurrent().hasFinished()) {
            showNext();
        }
        getCurrent().update();
    }

    @Override
    public void showNext() {
        if (currentAnimationIndex == queue.size()-1)
            hasFinish = true;
        if (hasFinished())
            return;
        currentAnimationIndex++;
        Animation A = queue.get(currentAnimationIndex);
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
        return queue.get(currentAnimationIndex);
    }

    private record AnimationWithCallback(Animation animation, Runnable onStart) implements Animation {
        @Override
        public void update() { animation.update(); }
        @Override
        public boolean hasFinished() { return animation.hasFinished(); }
    }
}