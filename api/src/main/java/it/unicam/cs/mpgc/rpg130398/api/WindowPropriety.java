package it.unicam.cs.mpgc.rpg130398.api;

/**
 * Read only view of window related proprieties
 * Associated with a GraphicsManager
 */
public interface WindowPropriety {
    /**
     * Ottieni il FRUSTUM utilizzato
     */
    float[] getFrustum();
    /**
     * Restituisce il fattore di scala applicato al contenuto per mantenere le proporzioni.
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
