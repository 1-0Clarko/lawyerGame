package it.unicam.cs.mpgc.rpg130398.Graphics;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import it.unicam.cs.mpgc.rpg130398.api.ShadersSource;
import it.unicam.cs.mpgc.rpg130398.api.Vertex;
import it.unicam.cs.mpgc.rpg130398.api.RendableObject;

public class GDX_MeshRenderer {
    RendableObject object;
    Mesh mesh;
    ShaderProgram defaultShader;
    ShaderProgram inUseShader;
    final long creationTimeMillis;

    GDX_MeshRenderer(RendableObject Object, ShaderProgram DefaultShader) {
        this.object = Object;
        this.defaultShader = DefaultShader;
        creationTimeMillis = System.currentTimeMillis();

        recalculateMash();
        setupShader();
    }
    public void render(Matrix4 screen_projection) {
        if (object.isDirty()) {
            if(!recalculateMash())
                return;
            setupShader();
            object.clearDirty();
        }

        inUseShader.bind();
        sendTransformDataToTheShader();
        sendScreenProjectionDataToTheShader(screen_projection);
        sendTimeDataToTheShader();
        mesh.render(inUseShader, GL20.GL_TRIANGLES, 0, object.getTriangleTriplets().length);
    }
    public void dispose() {
        mesh.dispose();
    }
    public RendableObject getObject() { return object; }

    /**
     *
     * @return true if the mash has been created, false otherwise
     */
    private boolean recalculateMash () {
        if (object.getObjectVertices() == null || object.getTriangleTriplets() == null) {
            return false;
        }

        // Inizializza la mash
        mesh = new Mesh( true, object.getObjectVertices().length, object.getTriangleTriplets().length,
                new VertexAttribute( VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE ),
                new VertexAttribute( VertexAttributes.Usage.ColorUnpacked, 4, ShaderProgram.COLOR_ATTRIBUTE) );

        // Estrarre pos.x, pos.y ,pos.z ,red ,green ,blue ,alpha di ogni vertice e li inserisce in un array
        float[] FormattedVertices = new float[object.getObjectVertices().length*7];

        int i = 0;
        for (Vertex CurrentVertex : object.getObjectVertices()) {
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
        mesh.setVertices(FormattedVertices);
        mesh.setIndices(object.getTriangleTriplets());
        return true;
    }
    private void setupShader () {
        ShaderProgram Shader = null;
        if (object.GetPreferdShader() != null) {
            ShadersSource Shaders_source = object.GetPreferdShader();
            Shader = new ShaderProgram(Shaders_source.GetVertexShader(), Shaders_source.GetFragmentShader());
        } else {
            Shader = defaultShader;
        }
        if (!Shader.isCompiled())
            throw new RuntimeException("Shader non compilata, errore");

        inUseShader = Shader;
    }
    private void sendTransformDataToTheShader () {
        float[] pos = object.getPosition();
        float[] rot = object.getRotation();

        // Movimento
        Matrix4 u_object_transform = new Matrix4();
        u_object_transform.setToTranslation(pos[0], pos[1], pos[2]);

        // Rotazione, usa l'ordine specificato nell'interfaccia Rotatable dell'oggetto
        u_object_transform.rotate(Vector3.Y, rot[1]);
        u_object_transform.rotate(Vector3.Z, rot[2]);
        u_object_transform.rotate(Vector3.X, rot[0]);
        inUseShader.setUniformMatrix("u_object_transform", u_object_transform);
    }
    private void sendScreenProjectionDataToTheShader (Matrix4 screen_transform) {
        inUseShader.setUniformMatrix("u_screen_transform", screen_transform);
    }
    private void sendTimeDataToTheShader () {
        inUseShader.setUniformf("u_time", System.currentTimeMillis()- creationTimeMillis);
    }
}
