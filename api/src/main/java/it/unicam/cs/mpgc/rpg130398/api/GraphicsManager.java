package it.unicam.cs.mpgc.rpg130398.api;

/**
 * Si occupa di disegnare oggetti 3D che implementano {@link RendableObject}.
 * Oppure testo, oggetti che implementano {@link RendableText}
 *
 * Un oggetto è visibile solo se almeno parte di esso è compresa in: 0 <= (pos.x, pos.y, pos.z) <= FRUSTUM.
 * il FRUSTUM viene chiesto nel costruttore, oppure sarà la classe Implementatrice a dichiararne uno.
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

    /**
     * Restituisce il fattore di scala applicato al contenuto mantenendone le proporzioni.
     *
     * Il valore restituito contiene due componenti:
     * <ul>
     *     <li>indice 0: scala applicata sull'asse X</li>
     *     <li>indice 1: scala applicata sull'asse Y</li>
     * </ul>
     *
     * Un valore inferiore a 1 indica che l'asse è stato compresso per
     * preservare l'aspect ratio del FRUSTUM, mentre un valore pari a 1
     * indica che non è stata applicata alcuna riduzione su quell'asse.
     *
     * @return array contenente i fattori di scala {scaleX, scaleY}
     */
    float[] getContentScale();
}
