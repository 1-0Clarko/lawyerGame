package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper;

import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Animation;
import it.unicam.cs.mpgc.rpg130398.api.RendableObject;

/**
 * Animates a RendableObject from a start position and rotation to an end position and rotation
 * over a given number of frames.
 */
public class TransformAnimation implements Animation {
    private final RendableObject target;

    private final float[] startPos;
    private final float[] endPos;
    private final float[] startRot;
    private final float[] endRot;

    private final int durationFrames;
    private int frame = 0;
    private Easing inUseEasing;

    public enum Easing {
        LINEAR,
        EASE_IN,   // lento all'inizio, veloce alla fine
        EASE_OUT,  // veloce all'inizio, lento alla fine
        EASE_IN_OUT
    }

    /**
     * @param target the object to animate
     * @param startPos start position [x, y, z]
     * @param endPos end position [x, y, z]
     * @param startRot start rotation [x, y, z] in degrees
     * @param endRot end rotation [x, y, z] in degrees
     * @param durationFrames number of frames to complete the animation
     */
    public TransformAnimation(RendableObject target, float[] startPos, float[] endPos, float[] startRot, float[] endRot, int durationFrames, Easing movimentType) {
        this.target = target;
        this.startPos = startPos;
        this.endPos = endPos;
        this.startRot = startRot;
        this.endRot = endRot;
        this.durationFrames = durationFrames;
        this.inUseEasing = movimentType;
    }

    @Override
    public void update() {
        if (hasFinished()) return;
        float t = frame / (float)(durationFrames - 1);
        t = applyEasing(t);

        target.setPosition(new float[]{
                lerp(startPos[0], endPos[0], t),
                lerp(startPos[1], endPos[1], t),
                lerp(startPos[2], endPos[2], t)
        });
        target.setRotation(new float[]{
                lerp(startRot[0], endRot[0], t),
                lerp(startRot[1], endRot[1], t),
                lerp(startRot[2], endRot[2], t)
        });
        frame++;
    }

    @Override
    public boolean hasFinished() {
        return frame >= durationFrames;
    }

    private float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private float applyEasing(float t) {
        return switch (inUseEasing) {
            case Easing.LINEAR -> t;
            case Easing.EASE_IN -> t * t;
            case Easing.EASE_OUT -> t * (2 - t);
            case Easing.EASE_IN_OUT -> t < 0.5f ? 2 * t * t : -1 + (4 - 2 * t) * t;
        };
    }
}