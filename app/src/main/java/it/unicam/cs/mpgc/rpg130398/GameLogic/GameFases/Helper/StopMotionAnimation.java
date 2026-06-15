package it.unicam.cs.mpgc.rpg130398.GameLogic.GameFases.Helper;

import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Sequence;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.api.RendableObject;

/**
 * Displays a series of 3D models one at a time by adding and removing them from the scene.
 * Each model is shown for a given number of frames before advancing to the next.
 */
public class StopMotionAnimation implements Sequence<RendableObject> {
    private final RendableObject[] models;
    private final GraphicsManager graphics;
    private final float frameInterval;
    private final boolean loop;
    private int currentObject = 0;
    private float framePassedForCurrentObject = 0;

    /**
     * @param graphics the graphics manager used to add and remove models
     * @param frameInterval number of frames each model is shown
     * @param loop if true, restarts from the first model when the last one finishes
     * @param models the models to display in order
     */
    public StopMotionAnimation(GraphicsManager graphics, float frameInterval, boolean loop, RendableObject[] models) {
        this.graphics = graphics;
        this.frameInterval = frameInterval;
        framePassedForCurrentObject = frameInterval; // To show immediately the first object
        this.loop = loop;
        this.models = models;
    }

    @Override
    public void showNext() {
        if (hasFinished()) return;
        if (currentObject >= 0) { //Dal secondo in poi
            graphics.removeObject(models[currentObject]);
        }
        currentObject++;
        if (loop && currentObject >= models.length)
            currentObject = 0;

        graphics.addObject(models[currentObject]);
        framePassedForCurrentObject = 0;
    }

    @Override
    public void update() {
        if (hasFinished()) return;
        framePassedForCurrentObject++;
        if (framePassedForCurrentObject >= frameInterval)
            showNext();
    }

    @Override
    public boolean hasCurrentItemFinished() {
        return hasFinished() || framePassedForCurrentObject >= frameInterval;
    }

    @Override
    public boolean hasFinished() {
        if (loop)
            return false;
        return currentObject >= models.length;
    }

    @Override
    public RendableObject getCurrent() {
        if (hasFinished()) return null;
        return models[currentObject];
    }
}
