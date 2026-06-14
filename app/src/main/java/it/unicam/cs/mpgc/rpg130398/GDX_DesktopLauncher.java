package it.unicam.cs.mpgc.rpg130398;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.Vector3;
import it.unicam.cs.mpgc.rpg130398.GameLogic.MyGoodGame;
import it.unicam.cs.mpgc.rpg130398.Graphics.GDX_GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;


/**
 * Entry point della applicazione desktop
 */
public class GDX_DesktopLauncher extends ApplicationAdapter {
    private static it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Game Game;
    public static float[] FRUSTUM = new float[]{16,9,16};
    final static int FPSMAX = 30;
    final static long LOGIC_UPDATE_INTERVAL = 1000 / (FPSMAX);

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // ApplicationAdapter.render() è chiamato FPSMAX volte al secondo
        config.setForegroundFPS(FPSMAX);
        config.setIdleFPS(FPSMAX);

        new Lwjgl3Application(new ApplicationAdapter() {
            long FrameCounter = 0;
            long lastLogicUpdate;

            @Override
            public void create() {
                GraphicsManager GraphicsManager = new GDX_GraphicsManager(FRUSTUM);
                Game = new MyGoodGame(GraphicsManager);
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
        }, config);
    }
}