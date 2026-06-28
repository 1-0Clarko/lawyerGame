package it.unicam.cs.mpgc.rpg130398;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Game;
import it.unicam.cs.mpgc.rpg130398.GameLogic.MyGoodGame;
import it.unicam.cs.mpgc.rpg130398.Graphics.GDX_GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.api.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.Input.GDX_InputManager;
import it.unicam.cs.mpgc.rpg130398.api.InputManager;

/**
 * Entry point della applicazione desktop
 */
public class GDX_DesktopLauncher extends ApplicationAdapter {
    private static Game Game;
    final static int FPSMAX = 30;
    final static long LOGIC_UPDATE_INTERVAL = 1000 / (FPSMAX);

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // ApplicationAdapter.render() è chiamato FPSMAX volte al secondo
        config.setForegroundFPS(FPSMAX);
        config.setIdleFPS(FPSMAX);

        new Lwjgl3Application(new GDX_Application(), config);
    }
    private static class GDX_Application extends ApplicationAdapter {
        long FrameCounter = 0;
        long lastLogicUpdate;

        @Override
        public void create() {
            GraphicsManager GraphicsManager = new GDX_GraphicsManager();
            InputManager InputManager = new GDX_InputManager(GraphicsManager);

            Game = new MyGoodGame(GraphicsManager, InputManager);
        }


        @Override
        public void render() {
            long now = System.currentTimeMillis();
            if (now - lastLogicUpdate >= LOGIC_UPDATE_INTERVAL) {
                lastLogicUpdate = now;
                Game.updateLogic(FrameCounter++);
            }
            Game.render();
        }

        @Override
        public void resize(int w, int h) {
            Game.resizeWindow(w, h);
        }

        @Override
        public void dispose() {
            Game.dispose();
        }
    }
}