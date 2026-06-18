package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.InterrogatoryScene;

import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper.*;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Generic3DObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.*;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.ModelLoader;
import it.unicam.cs.mpgc.rpg130398.Graphics.PLY_ModelLoader;
import it.unicam.cs.mpgc.rpg130398.api.*;

import java.util.ArrayList;

public class InterrogatoryScene implements GameScenes {
    // External api
    Game Game;
    GraphicsManager Graphic;
    InputManager Input;

    //scene 3D Objects
    RendableObject Table;
    RendableObject PhysicalFolder;

    //Managers
    DialogueWithDefendantManager DialogueManager;
    DefendantAnimationsManager DefendantAnimationManager;

    // Animations Handlers
    ArrayList<Animation> loopAnimations = new ArrayList<>();
    AnimationQueue CutSceneAnimations = new AnimationQueue();

    public InterrogatoryScene(Game game, GraphicsManager GraphicsManager, InputManager InputManager) {
        this.Game = game;
        this.Graphic = GraphicsManager;
        this.Input = InputManager;
        DialogueManager = new DialogueWithDefendantManager(Graphic);

        SetupSceneObjects();

        StartInitialAnimations();
    }

    @Override
    public GameScenes update(long frameNumber) {
        // Updates the loop animations
        for (Animation animation : loopAnimations)
            animation.update();

        if (!CutSceneAnimations.hasFinished()) { // if there is a CutScene, it will block the rest of the logic
            CutSceneAnimations.update();
            return this;
        }

        DialogueManager.update();

        return this;
    }
    private void SetupSceneObjects () {
        ModelLoader Model = new PLY_ModelLoader(new float[] {-1,1,1});
        Model.setPath("models/Table.ply");
        Table = new Generic3DObject(Model);
        Graphic.addObject(Table);

        Model.setPath("models/Folder.ply");
        PhysicalFolder = new Generic3DObject(Model);
        Graphic.addObject(PhysicalFolder);
    }
    private void StartInitialAnimations() {
        // setup the dependent model and the animation manager for its animations
        DefendantAnimationManager = new DefendantAnimationsManager(Graphic);
        loopAnimations.add(DefendantAnimationManager);

        // setup the sitting and inner monologue cut scene
        CutSceneAnimations.add(new sitingAnimation(Table, PhysicalFolder));
        CutSceneAnimations.add(new startMonologAnimation(Graphic));

        //TODO remove after finishing the scene
        CutSceneAnimations.showNext();
        CutSceneAnimations.showNext();
        CutSceneAnimations.showNext();
    }
}
