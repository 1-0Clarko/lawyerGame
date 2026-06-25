package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes;

import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper.AnimationQueue;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper.FadeAnimation;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.InterrogatoryScene.InterrogatoryScene;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Generic3DObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GenericTextObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.*;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper.MonologueAnimation;
import it.unicam.cs.mpgc.rpg130398.api.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.api.ModelLoader;
import it.unicam.cs.mpgc.rpg130398.Graphics.PLY_ModelLoader;
import it.unicam.cs.mpgc.rpg130398.api.InputManager;
import it.unicam.cs.mpgc.rpg130398.api.RendableObject;
import it.unicam.cs.mpgc.rpg130398.api.RendableText;

import java.awt.*;

public class StartScene implements GameScenes {
    Game game;
    GraphicsManager graphic;
    InputManager input;

    // Oggetti grafici
    RendableText textBox;
    RendableObject corridor;
    RendableObject blackScreen; // un piano completamente nero che copre l'intero schermo

    static final String[] INTRO = {"Cammini per un corridoio", "i muri sono dipinti di verde quasi ad arrivare alla testa", "a illuminare la via,\nc'è solo una lampadina bianca penzolante"};
    static final String[] OUTRO = {"Continui a camminare"};

    AnimationQueue animationQueue;

    public StartScene(Game game, GraphicsManager graphic, InputManager input) {
        this.game = game;
        this.graphic = graphic;
        this.input = input;

        textBox = new GenericTextObject();
        textBox.setFontPath("fonts/Undisclose.ttf");
        textBox.setPosition(new float[]{4.3f,1,0});
        textBox.setColor(Color.gray);
        graphic.addText(textBox);

        ModelLoader Model = new PLY_ModelLoader("models/StartScene/Corridor.ply");
        corridor = new Generic3DObject(Model);
        graphic.addObject(corridor);

        Model.setPath("models/BlackScreen.ply");
        blackScreen = new Generic3DObject(Model);
        graphic.addObject(blackScreen);


        Animation FadeInTransition = new FadeAnimation(blackScreen, 34, true);
        Monologue playerMonologueIntro = new MonologueAnimation(INTRO, textBox, 0.4f, 20);
        Animation FadeOutTransition = new FadeAnimation(blackScreen, 25, false);
        Monologue playerMonologueOutro = new MonologueAnimation(OUTRO, textBox, 0.2f, 20);

        animationQueue = new AnimationQueue();
        animationQueue.add(FadeInTransition);
        animationQueue.add(playerMonologueIntro);
        animationQueue.add(FadeOutTransition, this::onFadeOutStart);
        animationQueue.add(playerMonologueOutro, this::onOutroMonologueStart);
    }

    public GameScenes update(long FrameNumber) {
        if (!animationQueue.hasFinished()) {
            animationQueue.update();
            return this; //don't change the gameScene
        } else {
            finish();
            return new InterrogatoryScene(game, graphic, input);
        }
    }
    private void onFadeOutStart() {
        textBox.setText("");
    }
    private void onOutroMonologueStart() {
        textBox.setColor(Color.red);
        textBox.setPosition(new float[]{6.3f,1,0});
    }
    private void finish() {
        graphic.removeText(textBox);
        graphic.removeObject(corridor);
        graphic.removeObject(blackScreen);
    }
}