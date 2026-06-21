package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.InterrogatoryScene;

import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper.MonologueAnimation;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Generic3DObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GenericTextObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Animation;
import it.unicam.cs.mpgc.rpg130398.GameLogic.JSON_DialogLoader;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GenericDialog;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.ModelLoader;
import it.unicam.cs.mpgc.rpg130398.Graphics.PLY_ModelLoader;
import it.unicam.cs.mpgc.rpg130398.api.*;
import it.unicam.cs.mpgc.rpg130398.api.Dialog;

import java.io.IOException;
import java.util.ArrayList;

class DialogueWithDefendantManager {
    GraphicsManager Graphic;
    InputManager Input;

    ModelLoader QuestionButtonModel;

    RendableText answeresText; // The text for the answers of the dependent
    Animation answeresTextAnimation;

    record QuestionsButton (RendableText text, RendableObject rectangle, DialogNode.Connection choice){};
    QuestionsButton[] QuestionsButtons;

    Dialog DialogLogic;




    protected DialogueWithDefendantManager(GraphicsManager Graphic, InputManager Input) {
        this.Graphic = Graphic;
        this.Input = Input;

        QuestionButtonModel = new PLY_ModelLoader("models/UIButtonRectangle.ply");
        try {QuestionButtonModel.read();} catch (IOException e) {throw new RuntimeException(e);}

        answeresText = new GenericTextObject();
        answeresText.setFontPath("fonts/Undisclose.ttf");
        answeresText.setPosition(new float[]{7f,5,0});
        answeresText.setSize(1);
        Graphic.addText(answeresText);

        DialogLoader DialogLoader = new JSON_DialogLoader("DialogTrees/InterogatoryDialog.json");
        DialogLogic = new GenericDialog(DialogLoader);
        showCurrentNode();
    }

    boolean talking;
    protected void update () {
        if (!answeresTextAnimation.hasFinished()) {
            answeresTextAnimation.update();
            talking = true;
            return;
        }

        if (talking) { // once has finished talking
            listNextQuestions();
            talking = false;
            return;
        }

        QuestionsButton buttonClicked = buttonClicked();
        if (buttonClicked == null)
            return;

        DialogLogic.makeChoices(buttonClicked.choice);
        showCurrentNode();
        removeButtons();
    }
    private void listNextQuestions() {
        Graphic.removeText(answeresText);

        ArrayList<DialogNode.Connection> choices = DialogLogic.getValidChoices();

        QuestionsButtons = new QuestionsButton[choices.size()];
        float DistanceBetweenButtons = 16f/choices.size();
        for (int i = 0; i < QuestionsButtons.length; i++) {
            float xPos = DistanceBetweenButtons*i+2.4f;

            RendableText Text = new GenericTextObject();
            Text.setSize(3);
            Text.setPosition(new float[]{xPos,1,0});
            Text.setText(choices.get(i).selectionMessage());
            Graphic.addText(Text);

            RendableObject Box = new Generic3DObject();
            Box.setPosition(new float[]{xPos+3,0.5f,0});
            Box.setObjectVertices(QuestionButtonModel.getVertices());
            Box.setTriangleTriplets(QuestionButtonModel.getTriangleTriplets());
            Graphic.addObject(Box);

            QuestionsButtons[i] = new QuestionsButton(Text, Box, choices.get(i));
        }
    }

    private QuestionsButton buttonClicked() {
        if (!Input.isCursorJustPressed())
            return null;
        for (QuestionsButton Button : QuestionsButtons) {
            float[] ButtonBounds = Button.rectangle.getBoundingBox();
            // index 0=min x, 1=min y, 2=min z  3=max x, 4=max y, 5=max z

            // From localSpace to WorldSpace
            ButtonBounds[0] += Button.rectangle.getPosition()[0];
            ButtonBounds[1] += Button.rectangle.getPosition()[1];
            ButtonBounds[2] += Button.rectangle.getPosition()[2];
            ButtonBounds[3] += Button.rectangle.getPosition()[0];
            ButtonBounds[4] += Button.rectangle.getPosition()[1];
            ButtonBounds[5] += Button.rectangle.getPosition()[2];

            float[] CursorPos = Input.getCursorPos();
            float posX = CursorPos[0];
            float posY = CursorPos[1];

            // check if the x is outside the ButtonBounds
            if (posX < ButtonBounds[0] || posX > ButtonBounds[3])
                continue;
            // check if the y is outside the ButtonBounds
            if (posY < ButtonBounds[1] || posY > ButtonBounds[4])
                continue;
            // Button is clicked
            return Button;
        }
        return null;
    }
    private void removeButtons() {
        for (QuestionsButton Button : QuestionsButtons) {
            Graphic.removeObject(Button.rectangle);
            Graphic.removeText(Button.text);
        }
        QuestionsButtons = null;
    }
    private void showCurrentNode() {
        String defendantSpetch = DialogLogic.getCurrentNode().getText();
        Graphic.addText(answeresText);
        answeresTextAnimation = new MonologueAnimation(new String[]{defendantSpetch}, answeresText, 0.7f, 0);
    }
}
