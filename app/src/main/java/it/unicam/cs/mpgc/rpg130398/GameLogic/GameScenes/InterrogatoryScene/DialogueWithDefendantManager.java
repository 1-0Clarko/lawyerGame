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
import java.util.ArrayList;

/**
 * Orchestrates the interrogatory dialog: drives the {@link Dialog} state
 * machine, shows the defendant's lines, shows/reacts to the question buttons
 * (delegated to {@link QuestionButtonsUI}), and shows the collected flags
 * (delegated to {@link FlagsDisplayUI}).
 * <p>
 * This class owns the conversation flow;
 */
class DialogueWithDefendantManager {

    private static final String NO_MORE_QUESTIONS_MESSAGE = "Non ho altre domande, ci vediamo in tribunale.";

    private final GraphicsManager graphic;
    private final QuestionButtonsUI buttonsUI;
    private final FlagsDisplayUI flagsUI;

    private final RendableText answersText; // The text for the answers of the defendant
    private Animation answersTextAnimation;
    private final RendableText questionText; // The text for the question of the hovered button

    protected final Dialog dialogLogic;

    private boolean talking;
    private boolean awaitingFallbackClick; // true while only the "no more questions" button is shown
    private boolean dialogOver; // true once the fallback button has been clicked

    protected DialogueWithDefendantManager(GraphicsManager graphic, InputManager input) {
        this.graphic = graphic;
        this.buttonsUI = new QuestionButtonsUI(graphic, input);
        this.flagsUI = new FlagsDisplayUI(graphic);

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

    /**
     * @return true once the player clicked the "no more questions" fallback
     * button (no DialogNode.Connection was valid from the current node).
     */
    protected boolean isOver() {
        return dialogOver;
    }

    protected void update() {
        flagsUI.update(new ArrayList<>(dialogLogic.getOpinionatedFlags()));

        if (!answersTextAnimation.hasFinished()) {
            answersTextAnimation.update();
            talking = true;
            return;
        }

        if (talking) { // once has finished talking
            showNextQuestionsOrFallback();
            talking = false;
            return;
        }

        if (awaitingFallbackClick) {
            updateFallbackInteraction();
            return;
        }

        updateButtonsInteractions();
    }

    protected void clear() {
        graphic.removeText(answersText);
        graphic.removeText(questionText);
        buttonsUI.removeButtons();
        flagsUI.removeRows();
    }

    private void showNextQuestionsOrFallback() {
        ArrayList<DialogNode.Connection> choices = dialogLogic.getValidChoices();
        if (choices.isEmpty()) {
            awaitingFallbackClick = true;
            DialogNode.Connection fallback = new DialogNode.Connection(
                    dialogLogic.getCurrentNode().getId(), NO_MORE_QUESTIONS_MESSAGE, null, 0);
            ArrayList<DialogNode.Connection> fallbackChoices = new ArrayList<>();
            fallbackChoices.add(fallback);
            buttonsUI.show(fallbackChoices);
            return;
        }
        buttonsUI.show(choices);
    }

    private void updateFallbackInteraction() {
        DialogNode.Connection hoveredChoice = buttonsUI.getHoveredChoice();
        if (hoveredChoice == null)
            questionText.setText("");
        else
            questionText.setText(hoveredChoice.selectionMessage());

        if (!buttonsUI.isHoveredChoiceClicked())
            return;

        questionText.setText("");
        buttonsUI.removeButtons();
        dialogOver = true;
    }

    private void updateButtonsInteractions() {
        DialogNode.Connection hoveredChoice = buttonsUI.getHoveredChoice();
        if (hoveredChoice == null)
            questionText.setText("");
        else
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
        answersTextAnimation = new MonologueAnimation(new String[]{defendantSpeech}, answersText, 1f, 0);
    }
}