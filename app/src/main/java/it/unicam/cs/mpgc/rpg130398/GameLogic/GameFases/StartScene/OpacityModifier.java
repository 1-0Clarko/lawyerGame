package it.unicam.cs.mpgc.rpg130398.GameLogic.GameFases.StartScene;

import it.unicam.cs.mpgc.rpg130398.api.RendableObject;
import it.unicam.cs.mpgc.rpg130398.api.Vertex;

import java.awt.*;

/**
 * Modifies the opacity of a RendableObject by adjusting the alpha value of its vertices.
 * Opacity ranges from 0.0 (fully transparent) to 1.0 (fully opaque).
 */
public class OpacityModifier {
    private final RendableObject target;

    /**
     * @param target the object whose opacity will be modified
     */
    public OpacityModifier(RendableObject target) {
        this.target = target;
    }

    /**
     * Sets the opacity of the target object.
     * Reads the current vertices, modifies their alpha value and sends them back.
     *
     * @param opacity value between 0.0 (transparent) and 1.0 (opaque)
     */
    public void setOpacity(float opacity) {
        opacity = Math.clamp(opacity, 0f, 1f);

        Vertex[] vertices = target.getObjectVertices();
        for (Vertex v : vertices) {
            Color c = v.getColor();
            v.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(opacity * 255)));
        }

        // Notifica il graphics engine che i vertici sono cambiati
        target.setObjectVertices(vertices);
    }
}
