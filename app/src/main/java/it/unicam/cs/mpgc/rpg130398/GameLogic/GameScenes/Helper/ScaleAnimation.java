package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper;

import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Animation;
import it.unicam.cs.mpgc.rpg130398.api.RendableObject;
import it.unicam.cs.mpgc.rpg130398.api.Vertex;

public class ScaleAnimation implements Animation {
    private final ScaleModifier scaleModifier;
    private final int durationFrames;
    private final float finalScale;
    private int frame = 0;

    public ScaleAnimation(RendableObject target, int durationFrames, float finalScale) {
        scaleModifier = new ScaleModifier(target);
        this.durationFrames = durationFrames;
        this.finalScale = finalScale;
    }

    @Override
    public void update() {
        if (hasFinished()) return;
        float t = frame / (float) (durationFrames-1);
        scaleModifier.setScale(1+t*finalScale);
        frame++;
    }
    @Override
    public boolean hasFinished() {
        return frame >= durationFrames;
    }

    /**
     * Modifies the scale of a RendableObject vertices.
     */
    private static class ScaleModifier {
        private final RendableObject target;
        private final float[][] originalPositions;

        /**
         * @param target the object whose scale will be modified
         */
        public ScaleModifier(RendableObject target) {
            this.target = target;
            Vertex[] vertices = target.getObjectVertices();
            originalPositions = new float[vertices.length][3];
            for (int i = 0; i < vertices.length; i++)
                originalPositions[i] = vertices[i].getPosition().clone();
        }

        /**
         * Sets the scale of the target object.
         * Reads the current vertices, modifies their position value and sends them back.
         *
         * @param scale scale factor, where 0.0 collapses the object and 1.0 restores its original size
         */
        public void setScale(float scale) {
            scale = Math.max(0f, scale);

            Vertex[] vertices = target.getObjectVertices();
            for (int i = 0; i < vertices.length; i++) {
                float[] original = originalPositions[i];
                vertices[i].setPosition(new float[]{
                        original[0] * scale,
                        original[1] * scale,
                        original[2] * scale
                });
            }

            target.setObjectVertices(vertices);
        }
    }
}
