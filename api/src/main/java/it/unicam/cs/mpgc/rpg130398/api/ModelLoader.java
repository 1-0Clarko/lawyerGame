package it.unicam.cs.mpgc.rpg130398.api;
import java.io.IOException;
/**
 * Reads vertices and triangle triplets from a 3D model file.
 *
 */
public interface ModelLoader {

    /**
     * @param relativePath path relative to the working directory, can change it inside build.gradle.kts
     */
    void setPath(String relativePath);
    /**
     * @throws IOException if the file does not exist or cannot be read
     * @throws UnsupportedOperationException if the file format is not supported
     */
    void read() throws IOException, UnsupportedOperationException;
    /**
     * @return vertices read from the file,
     * null if the file has not been read successfully
     */
    Vertex[] getVertices();
    /**
     * @return triangle triplets read from the file.
     * Each group of 3 indices defines a triangle referencing vertices from getVertices().
     * Returns null if the file has not been read successfully.
     */
    short[] getTriangleTriplets();
}