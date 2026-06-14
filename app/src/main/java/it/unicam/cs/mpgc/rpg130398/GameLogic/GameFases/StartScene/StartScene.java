package it.unicam.cs.mpgc.rpg130398.GameLogic.GameFases.StartScene;

import it.unicam.cs.mpgc.rpg130398.GameLogic.Generic3DObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GenericTextObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Game;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.GameFase;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Monologue;
import it.unicam.cs.mpgc.rpg130398.GameLogic.SimpleMonologue;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.ModelLoader;
import it.unicam.cs.mpgc.rpg130398.Graphics.PLY_ModelLoader;
import it.unicam.cs.mpgc.rpg130398.api.RendableObject;
import it.unicam.cs.mpgc.rpg130398.api.RendableText;

public class StartScene implements GameFase {
    Game Game;
    GraphicsManager Graphic;

    // Oggetti grafici
    RendableText TextBox;
    RendableObject Corridor;
    RendableObject BlackScreen; // un piano completamente nero che copre l'intero schermo


    OpacityModifier BlackTransition; // per la transizione di ingresso e di uscita
    Monologue playerMonologue;
    static final String[] INTRO = {"Cammini per un corridoio", "i muri sono dipinti di verde quasi ad arrivare alla testa", "a illuminare la via,\nc'è solo una lampadina bianca penzolante"};
    static final String[] OUTRO = {"Continui a camminare"};

    public StartScene(Game game, GraphicsManager Gm) {
        this.Game = game;
        this.Graphic = Gm;

        TextBox = new GenericTextObject();
        TextBox.setFontPath("fonts/Undisclose.ttf");
        TextBox.setPosition(new float[]{4.3f,1,0});
        Graphic.addText(TextBox);
        playerMonologue = new SimpleMonologue(INTRO, TextBox, 2, 30);
        playerMonologue.showNext();

        ModelLoader Ml = new PLY_ModelLoader("models/Corridor.ply");
        Corridor = new Generic3DObject(Ml);
        Graphic.addObject(Corridor);

        Ml.setPath("models/BlackScreen.ply");
        BlackScreen = new Generic3DObject(Ml);
        Graphic.addObject(BlackScreen);
        BlackTransition = new OpacityModifier(BlackScreen);
    }

    public GameFase update(long FrameNumber) {
        if (FrameNumber <= 10) {
            BlackTransition.setOpacity(1-(FrameNumber/10f));
            return this;
        }

        playerMonologue.update();
        if (playerMonologue.hasFinishedDisplaying())
            playerMonologue.showNext();


        return this;
    }
}