package it.unicam.cs.mpgc.rpg130398.GameLogic.Interface;

import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.GraphicsManager;

/**
 * Represents a single phase of the game (e.g. menu, gameplay, cutscene).
 * Each phase manages its own logic and decides when to transition to the next phase.
 */
public interface GameFase {
    /**
     * Updates the game logic for this phase. Called 30 times per second.
     *
     * @param frameNumber frame counter since the start of the program
     * @return the next game phase, or itself if the phase has not ended.
     */
    GameFase update(long frameNumber);
}