package it.unicam.cs.mpgc.rpg130398.api;

/**
 * Rotazione di un oggetto nel mondo di gioco, espressa come angoli di Euler in gradi.
 * Ordine di applicazione: Y (up), Z (front), X (right).
 */
public interface Rotatable {

    /**
     * @return rotazione corrente come [x, y, z] in gradi
     */
    float[] getRotation();

    /**
     * @param rotation nuova rotazione come [x, y, z] in gradi
     */
    void setRotation(float[] rotation);
}