package it.unicam.cs.mpgc.rpg130398.GameLogic;

import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.ModelLoader;
import it.unicam.cs.mpgc.rpg130398.api.RendableObject;
import it.unicam.cs.mpgc.rpg130398.api.ShadersSource;
import it.unicam.cs.mpgc.rpg130398.api.Transform;
import it.unicam.cs.mpgc.rpg130398.api.Vertex;

public class Generic3DObject implements RendableObject, Transform {
    private boolean isDirty;
    Vertex[] vertices;
    short[] TriangleTriplets;
    ShadersSource PreferdShader;

    float[] pos = new float[3];
    float[] rot = new float[3];

    public Generic3DObject() {
        isDirty = true;
    }

    public Generic3DObject(ModelLoader ModelLoader) {
        try {
            ModelLoader.read();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        setObjectVertices(ModelLoader.getVertices());
        setTriangleTriplets(ModelLoader.getTriangleTriplets());

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
        return TriangleTriplets;
    }
    @Override
    public void setTriangleTriplets (short[] triplets) {
        validateTriplets(triplets);
        TriangleTriplets = triplets;
        isDirty = true;
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
    public float[] getBoundingBox() {
        float[] boundBox = new float[6];
        for (Vertex vertex : vertices) {
            float[] vertPos = vertex.getPosition();

            // Minimum pos
            boundBox[0] = Math.min(boundBox[0], vertPos[0]); //min x
            boundBox[1] = Math.min(boundBox[1], vertPos[1]); //min y
            boundBox[2] = Math.min(boundBox[2], vertPos[2]); //min z
            // Maximum pos
            boundBox[3] = Math.max(boundBox[3], vertPos[0]); //max x
            boundBox[4] = Math.max(boundBox[4], vertPos[1]); //max y
            boundBox[5] = Math.max(boundBox[5], vertPos[2]); //max z
        }
        return boundBox;
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
