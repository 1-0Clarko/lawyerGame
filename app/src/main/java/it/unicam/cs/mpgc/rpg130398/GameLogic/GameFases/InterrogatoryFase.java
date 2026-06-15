package it.unicam.cs.mpgc.rpg130398.GameLogic.GameFases;

import it.unicam.cs.mpgc.rpg130398.GameLogic.GameFases.Helper.AnimationQueue;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GameFases.Helper.StopMotionAnimation;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GameFases.Helper.TransformAnimation;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Generic3DObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GenericTextObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Animation;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Game;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.GameFase;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Sequence;
import it.unicam.cs.mpgc.rpg130398.GameLogic.JSON_DialogTreeLoader;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.ModelLoader;
import it.unicam.cs.mpgc.rpg130398.Graphics.PLY_ModelLoader;
import it.unicam.cs.mpgc.rpg130398.api.DialogNode;
import it.unicam.cs.mpgc.rpg130398.api.DialogTreeLoader;
import it.unicam.cs.mpgc.rpg130398.api.RendableObject;
import it.unicam.cs.mpgc.rpg130398.api.RendableText;

import java.util.Map;

public class InterrogatoryFase implements GameFase {
    Game Game;
    GraphicsManager Graphic;

    RendableObject Table;
    Defendent Defendent;
    DialogueMenager Dialogue;

    AnimationQueue CutSceneAnimations;

    public InterrogatoryFase(Game game, GraphicsManager Gm) {
        this.Game = game;
        this.Graphic = Gm;

        ModelLoader Model = new PLY_ModelLoader(new float[] {-1,1,1});
        Model.setPath("models/Table.ply");
        Table = new Generic3DObject(Model);
        Graphic.addObject(Table);
        float[] Zero = new float[]{0f,0f,0f};
        Animation EnterAnimation = new TransformAnimation(Table, new float[]{0.3f, -3f, 4f}, Zero, new float[]{-20f, 0f, 0f}, Zero,50, TransformAnimation.Easing.EASE_IN_OUT);

        CutSceneAnimations = new AnimationQueue(); // Sitting animation
        CutSceneAnimations.add(EnterAnimation);
        Defendent = new Defendent();
        Dialogue = new DialogueMenager();

    }

    @Override
    public GameFase update(long frameNumber) {
        Defendent.update();
        Dialogue.update();

        if (!CutSceneAnimations.hasFinished()) { // if there is a CutScene, it will block the rest of the logic
            CutSceneAnimations.update();
            return this;
        }
        return this;
    }

    private class Defendent {

        Sequence idleAnimation;
        Defendent () {
            ModelLoader Model = new PLY_ModelLoader(new float[] {-1,1,1});
            Model.setPath("models/DefendantSitting1.ply");
            RendableObject DefendantSitting1 = new Generic3DObject(Model);
            Model.setPath("models/DefendantSitting2.ply");
            RendableObject DefendantSitting2 = new Generic3DObject(Model);
            Model.setPath("models/DefendantSitting3.ply");
            RendableObject DefendantSitting3 = new Generic3DObject(Model);


            RendableObject[] Objects = new RendableObject[] {DefendantSitting1,DefendantSitting2, DefendantSitting3};
            idleAnimation = new StopMotionAnimation(Graphic, 80, true, Objects);
        }

        private void update () {
            idleAnimation.update();
        }
    }
    private class DialogueMenager {
        RendableText text;
        //  node-id     Node
        Map<Integer, DialogNode> DialogueNodes;

        DialogueMenager () {
            text = new GenericTextObject();

            DialogTreeLoader DialogLoader = new JSON_DialogTreeLoader("DialogTrees/InterogatoryDialog.json");
            try {
                DialogLoader.read();
            }catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
            DialogueNodes = DialogLoader.getNodes();
        }


        private void update () {

        }
    }
}
