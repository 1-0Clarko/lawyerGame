package it.unicam.cs.mpgc.rpg130398.resources.Shaders;

import it.unicam.cs.mpgc.rpg130398.api.ShadersSource;

public class Simple2D_Shader implements ShadersSource {

    public String GetVertexShader() { return """
        attribute vec3 a_position;
        attribute vec4 a_color;
        uniform mat4 u_object_transform;
        uniform mat4 u_screen_transform;
        varying vec4 v_color;
        void main() {
            gl_Position = u_screen_transform  * u_object_transform * vec4(a_position, 1.0);
            v_color = a_color;
        }
        """;}

    public String GetFragmentShader() { return """
        varying vec4 v_color;
        void main() {
            gl_FragColor = v_color;
        }
        """;}
}
