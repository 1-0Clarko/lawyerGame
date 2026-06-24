package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.TrialScene;

import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper.AnimationQueue;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper.FadeAnimation;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper.MonologueAnimation;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Generic3DObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GenericTextObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Animation;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Game;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.GameScenes;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.ModelLoader;
import it.unicam.cs.mpgc.rpg130398.Graphics.PLY_ModelLoader;
import it.unicam.cs.mpgc.rpg130398.api.InputManager;
import it.unicam.cs.mpgc.rpg130398.api.RendableObject;
import it.unicam.cs.mpgc.rpg130398.api.RendableText;

import java.util.Set;

/**
 * (Incomplete/Place-holder)
 * Last scene of the game.
 *
 * This scene is about the conversation between the player and the judge.
 * The player uses the flags collected in the previous scenes to build a defense
 * for the defendant and influence the final verdict.
 *
 * The available arguments depend on the evidence and information discovered
 * during the investigation and interrogation phases. Missing key flags
 * prevent certain lines of defense from being present.
 *
 * The final score of the player is based on the number of years of prison
 * assigned to the defendant. (lower is better)
 *
 * Depending on the player's choices and collected information, multiple
 * outcomes and endings are possible.
 */
public class TrialScene implements GameScenes {
    // External api
    private final Game game;
    private final GraphicsManager graphic;
    private final InputManager input;

    // Managers
    DialogueWithJudgeManager dialogueManager;

    // Scene 3D objects
    RendableObject table;
    RendableObject folder;
    RendableObject floor;

    // Animation handlers
    private final AnimationQueue cutSceneAnimations = new AnimationQueue();

    // Temporary
    RendableText text;
    final String[] THANKS = {
            "Grazie per aver giocato!",
            "Hai raggiunto la fine della versione attualmente disponibile.",
            "La scena finale del processo, in cui potrai utilizzare le informazioni raccolte\n per costruire la difesa del tuo cliente, non è ancora implementata.",
            "Grazie per il tempo che hai dedicato al gioco!\nqueste sono le flag che hai sbloccato"
    };
    /**
     * @param collectedFlags the flags that the player collected in the interrogatory dialog
     */
    public TrialScene (Game game, GraphicsManager graphic, InputManager input, Set<String> collectedFlags) {
        this.game = game;
        this.graphic = graphic;
        this.input = input;


        dialogueManager = new DialogueWithJudgeManager(graphic, collectedFlags.stream().toList());

        setupSceneObjects();
        setupPlaceHolderText();
        setupAnimations();
    }

    @Override
    public GameScenes update(long frameNumber) {
        // Updates the loop animations
        if (!cutSceneAnimations.hasFinished()) {
            cutSceneAnimations.update();
            return this;
        }
        dialogueManager.update();

        return this;
    }

    private void setupSceneObjects() {
        ModelLoader model = new PLY_ModelLoader(new float[]{-1, 1, 1});
        model.setPath("models/TrialScene/table.ply");
        table = new Generic3DObject(model);
        graphic.addObject(table);

        model.setPath("models/TrialScene/folder.ply");
        folder = new Generic3DObject(model);
        graphic.addObject(folder);

        model.setPath("models/TrialScene/floor.ply");
        floor = new Generic3DObject(model);
        graphic.addObject(floor);
    }

    private void setupPlaceHolderText() {
        text = new GenericTextObject();
        text.setPosition(new float[] {3,3,0});
        text.setSize(4);
        graphic.addText(text);
    }
    private void setupAnimations() {

        ModelLoader model = new PLY_ModelLoader("models/BlackScreen.ply");
        RendableObject blackScreen = new Generic3DObject(model);
        blackScreen.setPosition(new float[] {0,0,0.01f}); // Move a little more far to prevent z fighting with UI elements
        graphic.addObject(blackScreen);
        Animation fadeInTransition = new FadeAnimation(blackScreen, 34, true);
        cutSceneAnimations.add(fadeInTransition);
        cutSceneAnimations.add(new MonologueAnimation(THANKS, text, 0.8f, 30));
    }
}
