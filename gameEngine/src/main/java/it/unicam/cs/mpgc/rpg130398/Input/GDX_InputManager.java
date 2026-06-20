package it.unicam.cs.mpgc.rpg130398.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.api.InputManager;

public class GDX_InputManager implements InputManager {
    private final Vector2 FRUSTUM;
    private final GraphicsManager Graphics;

    public GDX_InputManager(float[] FRUSTUM, GraphicsManager GraphicsManager) {
        this.FRUSTUM = new Vector2(FRUSTUM[0], FRUSTUM[1]);
        this.Graphics = GraphicsManager;
    }

    @Override
    public boolean isCursorPressed() {
        return Gdx.input.isButtonPressed(Input.Buttons.LEFT);
    }

    @Override
    public boolean isCursorJustPressed() {
        return Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
    }

    @Override
    public float[] getCursorPos() {
        // Cursor position in pixels, Gdx.input.getY() is flipped to have it conforming with the worldSpace y up
        Vector2 pos = new Vector2( Gdx.input.getX(), (Gdx.graphics.getHeight()-Gdx.input.getY()) );

        Vector2 screenSize = new Vector2( Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );
        float[] wordSpacePos = { (pos.x/screenSize.x)*FRUSTUM.x/Graphics.getContentScale()[0], (pos.y/screenSize.y)*FRUSTUM.y/Graphics.getContentScale()[1] };

        return wordSpacePos;
    }
}