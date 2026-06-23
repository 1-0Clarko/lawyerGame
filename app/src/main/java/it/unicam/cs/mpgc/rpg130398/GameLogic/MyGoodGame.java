package it.unicam.cs.mpgc.rpg130398.GameLogic;

import com.badlogic.gdx.InputAdapter;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.InterrogatoryScene.InterrogatoryScene;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.StartScene;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.GameScenes;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.api.InputManager;

public final class MyGoodGame implements it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Game {
    GraphicsManager Graphics;
    InputManager Input;
    GameScenes CurrentGameFace;

    public MyGoodGame(GraphicsManager GraphicsManager, InputManager InputManager) {
        Graphics = GraphicsManager;
        Input = InputManager;

        CurrentGameFace = new StartScene(this, Graphics, Input);
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