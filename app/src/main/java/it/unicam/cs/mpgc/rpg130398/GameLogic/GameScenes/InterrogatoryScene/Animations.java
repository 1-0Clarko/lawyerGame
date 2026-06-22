package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.InterrogatoryScene;

import com.badlogic.gdx.graphics.g3d.Model;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper.*;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Generic3DObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GenericTextObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Animation;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Monologue;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Sequence;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.ModelLoader;
import it.unicam.cs.mpgc.rpg130398.Graphics.PLY_ModelLoader;
import it.unicam.cs.mpgc.rpg130398.api.RendableObject;
import it.unicam.cs.mpgc.rpg130398.api.RendableText;

import java.awt.*;

class sitingAnimation implements Animation {
    RendableObject Table;
    RendableObject PhysicalFolder;

    Animation TableAnimation;
    Animation FolderAnimation;

    protected sitingAnimation(RendableObject Table, RendableObject PhysicalFolder) {
        this.Table = Table;
        this.PhysicalFolder = PhysicalFolder;

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
class startMonologAnimation implements Animation {
    GraphicsManager Graphic;

    static final String[] INTRO = {
            "      .           .           .",
            "|- Il mio secondo caso come avvocato penalista d'ufficio... -|     ",
            "|- Pensavo che mi sarebbe capitato un taccheggiatore, non un presunto assassino. -|",
            "|- Nome sconosciuto. Origini sconosciute. Nessuno sa chi sia. -|",
            "|- E per di più in un carcere del genere... -| ",
            "|- Avrei dovuto aprire uno studio tutto mio.           \n"+
                    " Ma ormai è troppo tardi per ripensarci. -|",
            "|- Se voglio costruire una difesa, devo capire chi ho davanti. -|",
            "|- Bene... iniziamo il colloquio. -|"};
    RendableText TextBox;
    RendableObject BlackBar;

    AnimationQueue AnimationQueue = new AnimationQueue();

    protected startMonologAnimation(GraphicsManager Graphic) {
        this.Graphic = Graphic;
        TextBox = new GenericTextObject();
        TextBox.setPosition(new float[]{1.6f,1.6f,1});
        TextBox.setSize(4);
        Graphic.addText(TextBox);

        ModelLoader Model = new PLY_ModelLoader(new float[] {-1,1,1});
        Model.setPath("models/UIBlackBar.ply");
        BlackBar = new Generic3DObject(Model);
        BlackBar.setPosition(new float[]{0,0,1});

        Animation FadeInBox = new FadeAnimation(BlackBar, 10, false);
        AnimationQueue.add(FadeInBox, this::onTextStart);
        Animation Monologue = new MonologueAnimation(INTRO, TextBox, 1f, 60);
        AnimationQueue.add(Monologue);
        Animation FadeOutBox = new FadeAnimation(BlackBar, 14, true);
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
        Graphic.addObject(BlackBar);
    }
    private void onFadeOut() {
        Graphic.removeText(TextBox);
    }
}
class DefendantAnimationsManager implements Animation {
    GraphicsManager Graphic;

    StopMotionAnimation trustingAnimation;
    StopMotionAnimation neutralAnimation;
    StopMotionAnimation angryAnimation;
    StopMotionAnimation killingAnimation;
    enum Status {KILLANIMATION, ANGRY, NEUTRAL, TRUSTING};

    Sequence<RendableObject> CurrentAnimation;
    Status currentStatus;

    protected DefendantAnimationsManager(GraphicsManager Graphic) {
        this.Graphic = Graphic;
        ModelLoader ModelLoader = new PLY_ModelLoader(new float[] {-1,1,1});

        ModelLoader.setPath("models/DefendantSitting1.ply");
        RendableObject DefendantSitting1 = new Generic3DObject(ModelLoader);
        ModelLoader.setPath("models/DefendantSitting2.ply");
        RendableObject DefendantSitting2 = new Generic3DObject(ModelLoader);
        ModelLoader.setPath("models/DefendantSitting3.ply");
        RendableObject DefendantSitting3 = new Generic3DObject(ModelLoader);
        RendableObject[] Objects = new RendableObject[] {DefendantSitting1,DefendantSitting2,DefendantSitting3};
        neutralAnimation = new StopMotionAnimation(Graphic, 80, true, Objects);

        ModelLoader.setPath("models/DefendantSittingAngry1.ply");
        RendableObject DefendantAngry1 = new Generic3DObject(ModelLoader);
        ModelLoader.setPath("models/DefendantSittingAngry2.ply");
        RendableObject DefendantAngry2 = new Generic3DObject(ModelLoader);
        Objects = new RendableObject[] {DefendantAngry1, DefendantAngry2};
        angryAnimation = new StopMotionAnimation(Graphic, 70, true, Objects);

        ModelLoader.setPath("models/DefendantSittingKill1.ply");
        RendableObject DefendantKilling1 = new Generic3DObject(ModelLoader);
        ModelLoader.setPath("models/DefendantSittingKill2.ply");
        RendableObject DefendantKilling2 = new Generic3DObject(ModelLoader);
        ModelLoader.setPath("models/DefendantSittingKill33.ply");
        RendableObject DefendantKilling3 = new Generic3DObject(ModelLoader);
        Objects = new RendableObject[] {DefendantKilling1, DefendantKilling1, DefendantKilling1, DefendantKilling2, DefendantKilling3};
        killingAnimation = new StopMotionAnimation(Graphic, 10, false, Objects);

        CurrentAnimation = neutralAnimation;
        currentStatus = Status.NEUTRAL;
    }
    @Override
    public void update () {
        CurrentAnimation.update();
    }

    @Override
    public boolean hasFinished() {
        return CurrentAnimation.hasFinished();
    }
    public void setAnimationStatus(Status status) {
        if (status == currentStatus)
            return;

        Graphic.removeObject(CurrentAnimation.getCurrent());
        switch (status) {
            case NEUTRAL: CurrentAnimation = neutralAnimation; break;
            case ANGRY: CurrentAnimation = angryAnimation; break;
            case KILLANIMATION: CurrentAnimation = killingAnimation; break;
        }
        currentStatus = status;
    }
}
class KilledAnimation implements Animation {
    GraphicsManager Graphic;

    RendableObject Table;
    RendableObject PhysicalFolder;
    RendableObject BlackScreen;

    final int DURATION = 6;

    AnimationQueue CutScene = new AnimationQueue();


    protected KilledAnimation(RendableObject Table, RendableObject PhysicalFolder, GraphicsManager Graphic) {
        this.Graphic = Graphic;

        this.Table = Table;
        this.PhysicalFolder = PhysicalFolder;

        float[] Zero = new float[]{0f,0f,0f};
        float[] EndPos = new float[]{-10f,-10f,4f};
        float[] EndRot = new float[]{-80f,0f,0f};


        Animation TableTranslation = new TransformAnimation(Table, Zero, EndPos, Zero, EndRot,DURATION, TransformAnimation.Easing.EASE_IN);
        Animation FolderTranslation = new TransformAnimation(PhysicalFolder, Zero, EndPos, Zero, EndRot,DURATION, TransformAnimation.Easing.EASE_IN);
        Animation TableScale = new ScaleAnimation(Table, DURATION, 2);
        Animation FolderScale = new ScaleAnimation(PhysicalFolder, DURATION, 2);
        ParallelAnimation killAnimation = new ParallelAnimation(TableTranslation, FolderTranslation, TableScale, FolderScale);

        ModelLoader Model = new PLY_ModelLoader("models/BlackScreen.ply");
        BlackScreen = new Generic3DObject(Model);
        Animation FadeInTransition = new FadeAnimation(BlackScreen, 34, false);

        GenericTextObject TextBox = new GenericTextObject();
        TextBox.setFontPath("fonts/Undisclose.ttf");
        TextBox.setPosition(new float[]{4f,5,0});
        TextBox.setColor(Color.gray);
        TextBox.setSize(6);
        Graphic.addText(TextBox);
        Monologue GameOverTextAnimation = new MonologueAnimation(new String[] {"Game Over"}, TextBox, 0.1f, 0);

        CutScene.add(killAnimation);
        CutScene.add(FadeInTransition, this::onFadeStart);
        CutScene.add(GameOverTextAnimation);
    }
    @Override
    public void update() {
        CutScene.update();
    }

    @Override
    public boolean hasFinished() {
        return CutScene.hasFinished();
    }
    private void onFadeStart() {
        Graphic.addObject(BlackScreen);
    }
}