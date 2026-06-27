package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.InterrogatoryScene.Dialog;

import it.unicam.cs.mpgc.rpg130398.api.dialog.DialogState;

public interface DialogStateTrust extends DialogState {
    float getTrust();
    void addTrust(float trustDelta);
}
