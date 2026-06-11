package it.unicam.cs.mpgc.rpg130398.api;

import java.awt.*;

/**
 * Un Vertex rappresenta un punto con una posizione e un colore.
 * Le coordinate spesso sono relative ad un oggetto o al mondo di gioco
 */
public interface Vertex extends Positionable {
    Color getColor();
    void setColor(Color color);
}