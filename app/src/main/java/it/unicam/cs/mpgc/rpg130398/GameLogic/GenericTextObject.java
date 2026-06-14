package it.unicam.cs.mpgc.rpg130398.GameLogic;

import it.unicam.cs.mpgc.rpg130398.api.*;

import java.awt.*;

public class GenericTextObject implements RendableText, Positionable {
    private boolean isDirty;
    String text;
    String fontPath;
    Color color;

    float[] pos = new float[3];
    float size = 1;

    public GenericTextObject() {
        color = Color.white;
        isDirty = true;
    }

    @Override
    public String getText () {
        return text;
    }
    @Override
    public void setText (String text) {
        this.text = text;
        isDirty = true;
    }
    @Override
    public String getFontPath () {
        return fontPath;
    }
    @Override
    public void setFontPath(String fontPath) {
        this.fontPath = fontPath;
        isDirty = true;
    }
    @Override
    public float getSize () {
        return size;
    }
    @Override
    public void setSize (float size) {
        this.size = size;
        isDirty = true;
    }
    @Override
    public boolean isDirty() {
        return isDirty;
    }
    @Override
    public void clearDirty() {
        isDirty = false;
    }
    @Override
    public float[] getPosition() {
        return pos;
    }
    @Override
    public void setPosition(float[] position) {
        if (position == null || position.length != 3)
            throw new IllegalArgumentException("Position not valid");
        pos = position;
        isDirty = true;
    }
    @Override
    public Color getColor() {return color;}
    @Override
    public void setColor(Color color) {
        this.color = color;
        isDirty = true;
    }
}
