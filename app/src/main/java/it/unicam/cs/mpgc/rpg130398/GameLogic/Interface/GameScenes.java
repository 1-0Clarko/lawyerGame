package it.unicam.cs.mpgc.rpg130398.GameLogic.Interface;

/**
 * Represents a single phase of the game (e.g. menu, gameplay, cutscene).
 * Each phase manages its own logic and decides when to transition to the next phase.
 */
public interface GameScenes {
    /**
     * Updates the game logic for this phase. Called 30 times per second.
     *
     * @param frameNumber frame counter since the start of the program
     * @return the next game phase, or itself if the phase has not ended.
     * or null if the game is finished
     */
    GameScenes update(long frameNumber);
}