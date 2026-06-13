package it.unicam.cs.mpgc.rpg130398.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.util.Vector;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.api.RendableObject;
import it.unicam.cs.mpgc.rpg130398.api.RendableText;
import it.unicam.cs.mpgc.rpg130398.resources.Shaders.Simple3D_Shader;
import it.unicam.cs.mpgc.rpg130398.api.ShadersSource;

public class GDX_GraphicsManager implements GraphicsManager {
    public static Vector3 FRUSTUM = new Vector3(16, 9, 16);
    public static int START_WIDTH = 1920,  START_HEIGHT = 1080;
    ShaderProgram DefaultShader;
    Vector<GDX_MeshRenderer> MashObjects = new Vector<>();
    Vector<GDX_TextRenderer> TextMeshObjects = new Vector<>();

    public GDX_GraphicsManager() {
        // Ridimensiona la finestra
        Gdx.graphics.setWindowedMode(START_WIDTH, START_HEIGHT);
        // Per disegniare corretamente oggetti che si sovrapongono
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);

        // Compila lo Shader per gli oggetti senza preferenze
        ShadersSource Shaders_source = new Simple3D_Shader();
        DefaultShader = new ShaderProgram(Shaders_source.GetVertexShader(), Shaders_source.GetFragmentShader());
        if (!DefaultShader.isCompiled())
            throw new RuntimeException("Shader error: " + DefaultShader.getLog());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.18f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        Matrix4 screen_projection = CalculateScreenProjection();
        for (GDX_MeshRenderer MeshObject : MashObjects) {
            MeshObject.render(screen_projection);
        }
        for (GDX_TextRenderer TextMeshObject : TextMeshObjects) {
            TextMeshObject.render(screen_projection);
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {
        for (GDX_MeshRenderer MashObject : MashObjects) {
            MashObject.dispose();
        }
    }

    @Override
    public boolean addObject(RendableObject Object) {
        for (GDX_MeshRenderer r : MashObjects)
            if (r.getObject() == Object) return false;

        MashObjects.add(new GDX_MeshRenderer(Object, DefaultShader));
        return true;
    }

    @Override
    public boolean addText(RendableText textObject) {
        for (GDX_TextRenderer r : TextMeshObjects)
            if (r.getObject() == textObject) return false;

        TextMeshObjects.add(new GDX_TextRenderer(textObject, new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
        return true;
    }

    private Matrix4 CalculateScreenProjection() {
        // Proiezione ortografica: mappa da da WorldSpace(0 <= pos.x,pos.y <= FRUSTUM) a clipSpace(-1<= pos.x,pos.y <= 1) che usa openGL
        Matrix4 screen_projection = new Matrix4();
        screen_projection.setToOrtho(0, FRUSTUM.x, 0, FRUSTUM.y, 0, FRUSTUM.z);
        screen_projection.scale(1f, 1f, -1f);// Z axis is inverted to match OpenGL convention (negative z = from you to the screen)

        // Riduce la dimensione di uno degli assi per mantenere l'aspect ratio FRUSTUM.x / FRUSTUM.y
        float frustumAspect = FRUSTUM.x / FRUSTUM.y;
        float windowAspect  = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();

        if (windowAspect > frustumAspect) {
            float scale = frustumAspect / windowAspect;
            screen_projection.scale(scale, 1f, 1f);
        } else {
            float scale = windowAspect / frustumAspect;
            screen_projection.scale(1f, scale, 1f);
        }
        return screen_projection;
    }
}