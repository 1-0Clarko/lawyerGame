package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.InterrogatoryScene;

import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper.AnimationQueue;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper.MonologueAnimation;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Generic3DObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GenericTextObject;
import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Animation;
import it.unicam.cs.mpgc.rpg130398.GameLogic.JSON_DialogTreeLoader;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GenericDialog;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;
import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.ModelLoader;
import it.unicam.cs.mpgc.rpg130398.Graphics.PLY_ModelLoader;
import it.unicam.cs.mpgc.rpg130398.api.*;

import java.io.IOException;
import java.util.ArrayList;

class DialogueManager {
    GraphicsManager Graphic;
    ModelLoader QuestionButtonModel;

    RendableText answeresText; // The text box for the answers of the dependent
    Animation answeresTextAnimation;

    record QuestionsButton (RendableText text, RendableObject rectangle, AnimationQueue FadeInAnimation){};
    QuestionsButton[] QuestionsButtons;

    Dialog DialogLogic;

    protected DialogueManager(GraphicsManager Graphic) {
        this.Graphic = Graphic;
        QuestionButtonModel = new PLY_ModelLoader("models/UIButtonRectangle.ply");
        try {QuestionButtonModel.read();} catch (IOException e) {throw new RuntimeException(e);}

        answeresText = new GenericTextObject();
        answeresText.setFontPath("fonts/Undisclose.ttf");
        answeresText.setPosition(new float[]{7,6,0});
        answeresText.setSize(1);
        Graphic.addText(answeresText);

        DialogTreeLoader DialogLoader = new JSON_DialogTreeLoader("DialogTrees/InterogatoryDialog.json");
        DialogLogic = new GenericDialog(DialogLoader);
        answeresTextAnimation = new MonologueAnimation(new String[]{DialogLogic.getCurrentNode().getText()}, answeresText, 0.3f, 0);
    }


    protected void update () {
        if (!answeresTextAnimation.hasFinished()) {
            answeresTextAnimation.update();
            return;
        }

        nextQuestions();

        //           answeresTextAnimation = new MonologueAnimation(new String[]{DialogLogic.getCurrentNode().getText()}, answeresText, 0.3f, 0);
    }

    private void nextQuestions() {
        Graphic.removeText(answeresText);

        ArrayList<DialogNode.Connection> choices = DialogLogic.getValidChoices();
        QuestionsButtons = new QuestionsButton[choices.size()];
        float DistanceBetweenButtons = 16f/QuestionsButtons.length;
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
            QuestionsButtons[i] = new QuestionsButton(answeresText, null, null);
        }
    }
}
