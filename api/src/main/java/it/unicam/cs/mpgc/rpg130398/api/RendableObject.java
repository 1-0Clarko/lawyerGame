package it.unicam.cs.mpgc.rpg130398.api;

import it.unicam.cs.mpgc.rpg130398.api.Vertex;
import it.unicam.cs.mpgc.rpg130398.api.ShadersSource;

public interface RendableObject extends Transform {
    /**
     * Restituisce i vertici LocalSpace(Relative all'oggetto).
     * @return array di vertici
     */
    Vertex[] getObjectVertices ();
    void setObjectVertices (Vertex[] Vertices);
    /**
     * Restituisce la Shader che questo oggetto vorrebbe usare
     */
    ShadersSource GetPreferdShader ();
    /**
     * Imposta la Shader che questo oggetto vorrebbe usare.
     * Non è garantito che questa preferenza venga applicata
     */
    void SetPreferdShader (ShadersSource ShadersSource);
    /**
     * @return true se qualcosa è cambiato nell'oggetto vertici ecc
     */
    boolean isDirty();
    /**
     * Chiamato dal graphics engine dopo aver letto isDirty
     */
    void clearDirty();
}
