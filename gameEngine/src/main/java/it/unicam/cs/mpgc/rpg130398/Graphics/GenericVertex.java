package it.unicam.cs.mpgc.rpg130398.Graphics;

import it.unicam.cs.mpgc.rpg130398.api.Vertex;
import java.awt.Color;

public class GenericVertex implements Vertex {
    private float[] position = new float[3];
    private Color color;

    public GenericVertex(float x, float y, float z, Color color) {
        this.position = new float[]{x, y, z};
        this.color = color;
    }

    @Override
    public float[] getPosition() {
        return position;
    }

    @Override
    public void setPosition(float[] position) {
        if (position == null || position.length != 3)
            throw new IllegalArgumentException("Position must be [x, y, z]");
        this.position = position;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }
}