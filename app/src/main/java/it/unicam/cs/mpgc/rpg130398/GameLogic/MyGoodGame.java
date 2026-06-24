package it.unicam.cs.mpgc.rpg130398.GameLogic;

import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.TrialScene.TrialScene;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.GameScenes;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.api.InputManager;

import java.util.HashSet;

public final class MyGoodGame implements it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Game {
    GraphicsManager Graphics;
    InputManager Input;
    GameScenes CurrentGameFace;

    public MyGoodGame(GraphicsManager GraphicsManager, InputManager InputManager) {
        Graphics = GraphicsManager;
        Input = InputManager;

        //TODO change after finishing the scene
        HashSet<String> opinionatedFlags = new HashSet<>();
        opinionatedFlags.add("prova");
        CurrentGameFace = new TrialScene(this, Graphics, Input, opinionatedFlags);
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