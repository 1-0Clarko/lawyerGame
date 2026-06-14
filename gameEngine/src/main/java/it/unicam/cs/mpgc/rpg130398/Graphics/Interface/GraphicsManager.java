package it.unicam.cs.mpgc.rpg130398.Graphics.Interface;

import it.unicam.cs.mpgc.rpg130398.api.RendableObject;
import it.unicam.cs.mpgc.rpg130398.api.RendableText;

/**
 * Si occupa di disegnare oggetti 3D che implementano {@link RendableObject}.
 * Oppure testo, oggetti che implementano {@link RendableText}
 *
 * Un oggetto è visibile solo se almeno parte di esso è compresa in: 0 <= pos.x, pos.y <= FRUSTUM.
 * il FRUSTUM viene chiesto nel costruttore, oppure sarà la classe implementatrice a dichiararne uno.
 *
 * Per mostrare un oggetto che implementa {@link RendableObject} o {@link RendableText} in scena, basta aggiungere quell'ogetto utilizzando i rispettivi metodi: {addObject} e {addText}
 */
public interface GraphicsManager {
    /**
     * Ridisegna la scena corrente.
     */
    void render();
    /**
     * Ridimensiona la finestra
     */
    void resize(int width, int height);
    /**
     * Libera le risorse grafiche. Da chiamare prima della chiusura del programma.
     */
    void dispose();
    /**
     * Aggiunge un oggetto alla scena. Non aggiunge duplicati.
     * L'implementazione può rifiutare l'oggetto.
     *
     * @param Object oggetto da aggiungere alla scena
     * @return se è stato aggiunto l'oggetto
     */
    boolean addObject(RendableObject Object);
    /**
     * Aggiunge testo alla scena. Non aggiunge duplicati.
     * L'implementazione può rifiutare il testo.
     *
     * @param TextObject testo da aggiungere alla scena
     * @return se il testo è stato aggiunto
     */
    boolean addText(RendableText TextObject);
    /**
     * Rimuove un oggetto dalla scena.
     *
     * @param object oggetto da rimuovere
     * @return true se l'oggetto è stato rimosso
     */
    boolean removeObject(RendableObject object);

    /**
     * Rimuove un testo dalla scena.
     *
     * @param textObject testo da rimuovere
     * @return true se il testo è stato rimosso
     */
    boolean removeText(RendableText textObject);
}
