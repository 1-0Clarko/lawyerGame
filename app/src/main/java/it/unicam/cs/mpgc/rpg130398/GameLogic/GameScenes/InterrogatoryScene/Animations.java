package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.InterrogatoryScene;

import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper.*;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Generic3DObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GenericTextObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Animation;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Sequence;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.ModelLoader;
import it.unicam.cs.mpgc.rpg130398.Graphics.PLY_ModelLoader;
import it.unicam.cs.mpgc.rpg130398.api.RendableObject;
import it.unicam.cs.mpgc.rpg130398.api.RendableText;

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

    Sequence idleAnimation;
    protected DefendantAnimationsManager(GraphicsManager Graphic) {
        this.Graphic = Graphic;
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
    @Override
    public void update () {
        idleAnimation.update();
    }

    @Override
    public boolean hasFinished() {
        return false;
    }
}