package it.unicam.cs.mpgc.rpg130398.GameLogic;

import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Game;
import it.unicam.cs.mpgc.rpg130398.Graphics.GenericVertex;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.ModelLoader;
import it.unicam.cs.mpgc.rpg130398.Graphics.PLY_ModelLoader;
import it.unicam.cs.mpgc.rpg130398.api.RendableObject;
import it.unicam.cs.mpgc.rpg130398.api.Vertex;

import java.awt.*;

/**
 * l'unica cosa che fa è gestire il GraphicsManager
 */
public final class EmptyGame implements Game {
    GraphicsManager Gm;
    RendableObject Triangle;



    public EmptyGame(GraphicsManager GraphicsManager) {
        Gm = GraphicsManager;

        ModelLoader loader = new PLY_ModelLoader("models/Banana.ply");
        Triangle = new Generic3DObject(loader);
        Gm.addObject(Triangle);

        Triangle.setPosition(new float[]{16/2, 9/2, 16/2});
    }

    @Override
    public void updateLogic(long FrameNumber) {
        Triangle.setRotation(new float[]{FrameNumber,FrameNumber,FrameNumber});
    }
    @Override
    public void render() {Gm.render();};
    @Override
    public void resizeWindow(int width, int height) {Gm.resize(width, height);};
    @Override
    public void dispose() {Gm.dispose();};
}
