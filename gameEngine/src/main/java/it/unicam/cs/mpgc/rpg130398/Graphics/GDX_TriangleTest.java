package it.unicam.cs.mpgc.rpg130398.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.api.RendableObject;
import it.unicam.cs.mpgc.rpg130398.api.RendableText;
import it.unicam.cs.mpgc.rpg130398.resources.Shaders.Simple2D_Shader;
import it.unicam.cs.mpgc.rpg130398.api.ShadersSource;

/**
 * Semplice implementazione di iGraphicsManager che disegna un triangolo a schermo
 * Non aggiunge oggetti
 */
public class GDX_TriangleTest implements GraphicsManager {
    public static Vector3 FRUSTUM = new Vector3(16, 9, 16);
    public static int START_WIDTH = 1920,  START_HEIGHT = 1080;

    private Mesh TriangleMesh;
    float[] vertices = new float[]{
        //  x    y    z    r    g    b    a
            0f,  0f,  0f,  0f,  0f,  1f,  1f,   // bottom-left  — blu
            16f, 0f,  0f,  0f,  1f,  0f,  1f,   // bottom-right — verde
            8f,  9f,  0f,  1f,  0.8f,0f,  1f,   // top-center   — gold
    };
    private ShaderProgram Shader;

    public GDX_TriangleTest() {
        // Ridimensiona la finestra
        Gdx.graphics.setWindowedMode(START_WIDTH, START_HEIGHT);

        // Crea la mash del triangolo
        TriangleMesh = new Mesh( true, 3, 0,
                new VertexAttribute( Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE ),
                new VertexAttribute( Usage.ColorUnpacked, 4, ShaderProgram.COLOR_ATTRIBUTE) );
        TriangleMesh.setVertices(vertices);

        // Compila le shader
        ShadersSource Shaders_source = new Simple2D_Shader();
        Shader = new ShaderProgram(Shaders_source.GetVertexShader(), Shaders_source.GetFragmentShader());
        if (!Shader.isCompiled())
            throw new RuntimeException("Shader error: " + Shader.getLog());
        Shader.bind();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        TriangleMesh.render(Shader, GL20.GL_TRIANGLES);
    }

    @Override
    public void resize(int width, int height) {

        // Nessuna trasformazione applicata: i vertici restano nelle coordinate originali
        Matrix4 u_object_transform = new Matrix4().idt();
        Shader.setUniformMatrix("u_object_transform", u_object_transform);

        // Proiezione ortografica: mappa da da WorldSpace(0 <= pos.x,pos.y <= FRUSTUM) a clipSpace(-1<= pos.x,pos.y <= 1)
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
        Shader.setUniformMatrix("u_screen_transform", u_screen_transform);
    }

    @Override
    public void dispose() {
        TriangleMesh.dispose();
    }

    @Override
    public boolean addObject(RendableObject object) {return false;}

    @Override
    public boolean addText(RendableText textObject) {return false;}
}