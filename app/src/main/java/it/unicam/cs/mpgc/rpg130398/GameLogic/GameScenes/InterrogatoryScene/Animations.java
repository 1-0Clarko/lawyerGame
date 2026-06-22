package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.InterrogatoryScene;

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

class SitingAnimation implements Animation {
    private final RendableObject table;
    private final RendableObject physicalFolder;

    private final Animation tableAnimation;
    private final Animation folderAnimation;

    protected SitingAnimation(RendableObject table, RendableObject physicalFolder) {
        this.table = table;
        this.physicalFolder = physicalFolder;

        float[] zero = new float[]{0f, 0f, 0f};
        tableAnimation = new TransformAnimation(table, new float[]{0.3f, -3f, 4f}, zero, new float[]{-20f, 0f, 0f}, zero, 80, TransformAnimation.Easing.EASE_IN_OUT);
        folderAnimation = new TransformAnimation(physicalFolder, new float[]{0.3f, -3f, 4f}, zero, new float[]{-20f, 0f, 0f}, zero, 80, TransformAnimation.Easing.EASE_IN_OUT);
    }

    @Override
    public void update() {
        tableAnimation.update();
        folderAnimation.update();
    }

    @Override
    public boolean hasFinished() {
        return tableAnimation.hasFinished();
    }
}

class StartMonologAnimation implements Animation {
    private final GraphicsManager graphic;

    static final String[] INTRO = {
            "      .           .           .",
            "|- Il mio secondo caso come avvocato penalista d'ufficio... -|     ",
            "|- Pensavo che mi sarebbe capitato un taccheggiatore, non un presunto assassino. -|",
            "|- Nome sconosciuto. Origini sconosciute. Nessuno sa chi sia. -|",
            "|- Accusa: omicidio. Una tecnica della centrale elettrica, trovata morta\n" +
                    "sulla scena. -|",
            "|- E per di più in un carcere del genere... -| ",
            "|- Avrei dovuto aprire uno studio tutto mio.           \n" +
                    " Ma ormai è troppo tardi per ripensarci. -|",
            "|- Se voglio costruire una difesa, devo capire chi ho davanti. -|",
            "|- Bene... iniziamo il colloquio. -|"};

    private final RendableText textBox;
    private RendableObject blackBar;

    private final AnimationQueue animationQueue = new AnimationQueue();

    protected StartMonologAnimation(GraphicsManager graphic) {
        this.graphic = graphic;
        textBox = new GenericTextObject();
        textBox.setPosition(new float[]{1.6f, 1.6f, 1});
        textBox.setSize(4);
        graphic.addText(textBox);

        ModelLoader model = new PLY_ModelLoader(new float[]{-1, 1, 1});
        model.setPath("models/UIBlackBar.ply");
        blackBar = new Generic3DObject(model);
        blackBar.setPosition(new float[]{0, 0, 1});

        Animation fadeInBox = new FadeAnimation(blackBar, 10, false);
        animationQueue.add(fadeInBox, this::onTextStart);
        Animation monologue = new MonologueAnimation(INTRO, textBox, 1f, 60);
        animationQueue.add(monologue);
        Animation fadeOutBox = new FadeAnimation(blackBar, 14, true);
        animationQueue.add(fadeOutBox, this::onFadeOut);
    }

    @Override
    public void update() {
        animationQueue.update();
    }

    @Override
    public boolean hasFinished() {
        return animationQueue.hasFinished();
    }

    private void onTextStart() {
        graphic.addObject(blackBar);
    }

    private void onFadeOut() {
        graphic.removeText(textBox);
    }
}

class DefendantAnimationsManager implements Animation {

    private final GraphicsManager graphic;

    private StopMotionAnimation trustingAnimation;
    private StopMotionAnimation neutralAnimation;
    private StopMotionAnimation angryAnimation;
    private StopMotionAnimation killingAnimation;

    enum Status {KILLANIMATION, ANGRY, NEUTRAL, TRUSTING}

    private Sequence<RendableObject> currentAnimation;
    private Status currentStatus;

