package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.InterrogatoryScene;

import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper.*;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Generic3DObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.*;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.ModelLoader;
import it.unicam.cs.mpgc.rpg130398.Graphics.PLY_ModelLoader;
import it.unicam.cs.mpgc.rpg130398.api.*;

public class InterrogatoryScene implements GameScenes {
    Game Game;
    GraphicsManager Graphic;

    //scene 3D Objects
    RendableObject Table;
    RendableObject PhysicalFolder;

    DialogueManager DialogueManager;

    // Animations
    DefendantAnimationsManager DefendantAnimationManager;
    AnimationQueue CutSceneAnimations;

    public InterrogatoryScene(Game game, GraphicsManager Gm) {
        this.Game = game;
        this.Graphic = Gm;
        DialogueManager = new DialogueManager(Graphic);

        SetupSceneObjects();

        CutSceneAnimations = new AnimationQueue();
        // sitting animation
        CutSceneAnimations.add(new sitingAnimation(Table, PhysicalFolder));
        // intro thoughts
        CutSceneAnimations.add(new startMonologAnimation(Graphic));
    }

    @Override
    public GameScenes update(long frameNumber) {
        // Updates the idle animations
        DefendantAnimationManager.update();

        if (!CutSceneAnimations.hasFinished()) { // if there is a CutScene, it will block the rest of the logic
            CutSceneAnimations.update();
            return this;
        }

        DialogueManager.update();

        return this;
    }
    private void SetupSceneObjects () {
        // setup the dependent model
        DefendantAnimationManager = new DefendantAnimationsManager(Graphic);

        ModelLoader Model = new PLY_ModelLoader(new float[] {-1,1,1});
        Model.setPath("models/Table.ply");
        Table = new Generic3DObject(Model);
        Graphic.addObject(Table);

        Model.setPath("models/Folder.ply");
        PhysicalFolder = new Generic3DObject(Model);
        Graphic.addObject(PhysicalFolder);
    }
}
