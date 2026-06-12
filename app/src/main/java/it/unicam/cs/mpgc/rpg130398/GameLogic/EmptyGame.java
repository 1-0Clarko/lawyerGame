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

    // FRUSTUM = (16, 9, 16); (Confine massimo visibile)
    // +x verso destra, +y verso l'alto, +z verso lo schermo, -z verso di te
    Vertex[] vertices1 = new Vertex[]{
            // Base of the Pyramid
            new GenericVertex(-3,-3,-3, Color.red),
            new GenericVertex(3,-3,-3, Color.cyan),
            new GenericVertex(-3,-3,3, Color.red),
            new GenericVertex(3,-3,3, Color.cyan),
            // Punta
            new GenericVertex(0,3,0, Color.cyan),
    };
    short[] TriangleTriplets = new short[] { // Tels what vertices forms a triangle
            0,1,4, // faccia davanti
            1,3,4, // faccia destra
            3,2,4, // faccia dietro
            2,0,4, // faccia sinistra

            0,1,2, // triangolo sotto1
            2,3,1  // triangolo sotto2
    };
    RendableObject Triangle;

    public EmptyGame(GraphicsManager GraphicsManager) {
        Gm = GraphicsManager;

        Triangle = new Generic3DObject();
        Triangle.setObjectVertices(vertices1);
        Triangle.setTriangleTriplets(TriangleTriplets);
        Triangle.setPosition(new float[]{16/2, 9/2, 16/2});

        Gm.addObject(Triangle);
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
