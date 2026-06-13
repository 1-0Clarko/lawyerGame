package it.unicam.cs.mpgc.rpg130398.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import it.unicam.cs.mpgc.rpg130398.api.RendableText;

public class GDX_TextRenderer {
    RendableText Object;
    SpriteBatch batch;
    BitmapFont font;
    int screenWidth, screenHeight;

    GDX_TextRenderer(RendableText Object, Vector2 ScreenSize) {
        this.Object = Object;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont(); // default GDX font
        resize((int) ScreenSize.x, (int) ScreenSize.y);
    }

    public void resize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    public void render(Matrix4 screen_projection) {
        if (Object.isDirty()) {
            font.dispose();
            font = new BitmapFont();
            Object.clearDirty();
        }

        float[] pos = Object.getPosition();
        float scale = 0.04f;
        batch.setProjectionMatrix(screen_projection);
        batch.setTransformMatrix(new Matrix4().scale(scale, scale, scale));
        batch.begin();
        font.draw(batch, Object.getText(), pos[0]/scale, pos[1]/scale);
        batch.end();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public RendableText getObject() { return Object; }
}