package it.unicam.cs.mpgc.rpg130398.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.util.Vector;

import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import it.unicam.cs.mpgc.rpg130398.api.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.api.RendableObject;
import it.unicam.cs.mpgc.rpg130398.api.RendableText;
import it.unicam.cs.mpgc.rpg130398.resources.Shaders.Simple3D_Shader;
import it.unicam.cs.mpgc.rpg130398.api.ShadersSource;

public class GDX_GraphicsManager implements GraphicsManager {
    public static Vector3 FRUSTUM;
    public static int START_WIDTH = 1280, START_HEIGHT = 720;
    ShaderProgram DefaultShader;
    Vector<GDX_MeshRenderer> mashObjects = new Vector<>();
    Vector<GDX_TextRenderer> textMeshObjects = new Vector<>();

    Matrix4 calculated_screen_projection;
    Vector2 contentScale;

    public GDX_GraphicsManager(String windowTitle) {
        // Enable depth testing to correctly render overlapping objects
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        // Enable alpha blending for transparency support
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


        // Compile the default shader for objects without a preferred shader
        ShadersSource Shaders_source = new Simple3D_Shader();
        DefaultShader = new ShaderProgram(Shaders_source.GetVertexShader(), Shaders_source.GetFragmentShader());
        if (!DefaultShader.isCompiled())
            throw new RuntimeException("Shader error: " + DefaultShader.getLog());

        Gdx.graphics.setWindowedMode(START_WIDTH, START_HEIGHT);
        Gdx.graphics.setTitle(windowTitle);
    }

    @Override
    public void setFrustum(float[] FRUSTUM_To_Use) {
        if (FRUSTUM_To_Use == null || FRUSTUM_To_Use.length != 3)
            throw new IllegalArgumentException("the FRUSTUM need to have 3 coordinate");
        FRUSTUM = new Vector3(FRUSTUM_To_Use[0], FRUSTUM_To_Use[1], FRUSTUM_To_Use[2]);
    }

    private void checkIfInitialized() {
        if (FRUSTUM == null)
            throw new IllegalStateException("the FRUSTUM is not been set, please call the method 'setFrustum'" +
                                            " with the desired FRUSTUM before using this function");
    }

    @Override
    public void render() {
        checkIfInitialized();
        // 1 Disabilita temporaneamente lo scissor per pulire l'INTERA finestra anche le parti tagliate
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glClearColor(0, 0, 0, 1); // Imposta lo sfondo esterno su nero
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // 2 Riabilita lo scissor per il disegno della geometria di gioco
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        // 3 Render 3D objects with depth testing
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        for (GDX_MeshRenderer MeshObject : mashObjects) {
            MeshObject.render(calculated_screen_projection);
        }

        // 4 Render text on top of everything, ignoring depth
        Gdx.gl.glDepthFunc(GL20.GL_ALWAYS);
        for (GDX_TextRenderer TextMeshObject : textMeshObjects) {
            TextMeshObject.render(calculated_screen_projection);
        }
    }

    @Override
    public void resize(int width, int height) {
        checkIfInitialized();

        CalculateScreenProjection();
        applyScissor();
    }

    @Override
    public void dispose() {
        for (GDX_MeshRenderer MashObject : mashObjects) {
            MashObject.dispose();
        }
    }

    @Override
    public boolean addObject(RendableObject Object) {
        for (GDX_MeshRenderer r : mashObjects)
            if (r.getObject() == Object) return false;
        mashObjects.add(new GDX_MeshRenderer(Object, DefaultShader));
        return true;
    }

    @Override
    public boolean addText(RendableText textObject) {
        for (GDX_TextRenderer r : textMeshObjects)
            if (r.getObject() == textObject) return false;
        textMeshObjects.add(new GDX_TextRenderer(textObject, new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
        return true;
    }
    @Override
    public boolean removeObject(RendableObject object) {
        for (GDX_MeshRenderer r : mashObjects) {
            if (r.getObject() == object) {
                mashObjects.remove(r);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeText(RendableText textObject) {
        for (GDX_TextRenderer r : textMeshObjects) {
            if (r.getObject() == textObject) {
                textMeshObjects.remove(r);
                return true;
            }
        }
        return false;
    }
    private void CalculateScreenProjection() {
        // Orthographic projection: maps WorldSpace [0, FRUSTUM] to clip space [-1, 1]
        calculated_screen_projection = new Matrix4();
        calculated_screen_projection.setToOrtho(0, FRUSTUM.x, 0, FRUSTUM.y, 0, FRUSTUM.z);
        // Invert z axis to match OpenGL convention.
        // From (positive z = inside the screen) to (positive z = toward the viewer)
        calculated_screen_projection.scale(1f, 1f, -1f);


        // Scale one axis to preserve the FRUSTUM aspect ratio
        float frustumAspect = FRUSTUM.x / FRUSTUM.y;
        float windowAspect  = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();

        contentScale = new Vector2(1,1);
        if (windowAspect > frustumAspect)
            contentScale.x = frustumAspect / windowAspect;
        else
            contentScale.y = windowAspect / frustumAspect;;

        calculated_screen_projection.scale(contentScale.x, contentScale.y, 1f);
    }
    /**
     * Restricts rendering to the area of the window that corresponds to the FRUSTUM.
     * Anything drawn outside this area will not be visible.
     * Should be called on resize.
     */
    private void applyScissor() {
        float frustumAspect = FRUSTUM.x / FRUSTUM.y;
        float windowAspect = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        int w, h;

        if (windowAspect > frustumAspect) {
            h = Gdx.graphics.getHeight();
            w = (int)(h * frustumAspect);
        } else {
            w = Gdx.graphics.getWidth();
            h = (int)(w / frustumAspect);
        }

        Gdx.gl.glScissor(0, 0, w, h);
    }

    // -- WindowPropriety getters

    @Override
    public float[] getContentScale() {
        return new float[] {contentScale.x, contentScale.y};
    }
    @Override
    public float[] getFrustum() {
        return new float[] {FRUSTUM.x, FRUSTUM.y, FRUSTUM.z};
    }
}