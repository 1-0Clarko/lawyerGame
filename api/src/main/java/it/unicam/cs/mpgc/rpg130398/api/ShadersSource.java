package it.unicam.cs.mpgc.rpg130398.api;

/**
 * Shader source code for a vertex and fragment shader pair written in GLSL.
 * Renders the input triangles to the screen if their vertices have position >= 0 and <= FRUSTUM.
 */
public interface ShadersSource {
    /**
     * The VertexShader requires the following attributes for each vertex:
     *         attribute vec3 a_position;
     *         attribute vec4 a_color;
     * The VertexShader requires the following uniforms to be updated over time:
     *         uniform mat4 u_object_transform; // Moves and rotates the object's vertices in the world
     *         uniform mat4 u_screen_transform; // Projects world coordinates to clip space [-1, 1]
     *         uniform float u_time;            // Milliseconds elapsed since the object was added to the scene
     *
     * @return the source code of the VertexShader
     */
    String GetVertexShader();

    /**
     * @return the source code of the FragmentShader
     */
    String GetFragmentShader();
}
