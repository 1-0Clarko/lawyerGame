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

        // Inizializza la mash del triangolo con i parametri
        Mesh = new Mesh( true, Object.getObjectVertices().length, 0,
                new VertexAttribute( VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE ),
                new VertexAttribute( VertexAttributes.Usage.ColorUnpacked, 4, ShaderProgram.COLOR_ATTRIBUTE) );

        recalculateMash();
        setupShader();
        resize((int)ScreenSize.x, (int)ScreenSize.y);
    }
    public void resize(int width, int height) {
        sendScreenProjectionDataToTheShader(width, height);
    }
    public void render() {
        if (Object.isDirty()) {
            recalculateMash();
            setupShader();
            Object.clearDirty();
        }

        sendTransformDataToTheShader();
        Mesh.render(InUseShader, GL20.GL_TRIANGLES);
    }
    public void dispose() {
        Mesh.dispose();
    }
    public RendableObject getObject() { return Object; }
    private void recalculateMash () {
        // Estrarre pos.x, pos.y ,pos.z ,red ,green ,blue ,alpha da ogni vertice e inserirli in un array
        float[] FormattedVertices = new float[Object.getObjectVertices().length*7];

        int i = 0;
        for (Vertex CurrentVertex : Object.getObjectVertices()) {
            // Pos
            FormattedVertices[i++] = CurrentVertex.getPosition()[0];
            FormattedVertices[i++] = CurrentVertex.getPosition()[1];
            FormattedVertices[i++] = CurrentVertex.getPosition()[2];
            // Color
            FormattedVertices[i++] = CurrentVertex.GetColor().getRed();
            FormattedVertices[i++] = CurrentVertex.GetColor().getGreen();
            FormattedVertices[i++] = CurrentVertex.GetColor().getBlue();
            FormattedVertices[i++] = CurrentVertex.GetColor().getAlpha();
        }
        Mesh.setVertices(FormattedVertices);
    }
    private void setupShader () {
        ShaderProgram Shader = null;
        if (Object.GetPreferdShader() != null) {
            ShadersSource Shaders_source = Object.GetPreferdShader();
            Shader = new ShaderProgram(Shaders_source.GetVertexShader(), Shaders_source.GetFragmentShader());
        } else {
            Shader = DefaultShader;
        }
        if (!Shader.isCompiled()) // non DefaultShader
            throw new RuntimeException("Shader non compilata");

        Shader.bind();

        InUseShader = Shader;
    }
    private void sendTransformDataToTheShader () {
        // Nessuna trasformazione applicata: i vertici restano nelle coordinate originali
        Matrix4 u_object_transform = new Matrix4().idt();
        InUseShader.setUniformMatrix("u_object_transform", u_object_transform);

        // Invia la profondità massima, serve per rendere più scuri gli oggetti lontani
        InUseShader.setUniformf("u_max_depth", FRUSTUM.z);
    }
    private void sendScreenProjectionDataToTheShader (int width, int height) {
        // Proiezione ortografica: mappa da da WorldSpace(0 <= pos.x,pos.y <= FRUSTUM) a clipSpace(-1<= pos.x,pos.y <= 1) che usa openGL
        Matrix4 u_screen_transform = new Matrix4();
        u_screen_transform.setToOrtho(0, FRUSTUM.x, 0, FRUSTUM.y, 0, FRUSTUM.z);

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
