package it.unicam.cs.mpgc.rpg130398.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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

        setupFont();
        resize((int) ScreenSize.x, (int) ScreenSize.y);
    }

    public void resize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    public void render(Matrix4 screen_projection) {
        if (Object.isDirty()) {
            setupFont();
            Object.clearDirty();
        }

        float[] pos = Object.getPosition();
        float scale = 0.006f;
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

    private void setupFont() {
        if (font != null) font.dispose();

        if (Object.getFontPath() != null && Gdx.files.internal(Object.getFontPath()).exists()) {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(Object.getFontPath()));
            FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

            params.size = 64;
            font = generator.generateFont(params);

            java.awt.Color c = Object.getColor();
            font.setColor(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);

            generator.dispose();
        } else {
            font = new BitmapFont();
        }

        font.getData().setScale(Object.getSize());
    }
}