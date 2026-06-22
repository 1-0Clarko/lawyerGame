package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.InterrogatoryScene;

import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper.MonologueAnimation;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GenericTextObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Animation;
import it.unicam.cs.mpgc.rpg130398.GameLogic.JSON_DialogLoader;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GenericDialog;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.api.*;
import it.unicam.cs.mpgc.rpg130398.api.Dialog;

import java.awt.*;

/**
 * Orchestrates the interrogatory dialog: drives the {@link Dialog} state
 * machine, shows the defendant's lines, and shows/reacts to the question
 * buttons (delegated to {@link QuestionButtonsUI}).
 * <p>
 * This class owns the conversation flow;
 */
class DialogueWithDefendantManager {

    private final GraphicsManager graphic;
    private final QuestionButtonsUI buttonsUI;

    private final RendableText answersText; // The text for the answers of the defendant
    private Animation answersTextAnimation;
    private final RendableText questionText; // The text for the question of the hovered button

    protected final Dialog dialogLogic;

    private boolean talking;

    protected DialogueWithDefendantManager(GraphicsManager graphic, InputManager input) {
        this.graphic = graphic;
        this.buttonsUI = new QuestionButtonsUI(graphic, input);

        answersText = new GenericTextObject();
        answersText.setFontPath("fonts/FreeHustle Hardcore.ttf");
        answersText.setColor(Color.GRAY);
        answersText.setPosition(new float[]{7f, 6, 0});
        answersText.setSize(0.9f);
        graphic.addText(answersText);

        questionText = new GenericTextObject();
        questionText.setPosition(new float[]{1.3f, 2.5f, 0});
        questionText.setSize(4f);
        graphic.addText(questionText);

        DialogLoader dialogLoader = new JSON_DialogLoader("DialogTrees/InterogatoryDialog.json");
        dialogLogic = new GenericDialog(dialogLoader);
        showNodeDialog(dialogLogic.getCurrentNode());
    }

    protected void update() {
        if (!answersTextAnimation.hasFinished()) {
            answersTextAnimation.update();
            talking = true;
            return;
        }

        if (talking) { // once has finished talking
            buttonsUI.show(dialogLogic.getValidChoices());
            talking = false;
            return;
        }

        updateButtonsInteractions();
    }

    private void updateButtonsInteractions() {
        DialogNode.Connection hoveredChoice = buttonsUI.getHoveredChoice();
        if (hoveredChoice == null)
            return;

        questionText.setText(hoveredChoice.selectionMessage());

        if (!buttonsUI.isHoveredChoiceClicked())
            return;

        // Clicked on button
        questionText.setText("");
        DialogNode nextNode = dialogLogic.getNodeFromID(hoveredChoice.idOther());
        showNodeDialog(nextNode);
        dialogLogic.makeChoices(hoveredChoice);
        buttonsUI.removeButtons();
    }

    boolean once = true;
    private void showNodeDialog(DialogNode node) {
        String defendantSpeech = node.isVisited() ? "" : node.getText();
        if (once) { //shows the defendent text for the first node. This is necessary because node.isVisited() on the first node is always true from the start
            defendantSpeech = node.getText();
            once = false;
        }

        graphic.addText(answersText);
        answersTextAnimation = new MonologueAnimation(new String[]{defendantSpeech}, answersText, 10 * 0.7f, 0);
    }
}