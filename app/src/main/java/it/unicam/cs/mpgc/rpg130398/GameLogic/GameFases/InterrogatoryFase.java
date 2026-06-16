package it.unicam.cs.mpgc.rpg130398.GameLogic.GameFases;

import it.unicam.cs.mpgc.rpg130398.GameLogic.GameFases.Helper.*;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Generic3DObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GenericTextObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.*;
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

    //common 3D Objects
    RendableObject Table;
    RendableObject PhysicalFolder;

    // Private classes
    Defendent Defendent;
    DialogueMenager Dialogue;

    // Animations
    AnimationQueue CutSceneAnimations;

    public InterrogatoryFase(Game game, GraphicsManager Gm) {
        this.Game = game;
        this.Graphic = Gm;
        Defendent = new Defendent();
        Dialogue = new DialogueMenager();

        SetupSceneObjects();

        CutSceneAnimations = new AnimationQueue();

        // sitting animation
        CutSceneAnimations.add(new sitingAnimation());
        // intro thoughts
        CutSceneAnimations.add(new thoughtsStartMonolog());
    }

    @Override
    public GameFase update(long frameNumber) {
        Defendent.update(); //Animation

        if (!CutSceneAnimations.hasFinished()) { // if there is a CutScene, it will block the rest of the logic
            CutSceneAnimations.update();
            return this;
        }

        Dialogue.update();

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
    private class sitingAnimation implements Animation {
        Animation TableAnimation;
        Animation FolderAnimation;

        sitingAnimation() {
            float[] Zero = new float[]{0f,0f,0f};
            TableAnimation = new TransformAnimation(Table, new float[]{0.3f, -3f, 4f}, Zero, new float[]{-20f, 0f, 0f}, Zero,80, TransformAnimation.Easing.EASE_IN_OUT);
            FolderAnimation = new TransformAnimation(PhysicalFolder, new float[]{0.3f, -3f, 4f}, Zero, new float[]{-20f, 0f, 0f}, Zero,80, TransformAnimation.Easing.EASE_IN_OUT);
        }
        @Override
        public void update() {
            TableAnimation.update();
            FolderAnimation.update();
        }

        @Override
        public boolean hasFinished() {
            return TableAnimation.hasFinished();
        }
    }
    private class thoughtsStartMonolog implements Animation {
        static final String[] INTRO = {
                "              |- PENSIERO -|                     ",
                "|- Il mio secondo caso come avvocato penalista d'ufficio... -|     ",
                "|- Pensavo che mi sarebbe capitato un taccheggiatore, non un presunto assassino. -|",
                "|- Nome sconosciuto. Origini sconosciute. Nessuno sa chi sia. -|",
                "|- E per di più in un carcere del genere... -| ",
                "|- Avrei dovuto aprire uno studio tutto mio.           \n"+
                " Ma ormai è troppo tardi per ripensarci. -|",
                "|- Se voglio costruire una difesa, devo capire chi ho davanti. -|",
                "|- Bene... iniziamo il colloquio. -|"};
        RendableText TextBox;
        RendableObject BlackBox;

        AnimationQueue AnimationQueue = new AnimationQueue();

        thoughtsStartMonolog() {
            TextBox = new GenericTextObject();
            TextBox.setPosition(new float[]{1.6f,1.6f,1});
            TextBox.setSize(4);
            Graphic.addText(TextBox);

            ModelLoader Model = new PLY_ModelLoader(new float[] {-1,1,1});
            Model.setPath("models/UIBlackBar.ply");
            BlackBox = new Generic3DObject(Model);

            Animation FadeInBox = new FadeAnimation(BlackBox, 10, false);
            AnimationQueue.add(FadeInBox, this::onTextStart);
            Animation Monologue = new SimpleMonologue(INTRO, TextBox, 1f, 60);
            AnimationQueue.add(Monologue);
            Animation FadeOutBox = new FadeAnimation(BlackBox, 14, true);
            AnimationQueue.add(FadeOutBox, this::onFadeOut);
        }

        @Override
        public void update() {
            AnimationQueue.update();
        }

        @Override
        public boolean hasFinished() {
            return AnimationQueue.hasFinished();
        }

        private void onTextStart() {
            Graphic.addObject(BlackBox);
        }
        private void onFadeOut() {
            Graphic.removeText(TextBox);
        }
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
