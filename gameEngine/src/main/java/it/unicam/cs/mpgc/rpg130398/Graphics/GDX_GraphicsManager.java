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
    public static Vector3 FRUSTUM = new Vector3(16, 9, 16);        // TODO: FRUSTUM should be passed in the constructor
    public static int START_WIDTH = 1920, START_HEIGHT = 1080;
    ShaderProgram DefaultShader;
    Vector<GDX_MeshRenderer> MashObjects = new Vector<>();
    Vector<GDX_TextRenderer> TextMeshObjects = new Vector<>();

    public GDX_GraphicsManager() {
        Gdx.graphics.setWindowedMode(START_WIDTH, START_HEIGHT);
        // Enable depth testing to correctly render overlapping objects
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        // Enable alpha blending for transparency support
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        // TODO: clip geometry outside the FRUSTUM
        // Compile the default shader for objects without a preferred shader
        ShadersSource Shaders_source = new Simple3D_Shader();
        DefaultShader = new ShaderProgram(Shaders_source.GetVertexShader(), Shaders_source.GetFragmentShader());
        if (!DefaultShader.isCompiled())
            throw new RuntimeException("Shader error: " + DefaultShader.getLog());
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        Matrix4 screen_projection = CalculateScreenProjection();

        // Render 3D objects with depth testing
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        for (GDX_MeshRenderer MeshObject : MashObjects) {
            MeshObject.render(screen_projection);
        }

        // Render text on top of everything, ignoring depth
        Gdx.gl.glDepthFunc(GL20.GL_ALWAYS);
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
        // Orthographic projection: maps WorldSpace [0, FRUSTUM] to clip space [-1, 1]
        Matrix4 screen_projection = new Matrix4();
        screen_projection.setToOrtho(0, FRUSTUM.x, 0, FRUSTUM.y, 0, FRUSTUM.z);
        // Invert z axis to match OpenGL convention (positive z = toward the viewer)
        screen_projection.scale(1f, 1f, -1f);
        // Scale one axis to preserve the FRUSTUM aspect ratio
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