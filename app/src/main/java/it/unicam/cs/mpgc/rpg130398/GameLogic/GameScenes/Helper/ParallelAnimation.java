package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper;

import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Animation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Executes multiple animations at the same time.
 * Finishes only when all contained animations have finished.
 */
public class ParallelAnimation implements Animation {
    private final List<Animation> animations = new ArrayList<>();

    public ParallelAnimation() { }

    public ParallelAnimation(Animation... animations) {
        this(Arrays.asList(animations));
    }

    public ParallelAnimation(Collection<Animation> animations) {
        if (animations == null)
            throw new NullPointerException();
        animations.forEach(this::add);
    }

    /**
     * Adds an animation to execute in parallel with the others.
     * @param animation the animation to add
     */
    public void add(Animation animation) {
        if (animation == null)
            throw new NullPointerException();
        animations.add(animation);
    }

    @Override
    public void update() {
        if (hasFinished()) return;
        for (Animation animation : animations) {
            if (!animation.hasFinished())
                animation.update();
        }
    }

    @Override
    public boolean hasFinished() {
        for (Animation animation : animations) {
            if (!animation.hasFinished())
                return false;
        }
        return true;
    }
}
