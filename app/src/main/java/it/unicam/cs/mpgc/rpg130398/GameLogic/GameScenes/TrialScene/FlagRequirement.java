package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.TrialScene;

import it.unicam.cs.mpgc.rpg130398.api.ConnectionRequirement;
import it.unicam.cs.mpgc.rpg130398.api.DialogState;

/**
 * Requirement satisfied when the current collected flags have a
 * specific flag present
 */
public class FlagRequirement implements ConnectionRequirement {

    private final String type = "flag";
    private final String flag;

    public FlagRequirement(String flag) {
        this.flag = flag;
    }

    @Override
    public boolean isSatisfied(DialogState state) {
        System.out.println(state.getCollectedFlags().contains(flag));
        return state.getCollectedFlags().contains(flag);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "FlagRequirement{flag=" + flag + '}';
    }
}