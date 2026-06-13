package it.unicam.cs.mpgc.rpg130398.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import it.unicam.cs.mpgc.rpg130398.api.ShadersSource;
import it.unicam.cs.mpgc.rpg130398.api.Vertex;
import it.unicam.cs.mpgc.rpg130398.api.RendableObject;

public class GDX_MeshRenderer {
    RendableObject Object;
    Mesh Mesh;
    ShaderProgram DefaultShader;
    ShaderProgram InUseShader;

    GDX_MeshRenderer(RendableObject Object, ShaderProgram DefaultShader) {
        this.Object = Object;
        this.DefaultShader = DefaultShader;

        recalculateMash();
        setupShader();
    }
    public void render(Matrix4 screen_projection) {
        if (Object.isDirty()) {
            if(!recalculateMash())
                return;
            setupShader();
            Object.clearDirty();
        }

        InUseShader.bind();
        sendTransformDataToTheShader();
        sendScreenProjectionDataToTheShader(screen_projection);
        Mesh.render(InUseShader, GL20.GL_TRIANGLES, 0, Object.getTriangleTriplets().length);
    }
    public void dispose() {
        Mesh.dispose();
    }
    public RendableObject getObject() { return Object; }

    /**
     *
     * @return true if the mash has been created, false otherwise
     */
    private boolean recalculateMash () {
        if (Object.getObjectVertices() == null || Object.getTriangleTriplets() == null) {
            return false;
        }

        // Inizializza la mash
        Mesh = new Mesh( true, Object.getObjectVertices().length, Object.getTriangleTriplets().length,
                new VertexAttribute( VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE ),
                new VertexAttribute( VertexAttributes.Usage.ColorUnpacked, 4, ShaderProgram.COLOR_ATTRIBUTE) );

        // Estrarre pos.x, pos.y ,pos.z ,red ,green ,blue ,alpha di ogni vertice e li inserisce in un array
        float[] FormattedVertices = new float[Object.getObjectVertices().length*7];

        int i = 0;
        for (Vertex CurrentVertex : Object.getObjectVertices()) {
            // Pos
            FormattedVertices[i++] = CurrentVertex.getPosition()[0];
            FormattedVertices[i++] = CurrentVertex.getPosition()[1];
            FormattedVertices[i++] = CurrentVertex.getPosition()[2];
            // Color
            FormattedVertices[i++] = (float) CurrentVertex.getColor().getRed()/255;
            FormattedVertices[i++] = (float) CurrentVertex.getColor().getGreen()/255;
            FormattedVertices[i++] = (float) CurrentVertex.getColor().getBlue()/255;
            FormattedVertices[i++] = (float) CurrentVertex.getColor().getAlpha()/255;
        }
        Mesh.setVertices(FormattedVertices);
        Mesh.setIndices(Object.getTriangleTriplets());
        return true;
    }
    private void setupShader () {
        ShaderProgram Shader = null;
        if (Object.GetPreferdShader() != null) {
            ShadersSource Shaders_source = Object.GetPreferdShader();
            Shader = new ShaderProgram(Shaders_source.GetVertexShader(), Shaders_source.GetFragmentShader());
        } else {
            Shader = DefaultShader;
        }
        if (!Shader.isCompiled())
            throw new RuntimeException("Shader non compilata, errore");

        InUseShader = Shader;
    }
    private void sendTransformDataToTheShader () {
        float[] pos = Object.getPosition();
        float[] rot = Object.getRotation();

        // Movimento
        Matrix4 u_object_transform = new Matrix4();
        u_object_transform.setToTranslation(pos[0], pos[1], pos[2]);

        // Rotazione, usa l'ordine specificato nell'interfaccia Rotatable dell'oggetto
        u_object_transform.rotate(Vector3.Y, rot[1]);
        u_object_transform.rotate(Vector3.Z, rot[2]);
        u_object_transform.rotate(Vector3.X, rot[0]);
        InUseShader.setUniformMatrix("u_object_transform", u_object_transform);
    }
    private void sendScreenProjectionDataToTheShader (Matrix4 screen_transform) {
        InUseShader.setUniformMatrix("u_screen_transform", screen_transform);
    }
}
