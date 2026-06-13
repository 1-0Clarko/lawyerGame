package it.unicam.cs.mpgc.rpg130398.GameLogic;

import it.unicam.cs.mpgc.rpg130398.api.*;

public class GenericTextObject implements RendableText, Positionable {
    private boolean isDirty;
    String text;
    String font;

    float[] pos = new float[3];
    float size = 1;

    public GenericTextObject() {
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
    public String getFont () {
        return font;
    }
    @Override
    public void setFont (String font) {
        this.font = font;
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
}
