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
    private final List<AnimationWithCallback> queue = new ArrayList<>();
    private int currentAnimationIndex = -1;

    /**
     * Adds an animation/animations to the end of the queue.
     * @param animations the animation to add
     */
    public void add(Animation... animations) {
        for (Animation animation : animations)
            add(animation, null, null);
    }
    /**
     * Adds an animation to the end of the queue.
     * @param animation the animation to add
     * @param onStart a method to run on the start of this animation
     */
    public void add(Animation animation, Runnable onStart) {
        add(animation, onStart, null);
    }
    /**
     * Adds an animation to the end of the queue.
     * @param animation the animation to add
     * @param onStart a method to run on the start of this animation
     * @param onEnd a method to run on the end of this animation
     */
    public void add(Animation animation, Runnable onStart, Runnable onEnd) {
        queue.add(new AnimationWithCallback(animation, onStart, onEnd));
    }

    @Override
    public void update() {
        if (hasFinished()) return;
        if (currentAnimationIndex == -1)
            showNext();

        getCurrent().update();
        if (getCurrent().hasFinished()) {
            showNext();
            update(); // instantly show the first frame of the next animation
        }

    }

    @Override
    public void showNext() {
        if (hasFinished())
            return;

        if (currentAnimationIndex != -1) {
            AnimationWithCallback A = queue.get(currentAnimationIndex);
            Runnable onEnd = A.onEnd();
            if (onEnd != null)
                onEnd.run();
        }
        currentAnimationIndex++;

        if (hasFinished())
            return;
        AnimationWithCallback A = queue.get(currentAnimationIndex);
        Runnable onStart = A.onStart();
        if (onStart != null)
            onStart.run();
    }

    @Override
    public boolean hasCurrentItemFinished() {
        return hasFinished() || getCurrent().hasFinished();
    }

    @Override
    public boolean hasFinished() {
        return currentAnimationIndex == queue.size();
    }

    @Override
    public Animation getCurrent() {
        return queue.get(currentAnimationIndex);
    }

    private record AnimationWithCallback(Animation animation, Runnable onStart, Runnable onEnd) implements Animation {
        @Override
        public void update() { animation.update(); }
        @Override
        public boolean hasFinished() { return animation.hasFinished(); }
    }
}