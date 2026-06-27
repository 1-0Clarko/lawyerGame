package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.InterrogatoryScene.Dialog;

import it.unicam.cs.mpgc.rpg130398.api.dialog.Dialog;
import it.unicam.cs.mpgc.rpg130398.api.dialog.DialogState;
import it.unicam.cs.mpgc.rpg130398.api.dialog.ConnectionRequirement;

/**
 * Requirement satisfied when the current trust value of the conversation is within
 * [minTrust, maxTrust] (inclusive on both ends).
 */
public class TrustRequirement implements ConnectionRequirement {

    private final String type = "trust";
    private final int minTrust;
    private final int maxTrust;

    public TrustRequirement(int minTrust, int maxTrust) {
        this.minTrust = minTrust;
        this.maxTrust = maxTrust;
    }

    @Override
    public boolean isSatisfiedBy(Dialog dialog) {
        if (dialog instanceof DialogStateTrust TrustState) {
            return (TrustState.getTrust() >= minTrust && TrustState.getTrust() <= maxTrust);
        }

        DialogStateNotPresent();
        return false;
    }

    @Override
    public String getType() {
        return type;
    }

    public int getMinTrust() { return minTrust; }
    public int getMaxTrust() { return maxTrust; }

    @Override
    public String toString() {
        return "TrustRequirement{minTrust=" + minTrust + ", maxTrust=" + maxTrust + '}';
    }
}