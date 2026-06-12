package it.unicam.cs.mpgc.rpg130398.GameLogic;

import it.unicam.cs.mpgc.rpg130398.api.RendableObject;
import it.unicam.cs.mpgc.rpg130398.api.ShadersSource;
import it.unicam.cs.mpgc.rpg130398.api.Transform;
import it.unicam.cs.mpgc.rpg130398.api.Vertex;

public class Generic3DObject implements RendableObject, Transform {
    private boolean isDirty;
    Vertex[] vertices;
    ShadersSource PreferdShader;

    float[] pos = new float[3];
    float[] rot = new float[3];

    public Generic3DObject() {
        isDirty = true;
    }

    @Override
    public Vertex[] getObjectVertices () {
        return vertices;
    }
    @Override
    public void setObjectVertices (Vertex[] Vertices) {
        vertices = Vertices;
        isDirty = true;
    }
    @Override
    public short[] getTriangleTriplets () {
        //TODO In the next commit XD
        return null;
    }
    @Override
    public void setTriangleTriplets (short[] triplets) {
        //TODO In the next commit XD
    }
    @Override
    public ShadersSource GetPreferdShader () {
        return PreferdShader;
    }
    @Override
    public void SetPreferdShader (ShadersSource ShadersSource) {
        PreferdShader = ShadersSource;
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
    public float[] getRotation() {
        return rot;
    }
    @Override
    public void setRotation(float[] rotation) {
        if (rotation == null || rotation.length != 3)
            throw new IllegalArgumentException("Position not valid");
        rot = rotation;
        isDirty = true;
    }
}
