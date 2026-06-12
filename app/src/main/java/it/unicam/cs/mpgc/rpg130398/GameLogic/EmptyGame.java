package it.unicam.cs.mpgc.rpg130398.GameLogic;

import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Game;
import it.unicam.cs.mpgc.rpg130398.Graphics.GenericVertex;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.api.RendableObject;
import it.unicam.cs.mpgc.rpg130398.api.Vertex;

import java.awt.*;

/**
 * l'unica cosa che fa è gestire il GraphicsManager
 */
public final class EmptyGame implements Game {
    GraphicsManager Gm;

    Vertex[] vertices1 = new Vertex[]{
            new GenericVertex(3,3,0, Color.red),
            new GenericVertex(6,3,0, Color.cyan),
            new GenericVertex(4.5f,6,16, Color.pink)
    };
    Vertex[] vertices2 = new Vertex[]{
            new GenericVertex(3,3,0, Color.pink),
            new GenericVertex(6,3,0, Color.red),
            new GenericVertex(4.5f,6,0, Color.cyan)
    };
    Vertex[] vertices3 = new Vertex[]{
            new GenericVertex(3,3,0, Color.cyan),
            new GenericVertex(6,3,0, Color.pink),
            new GenericVertex(4.5f,6,0, Color.red)
    };
    RendableObject Triangle;

    public EmptyGame(GraphicsManager GraphicsManager) {
        Gm = GraphicsManager;

        Triangle = new Generic3DObject();
        Triangle.setObjectVertices(vertices1);
        Gm.addObject(Triangle);
    }

    @Override
    public void updateLogic(long FrameNumber) {
        if (((FrameNumber/20)%3) == 0)
            Triangle.setObjectVertices(vertices1);
        if (((FrameNumber/20)%3) == 1)
            Triangle.setObjectVertices(vertices2);
        if (((FrameNumber/20)%3) == 2)
            Triangle.setObjectVertices(vertices3);

    }
    @Override
    public void render() {Gm.render();};
    @Override
    public void resizeWindow(int width, int height) {Gm.resize(width, height);};
    @Override
    public void dispose() {Gm.dispose();};
}
