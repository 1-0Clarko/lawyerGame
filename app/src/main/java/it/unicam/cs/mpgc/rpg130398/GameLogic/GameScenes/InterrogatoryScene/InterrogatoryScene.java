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

    public InterrogatoryScene(Game game, GraphicsManager graphic, InputManager input) {
        this.game = game;
        this.graphic = graphic;
        this.input = input;

        dialogueManager = new DialogueWithDefendantManager(graphic, input);
        setupSceneObjects();
        startInitialAnimations();
    }

    @Override
    public GameScenes update(long frameNumber) {
        // Updates the loop animations
        for (Animation animation : loopAnimations)
            animation.update();

        if (!cutSceneAnimations.hasFinished()) { // if there is a CutScene, it will block the rest of the logic
            cutSceneAnimations.update();
            return this;
        }

        dialogueManager.update();
        handleTrustEvents();
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

    private void killed() {
        defendantAnimationManager.setAnimationStatus(KILLANIMATION);
        // Moves the animation from a cyclic animation to a blocking animation
        loopAnimations.remove(defendantAnimationManager);
        cutSceneAnimations = new AnimationQueue();
        cutSceneAnimations.add(defendantAnimationManager);
        cutSceneAnimations.add(new KilledAnimation(table, physicalFolder, graphic));
    }
}