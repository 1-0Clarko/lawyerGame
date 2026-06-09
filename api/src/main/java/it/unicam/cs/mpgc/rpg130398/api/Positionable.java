package it.unicam.cs.mpgc.rpg130398.api;

/**
 * Rappresenta la posizione spaziale di un oggetto nel mondo di gioco.
 */
public interface Positionable {

    /**
     * @return posizione corrente dell'oggetto nel mondo [x, y, z]
     */
    float[] getPosition();

    /**
     * @param position nuova posizione [x, y, z]
     */
    void setPosition(float[] position);
}
