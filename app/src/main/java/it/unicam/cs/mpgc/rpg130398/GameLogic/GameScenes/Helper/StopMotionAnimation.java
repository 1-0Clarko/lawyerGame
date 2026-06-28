package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper;

import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Sequence;
import it.unicam.cs.mpgc.rpg130398.api.GraphicsManager;
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
    private int currentIndex = -1;
    private float framePassedForCurrentObject = 0;
    private boolean hasFinish = false;

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
        if (currentIndex != -1) //salta la prima chiamata, perché l'oggetto da nascondere non c'è nella prima chiamata
            graphics.removeObject(models[currentIndex]);

        currentIndex++;
        if (hasFinished()) return;
        if (currentIndex == models.length && loop)
            currentIndex = 0;

        graphics.addObject(models[currentIndex]);
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
        return currentIndex == models.length;
    }

    @Override
    public RendableObject getCurrent() {
        if (hasFinished()) return null;
        if (currentIndex == -1)
            return null;
        return models[currentIndex];
    }
}
