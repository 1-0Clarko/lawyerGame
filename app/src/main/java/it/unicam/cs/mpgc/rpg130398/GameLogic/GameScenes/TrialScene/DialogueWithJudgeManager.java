package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.TrialScene;

import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper.FlagsDisplayUI;
import it.unicam.cs.mpgc.rpg130398.api.GraphicsManager;

import java.util.List;

public class DialogueWithJudgeManager {

    FlagsDisplayUI flagsDisplay;
    List<String> collectedFlags;

    DialogueWithJudgeManager(GraphicsManager graphic, List<String> collectedFlags) {
        flagsDisplay = new FlagsDisplayUI(graphic);
        this.collectedFlags = collectedFlags;
    }
    boolean once = true;
    protected void update() {
        if (once) {
            flagsDisplay.update(collectedFlags);
            once = false;
        }
    }
}
