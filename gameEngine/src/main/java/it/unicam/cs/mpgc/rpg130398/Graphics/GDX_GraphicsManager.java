package it.unicam.cs.mpgc.rpg130398.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.util.Vector;

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

    public GDX_GraphicsManager() {
        // Ridimensiona la finestra
        Gdx.graphics.setWindowedMode(START_WIDTH, START_HEIGHT);

        // Compila lo Shader per gli oggetti senza preferenze
        ShadersSource Shaders_source = new Simple3D_Shader();
        DefaultShader = new ShaderProgram(Shaders_source.GetVertexShader(), Shaders_source.GetFragmentShader());
        if (!DefaultShader.isCompiled())
            throw new RuntimeException("Shader error: " + DefaultShader.getLog());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for (GDX_MeshRenderer RendableObject : MashObjects) {
            RendableObject.render();
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

        MashObjects.add(new GDX_MeshRenderer(Object, DefaultShader, FRUSTUM, new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
        return true;
    }

    @Override
    public boolean addText(RendableText textObject) {return false;}
}