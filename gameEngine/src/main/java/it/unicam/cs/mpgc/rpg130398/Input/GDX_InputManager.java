package it.unicam.cs.mpgc.rpg130398.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import it.unicam.cs.mpgc.rpg130398.api.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.api.InputManager;
import it.unicam.cs.mpgc.rpg130398.api.WindowPropriety;

public class GDX_InputManager implements InputManager {
    private final WindowPropriety GameWindow;

    public GDX_InputManager(WindowPropriety GameWindow) {
        this.GameWindow = GameWindow;
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
        float[] FRUSTUM = GameWindow.getFrustum();

        // Cursor position in pixels, Gdx.input.getY() is flipped to have it conforming with the worldSpace y up
        Vector2 pos = new Vector2( Gdx.input.getX(), (Gdx.graphics.getHeight()-Gdx.input.getY()) );

        Vector2 screenSize = new Vector2( Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );
        float[] wordSpacePos = { (pos.x/screenSize.x)*FRUSTUM[0], (pos.y/screenSize.y)*FRUSTUM[1] };

        // Applica la scala che il GraphicManager usa per il contenuto
        wordSpacePos[0] = wordSpacePos[0] / GameWindow.getContentScale()[0];
        wordSpacePos[1] = wordSpacePos[1] / GameWindow.getContentScale()[1];

        return wordSpacePos;
    }
}