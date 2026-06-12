package it.unicam.cs.mpgc.rpg130398.api;

import it.unicam.cs.mpgc.rpg130398.api.Vertex;
import it.unicam.cs.mpgc.rpg130398.api.ShadersSource;

/**
 * Represents an object that can be rendered in the game world.
 * Holds the geometry (vertices and triangle indices) and needs a transform (position, rotation).
 * Triangles must be defined using index triplets via getTriangleTriplets().
 *
 * The dirty flag signals the graphics engine that the object has changed and needs to be re-rendered.
 */
public interface RendableObject extends Transform {
    /**
     * Restituisce i vertici LocalSpace(Relative all'oggetto).
     * @return array di vertici
     */
    Vertex[] getObjectVertices ();
    void setObjectVertices (Vertex[] Vertices);
    /**
     * Returns the index triplets that define the triangles of this object.
     * Each group of 3 indices refers to vertices in getObjectVertices().
     * @return array of index triplets
     */
    short[] getTriangleTriplets();
    /**
     * @param triplets new index triplets, must be a multiple of 3
     */
    void setTriangleTriplets(short[] triplets);
    /**
     * Returns the preferred shader for this object.
     * @return the preferred shader, or {@code null} if it has none
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



    //VALIDATION
    /**
     * Validates that the given triplets array is not null, not empty, and a multiple of 3.
     */
    default void validateTriplets(short[] triplets) {
        if (triplets.length % 3 != 0)
            throw new IllegalArgumentException("Triplets length must be a multiple of 3, got: " + triplets.length);
    }
}