    protected DefendantAnimationsManager(GraphicsManager graphic) {
        this.graphic = graphic;
        ModelLoader modelLoader = new PLY_ModelLoader(new float[]{-1, 1, 1});

        modelLoader.setPath("models/DefendantSitting1.ply");
        RendableObject defendantSitting1 = new Generic3DObject(modelLoader);
        modelLoader.setPath("models/DefendantSitting2.ply");
        RendableObject defendantSitting2 = new Generic3DObject(modelLoader);
        modelLoader.setPath("models/DefendantSitting3.ply");
        RendableObject defendantSitting3 = new Generic3DObject(modelLoader);
        RendableObject[] objects = new RendableObject[]{defendantSitting1, defendantSitting2, defendantSitting3};
        neutralAnimation = new StopMotionAnimation(graphic, 80, true, objects);

        modelLoader.setPath("models/DefendantSittingAngry1.ply");
        RendableObject defendantAngry1 = new Generic3DObject(modelLoader);
        modelLoader.setPath("models/DefendantSittingAngry2.ply");
        RendableObject defendantAngry2 = new Generic3DObject(modelLoader);
        objects = new RendableObject[]{defendantAngry1, defendantAngry2};
        angryAnimation = new StopMotionAnimation(graphic, 70, true, objects);

        modelLoader.setPath("models/DefendantSittingKill1.ply");
        RendableObject defendantKilling1 = new Generic3DObject(modelLoader);
        modelLoader.setPath("models/DefendantSittingKill2.ply");
        RendableObject defendantKilling2 = new Generic3DObject(modelLoader);
        modelLoader.setPath("models/DefendantSittingKill33.ply");
        RendableObject defendantKilling3 = new Generic3DObject(modelLoader);
        // DefendantKilling1 ripetuto 3 volte di proposito: rallenta il primo
        // frame dell'animazione di morte rispetto agli ultimi due.
        objects = new RendableObject[]{defendantKilling1, defendantKilling1, defendantKilling1, defendantKilling2, defendantKilling3};
        killingAnimation = new StopMotionAnimation(graphic, 10, false, objects);

        // TODO: trustingAnimation needs its own dedicated models
        // (e.g. models/DefendantSittingTrusting*.ply). Until then it falls
        // back to the neutral animation so TRUSTING never leaves the
        // defendant invisible (see setAnimationStatus).
        trustingAnimation = neutralAnimation;

        currentAnimation = neutralAnimation;
        currentStatus = Status.NEUTRAL;
    }

    @Override
    public void update() {
        currentAnimation.update();
    }

    @Override
    public boolean hasFinished() {
        return currentAnimation.hasFinished();
    }

    public void setAnimationStatus(Status status) {
        if (status == currentStatus)
            return;

        graphic.removeObject(currentAnimation.getCurrent());
        switch (status) {
            case NEUTRAL -> currentAnimation = neutralAnimation;
            case ANGRY -> currentAnimation = angryAnimation;
            case KILLANIMATION -> currentAnimation = killingAnimation;
            case TRUSTING -> currentAnimation = trustingAnimation;
        }
        currentStatus = status;
    }
}

class KilledAnimation implements Animation {
    private final GraphicsManager graphic;

    private final RendableObject table;
    private final RendableObject physicalFolder;
    private RendableObject blackScreen;

    private static final int DURATION = 6;

    private final AnimationQueue cutScene = new AnimationQueue();

    protected KilledAnimation(RendableObject table, RendableObject physicalFolder, GraphicsManager graphic) {
        this.graphic = graphic;

        this.table = table;
        this.physicalFolder = physicalFolder;

        float[] zero = new float[]{0f, 0f, 0f};
        float[] endPos = new float[]{-10f, -10f, 4f};
        float[] endRot = new float[]{-80f, 0f, 0f};

        Animation tableTranslation = new TransformAnimation(table, zero, endPos, zero, endRot, DURATION, TransformAnimation.Easing.EASE_IN);
        Animation folderTranslation = new TransformAnimation(physicalFolder, zero, endPos, zero, endRot, DURATION, TransformAnimation.Easing.EASE_IN);
        Animation tableScale = new ScaleAnimation(table, DURATION, 2);
        Animation folderScale = new ScaleAnimation(physicalFolder, DURATION, 2);
        ParallelAnimation killAnimation = new ParallelAnimation(tableTranslation, folderTranslation, tableScale, folderScale);

        ModelLoader model = new PLY_ModelLoader("models/BlackScreen.ply");
        blackScreen = new Generic3DObject(model);
        Animation fadeInTransition = new FadeAnimation(blackScreen, 34, false);

        GenericTextObject textBox = new GenericTextObject();
        textBox.setFontPath("fonts/Undisclose.ttf");
        textBox.setPosition(new float[]{4f, 5, 0});
        textBox.setColor(Color.gray);
        textBox.setSize(6);
        graphic.addText(textBox);
        Monologue gameOverTextAnimation = new MonologueAnimation(new String[]{"Game Over"}, textBox, 0.1f, 0);

        cutScene.add(killAnimation);
        cutScene.add(fadeInTransition, this::onFadeStart);
        cutScene.add(gameOverTextAnimation);
    }

    @Override
    public void update() {
        cutScene.update();
    }

    @Override
    public boolean hasFinished() {
        return cutScene.hasFinished();
    }

    private void onFadeStart() {
        graphic.addObject(blackScreen);
    }
}