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
    public Vector3 FRUSTUM;

    GDX_MeshRenderer(RendableObject Object, ShaderProgram DefaultShader, Vector3 FRUSTUM, Vector2 ScreenSize) {
        this.Object = Object;
        this.FRUSTUM = FRUSTUM;
        this.DefaultShader = DefaultShader;

        recalculateMash();
        setupShader();
        resize((int)ScreenSize.x, (int)ScreenSize.y);
    }
    public void resize(int width, int height) {
        sendScreenProjectionDataToTheShader(width, height);
    }
    public void render() {
        if (Object.isDirty()) {
            if(!recalculateMash())
                return;
            setupShader();
            Object.clearDirty();
        }

        InUseShader.bind();
        sendTransformDataToTheShader();
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
        // Nessuna trasformazione applicata: i vertici restano nelle coordinate originali
        Matrix4 u_object_transform = new Matrix4().idt();
        InUseShader.setUniformMatrix("u_object_transform", u_object_transform);
    }
    private void sendScreenProjectionDataToTheShader (int width, int height) {
        // Proiezione ortografica: mappa da da WorldSpace(0 <= pos.x,pos.y <= FRUSTUM) a clipSpace(-1<= pos.x,pos.y <= 1) che usa openGL
        Matrix4 u_screen_transform = new Matrix4();
        u_screen_transform.setToOrtho(0, FRUSTUM.x, 0, FRUSTUM.y, 0, FRUSTUM.z);
        u_screen_transform.scale(1f, 1f, -1f);// Z axis is inverted to match OpenGL convention (negative z = away from camera)

        // Riduce la dimensione di uno degli assi per mantenere l'aspect ratio FRUSTUM.x / FRUSTUM.y
        float frustumAspect = FRUSTUM.x / FRUSTUM.y;
        float windowAspect  = (float) width / height;

        if (windowAspect > frustumAspect) {
            float scale = frustumAspect / windowAspect;
            u_screen_transform.scale(scale, 1f, 1f);
        } else {
            float scale = windowAspect / frustumAspect;
            u_screen_transform.scale(1f, scale, 1f);
        }
        InUseShader.setUniformMatrix("u_screen_transform", u_screen_transform);
    }
}
