package it.unicam.cs.mpgc.rpg130398.api;

/**
 * Shader source code per una coppia di vertex e fragment shaders scritte in GLSL.
 * Renderizza i triangoli in input a schermo, se hanno vertici con posizione >= 0 e <= del FRUSTUM
 */
public interface ShadersSource {
    /**
     * la VertexShader necessita dei seguenti attribuiti per ogni vertice:
     *         attribute vec3 a_position;
     *         attribute vec4 a_color;
     * la VertexShader necessita dei seguenti uniform da aggiornare nel tempo:
     *         uniform mat4 u_object_transform; // Sposta e ruota i vertici dell'oggetto nel mondo
     *         uniform mat4 u_screen_transform; // Proietta le coordinate del mondo in clip space [-1, 1]
     *
     * @return il source code della VertexShader
     */
    String GetVertexShader();
    /**
     * @return il source code della FragmentShader
     */
    String GetFragmentShader();
}
