package it.unicam.cs.mpgc.rpg130398;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import it.unicam.cs.mpgc.rpg130398.GameLogic.EmptyGame;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Game;
import it.unicam.cs.mpgc.rpg130398.Graphics.GDX_GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;


/**
 * Entry point della applicazione desktop
 */
public class GDX_DesktopLauncher extends ApplicationAdapter {
    private static Game Game;

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(30);
        config.setIdleFPS(30);

        new Lwjgl3Application(new ApplicationAdapter() {
            long FrameCounter = 0;

            @Override
            public void create() {
                GraphicsManager GraphicsManager = new GDX_GraphicsManager();
                Game = new EmptyGame(GraphicsManager);
            }

            @Override
            public void render() {
                // Prima render poi updateLogic, per avere un frame rate più stabile
                Game.render();
                Game.updateLogic(FrameCounter++);
            }

            @Override
            public void resize(int w, int h) {
                Game.resizeWindow(w, h);
            }

            @Override
            public void dispose() {
                Game.dispose();
            }
        }, config);

    }
}