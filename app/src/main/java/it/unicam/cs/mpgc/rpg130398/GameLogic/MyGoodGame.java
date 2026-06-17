package it.unicam.cs.mpgc.rpg130398.GameLogic;

import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.InterrogatoryScene.InterrogatoryScene;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.GameScenes;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;

public final class MyGoodGame implements it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Game {
    GraphicsManager Graphics;
    GameScenes CurrentGameFace;

    public MyGoodGame(GraphicsManager GraphicsManager) {
        Graphics = GraphicsManager;
        CurrentGameFace = new InterrogatoryScene(this, Graphics);
    }

    @Override
    public void updateLogic(long FrameNumber) {
        if (CurrentGameFace == null)
            return;

        CurrentGameFace = CurrentGameFace.update(FrameNumber);
    }
    @Override
    public void render() {Graphics.render();};
    @Override
    public void resizeWindow(int width, int height) {Graphics.resize(width, height);};
    @Override
    public void dispose() {Graphics.dispose();};
}