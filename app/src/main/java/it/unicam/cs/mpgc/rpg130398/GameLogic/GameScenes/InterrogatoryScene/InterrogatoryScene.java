package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.InterrogatoryScene;

import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper.*;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Generic3DObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.*;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.ModelLoader;
import it.unicam.cs.mpgc.rpg130398.Graphics.PLY_ModelLoader;
import it.unicam.cs.mpgc.rpg130398.api.*;

import java.util.ArrayList;

import static it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.InterrogatoryScene.DefendantAnimationsManager.Status.*;

public class InterrogatoryScene implements GameScenes {

    private static final int FPS = 30;
    private static final int TIME_LIMIT_SECONDS = 4 * 60;
    private static final int TIME_LIMIT_FRAMES = TIME_LIMIT_SECONDS * FPS; // 7200 frames
    private long INITIAL_FRAME_NUMBER = -1;

    // External api
    private final Game game;
    private final GraphicsManager graphic;
    private final InputManager input;

    // Scene 3D objects
    private RendableObject table;
    private RendableObject physicalFolder;

    // Managers
    private DialogueWithDefendantManager dialogueManager;
    private DefendantAnimationsManager defendantAnimationManager;

    // Animation handlers
    private final ArrayList<Animation> loopAnimations = new ArrayList<>();
    private AnimationQueue cutSceneAnimations = new AnimationQueue();

    // Time limit / scene end handling
    private boolean timerStopped; // true once KILLED happens: the time limit no longer matters
    private boolean interrogatoryOver; // true once we've moved past the dialog (END reached or time is up)

    public InterrogatoryScene(Game game, GraphicsManager graphic, InputManager input) {
        this.game = game;
        this.graphic = graphic;
        this.input = input;

        dialogueManager = new DialogueWithDefendantManager(graphic, input);
        setupSceneObjects();
        startInitialAnimations();
        
        // TODO REMOVE once finish
        cutSceneAnimations.showNext();
        cutSceneAnimations.showNext();
        cutSceneAnimations.showNext();
    }

    @Override
    public GameScenes update(long frameNumber) {
        if (INITIAL_FRAME_NUMBER == -1)
            INITIAL_FRAME_NUMBER = frameNumber;

        // Updates the loop animations
        for (Animation animation : loopAnimations)
            animation.update();

        if (!cutSceneAnimations.hasFinished()) { // if there is a CutScene, it will block the rest of the logic
            cutSceneAnimations.update();
            return this;
        }

        if (timerStopped)
            return this;
        if (interrogatoryOver)
            return nextScene();

        dialogueManager.update();
        handleTrustEvents();
        handleTimeLimit(frameNumber);
        handleDialogEnd();
        return this;
    }

    private void setupSceneObjects() {
        ModelLoader model = new PLY_ModelLoader(new float[]{-1, 1, 1});
        model.setPath("models/Table.ply");
        table = new Generic3DObject(model);
        graphic.addObject(table);

        model.setPath("models/Folder.ply");
        physicalFolder = new Generic3DObject(model);
        graphic.addObject(physicalFolder);
    }

    private void startInitialAnimations() {
        // setup the defendant
        defendantAnimationManager = new DefendantAnimationsManager(graphic);
        loopAnimations.add(defendantAnimationManager);

        // setup the sitting and inner monologue cut scene
        cutSceneAnimations.add(new SitingAnimation(table, physicalFolder));
        cutSceneAnimations.add(new StartMonologAnimation(graphic));
    }

    /**
     * Maps the current trust value to a defendant animation/game state.
     * Ranges are contiguous and cover every possible trust value with no gaps:
     * trust >= 4 -> TRUSTING, 0 <= trust < 4 -> NEUTRAL, -3 <= trust < 0 -> ANGRY,
     * trust < -3 -> killed.
     */
    private void handleTrustEvents() {
        if (timerStopped) // already killed, nothing left to update here
            return;

        int trust = dialogueManager.dialogLogic.getTrust();
        if (trust >= 4)
            defendantAnimationManager.setAnimationStatus(TRUSTING);
        else if (trust >= 0)
            defendantAnimationManager.setAnimationStatus(NEUTRAL);
        else if (trust >= -3)
            defendantAnimationManager.setAnimationStatus(ANGRY);
        else
            killed();
    }

    /**
     * If the dialog reached its END node, the interrogatory is over and the
     * scene moves on to the defense scene, with no need to wait for the time
     * limit.
     */
    private void handleDialogEnd() {
        if (timerStopped || interrogatoryOver)
            return;

        if ("END".equals(dialogueManager.dialogLogic.getCurrentNode().getFlag()))
            interrogatoryOver = true;
    }

    /**
     * If the time limit is reached before the dialog naturally ends, a guard
     * interrupts the interrogatory and the scene moves on to the defense
     * scene. Does nothing once the defendant has been killed (timerStopped)
     * or the interrogatory is already over.
     */
    private void handleTimeLimit(long frameNumber) {
        if (timerStopped || interrogatoryOver)
            return;

        if (frameNumber-INITIAL_FRAME_NUMBER < TIME_LIMIT_FRAMES)
            return;

        clearUI();
        interrogatoryOver = true;
        loopAnimations.remove(defendantAnimationManager);
        cutSceneAnimations = new AnimationQueue();
        cutSceneAnimations.add(new GuardInterruptionAnimation(graphic));
    }

    private void killed() {
        clearUI();
        timerStopped = true;
        defendantAnimationManager.setAnimationStatus(KILLANIMATION);
        // Moves the animation from a cyclic animation to a blocking animation
        loopAnimations.remove(defendantAnimationManager);
        cutSceneAnimations = new AnimationQueue();
        cutSceneAnimations.add(defendantAnimationManager);
        cutSceneAnimations.add(new KilledAnimation(table, physicalFolder, graphic));
    }
    private void clearUI() {
        dialogueManager.clear();
    }
    /**
     * @return the next scene to move to once the interrogatory is over.
     * TODO: replace with `new DefenseScene(...)` once that scene exists.
     */
    private GameScenes nextScene() {
        return this;
    }
}