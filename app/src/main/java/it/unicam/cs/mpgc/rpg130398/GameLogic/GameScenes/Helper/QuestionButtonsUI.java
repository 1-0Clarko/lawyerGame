package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper;

import it.unicam.cs.mpgc.rpg130398.GameLogic.Generic3DObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GenericTextObject;
import it.unicam.cs.mpgc.rpg130398.api.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.api.ModelLoader;
import it.unicam.cs.mpgc.rpg130398.GameLogic.PLY_ModelLoader;
import it.unicam.cs.mpgc.rpg130398.api.dialog.DialogNode;
import it.unicam.cs.mpgc.rpg130398.api.InputManager;
import it.unicam.cs.mpgc.rpg130398.api.RendableObject;
import it.unicam.cs.mpgc.rpg130398.api.RendableText;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Owns the lifecycle of the on-screen question buttons: creating them from a
 * list of available choices, detecting hover/click, and removing them once a
 * choice has been made.
 * <p>
 * This class knows nothing about dialog trees or trust: it only turns a list
 * of {@link DialogNode.Connection} into clickable 3D buttons and reports back
 * which one (if any) is hovered or clicked.
 */
public class QuestionButtonsUI {

    private static final int MAX_LENGTH_TEXT_ON_BUTTON = 30;

    private final GraphicsManager graphic;
    private final InputManager input;
    private final ModelLoader questionButtonModel;

    private record QuestionButton(RendableText text, RendableObject rectangle, DialogNode.Connection choice) {}

    private QuestionButton[] questionButtons = new QuestionButton[0];

    public QuestionButtonsUI(GraphicsManager graphic, InputManager input) {
        this.graphic = graphic;
        this.input = input;

        questionButtonModel = new PLY_ModelLoader("models/UIButtonRectangle.ply");
        try {
            questionButtonModel.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates and shows one button per available choice, replacing any
     * buttons currently on screen.
     */
    public void show(ArrayList<DialogNode.Connection> choices) {
        removeButtons();

        questionButtons = new QuestionButton[choices.size()];
        float distanceBetweenButtons = 16f / choices.size();

        for (int i = 0; i < questionButtons.length; i++) {
            float xPos = distanceBetweenButtons * i + 2.4f;

            String lineToDisplay = truncateStringForButton(choices.get(i).selectionMessage());

            RendableText text = new GenericTextObject();
            text.setSize(3);
            text.setPosition(new float[]{xPos - 0.7f, 1, 0});
            text.setText(lineToDisplay);
            graphic.addText(text);

            RendableObject box = new Generic3DObject();
            box.setPosition(new float[]{xPos + 3, 0.5f, 0});
            box.setObjectVertices(questionButtonModel.getVertices());
            box.setTriangleTriplets(questionButtonModel.getTriangleTriplets());
            graphic.addObject(box);

            questionButtons[i] = new QuestionButton(text, box, choices.get(i));
        }
    }

    /**
     * @return the corresponding connection of the button currently under the cursor, or
     * null if none is hovered (or no buttons are shown).
     */
    public DialogNode.Connection getHoveredChoice() {
        QuestionButton hovered = hoveredButton();
        return hovered == null ? null : hovered.choice();
    }

    /**
     * @return true if the cursor was just pressed while hovering a button
     */
    public boolean isHoveredChoiceClicked() {
        return hoveredButton() != null && input.isCursorJustPressed();
    }

    /**
     * Removes all currently shown buttons from the graphics and clears them.
     */
    public void removeButtons() {
        for (QuestionButton button : questionButtons) {
            graphic.removeObject(button.rectangle());
            graphic.removeText(button.text());
        }
        questionButtons = new QuestionButton[0];
    }

    private String truncateStringForButton(String text) {
        String firstSentence = text.split("[.,?]", 2)[0];
        return firstSentence.length() > MAX_LENGTH_TEXT_ON_BUTTON
                ? firstSentence.substring(0, MAX_LENGTH_TEXT_ON_BUTTON) + "-"
                : firstSentence;
    }

    private QuestionButton hoveredButton() {
        for (QuestionButton button : questionButtons) {
            float[] bounds = button.rectangle().getBoundingBox();
            // index 0=min x, 1=min y, 2=min z  3=max x, 4=max y, 5=max z

            // From localSpace to WorldSpace
            float[] position = button.rectangle().getPosition();
            bounds[0] += position[0];
            bounds[1] += position[1];
            bounds[2] += position[2];
            bounds[3] += position[0];
            bounds[4] += position[1];
            bounds[5] += position[2];

            float[] cursorPos = input.getCursorPos();
            float posX = cursorPos[0];
            float posY = cursorPos[1];

            if (posX < bounds[0] || posX > bounds[3])
                continue;
            if (posY < bounds[1] || posY > bounds[4])
                continue;

            return button;
        }
        return null;
    }
}
