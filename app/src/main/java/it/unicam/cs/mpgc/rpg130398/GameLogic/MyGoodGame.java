package it.unicam.cs.mpgc.rpg130398.GameLogic;

import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.InterrogatoryScene.InterrogatoryScene;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.StartScene;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.GameScenes;
import it.unicam.cs.mpgc.rpg130398.api.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.api.InputManager;

public final class MyGoodGame implements it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Game {
    GraphicsManager graphics;
    InputManager input;
    GameScenes currentGameFace;

    public MyGoodGame(GraphicsManager GraphicsManager, InputManager InputManager) {
        graphics = GraphicsManager;
        input = InputManager;

        currentGameFace = new StartScene(this, graphics, input);
    }

    @Override
    public void updateLogic(long FrameNumber) {
        if (currentGameFace == null)
            return;

        currentGameFace = currentGameFace.update(FrameNumber);
    }
    @Override
    public void render() {graphics.render();};
    @Override
    public void resizeWindow(int width, int height) {graphics.resize(width, height);};
    @Override
    public void dispose() {graphics.dispose();};
}