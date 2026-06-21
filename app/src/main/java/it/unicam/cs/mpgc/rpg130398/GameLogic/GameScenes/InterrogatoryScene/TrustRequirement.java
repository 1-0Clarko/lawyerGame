package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.InterrogatoryScene;

import it.unicam.cs.mpgc.rpg130398.api.DialogState;
import it.unicam.cs.mpgc.rpg130398.api.ConnectionRequirement;

/**
 * Requirement satisfied when the current trust value lies within
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
    public boolean isSatisfied(DialogState state) {
        int trust = state.getTrust();
        return trust >= minTrust && trust <= maxTrust;
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