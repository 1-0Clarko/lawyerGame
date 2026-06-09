package it.unicam.cs.mpgc.rpg130398.GameLogic.Interface;

/**
    un gioco con qualche tipo di grafica
 */
public interface Game {
    /**
     * Si occupa di far funzionare la logica del gioco.
     * Deve essere chiamato esattamente 30 volte per secondo.
     *
     * @param FrameNumber contatore di frame dall'inizio del programma
     */
    void updateLogic(long FrameNumber);
    /**
     * Ridisegna la grafica del gioco,
     * Puo essere testuale o altro
     */
    void render();
    /**
     * Cambia la dimensione della Grafica
     */
    void resizeWindow(int width, int height);
    void dispose();
}