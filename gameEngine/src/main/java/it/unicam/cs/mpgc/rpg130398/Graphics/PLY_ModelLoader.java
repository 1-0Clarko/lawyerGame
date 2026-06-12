package it.unicam.cs.mpgc.rpg130398.Graphics;

import it.unicam.cs.mpgc.rpg130398.Graphics.Interface.ModelLoader;
import it.unicam.cs.mpgc.rpg130398.api.Vertex;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

/* example file .ply:
ply
format ascii 1.0
comment Created in Blender version 5.1.0
element vertex 8
property float x
property float y
property float z
property uchar red
property uchar green
property uchar blue
property uchar alpha
element face 12
property list uchar uint vertex_indices
end_header
1 1 1 254 0 8 255
1 1 -1 52 1 254 255
1 -1 1 0 0 0 255
1 -1 -1 19 250 254 255
-1 1 1 239 254 0 255
-1 1 -1 121 0 254 255
-1 -1 1 34 254 0 255
-1 -1 -1 254 174 52 255
3 4 2 0
3 2 7 3
3 6 5 7
3 1 7 5
3 0 3 1
3 4 1 5
3 4 6 2
3 2 6 7
3 6 4 5
3 1 3 7
3 0 2 3
3 4 0 1
*/
public class PLY_ModelLoader implements ModelLoader {
    final String[] PARAMETERS = new String[]{"ply", "format ascii", "element vertex", "property float x", "property float y", "property float z",
            "property uchar red", "property uchar green", "property uchar blue", "property uchar alpha", "element face", "end_header"};
    String path;
    Vertex[] vertices;
    short[] TriangleTriplets;

    public PLY_ModelLoader() {};

    public PLY_ModelLoader(String relativePath) {
        setPath(relativePath);
    }

    @Override
    public void setPath(String relativePath) {
        this.path = relativePath;
    }

    @Override
    public Vertex[] getVertices() {
        return vertices;
    }
    @Override
    public short[] getTriangleTriplets() {
        return TriangleTriplets;
    }
    @Override
    public void read () throws IOException, UnsupportedOperationException {
        BufferedReader buffer = new BufferedReader(new FileReader(path));
        String line;

        // Find the parameters of this file
        //   Parameter Name, value of the parameter
        //              |       |
        LinkedHashMap<String, Float> foundedParameters = new LinkedHashMap<>();
        while ((line = buffer.readLine()) != null) {
            addParameter(line, foundedParameters);

            if (foundedParameters.containsKey("end_header"))
                break;
        }

        // Check if the parameters are compatible with this class
        for (String parameterName : PARAMETERS) {
            if (!foundedParameters.containsKey(parameterName))
                throw new UnsupportedOperationException("File not compatible, '" + parameterName + "' parameter not found");
        }


        vertices = readVertices(buffer, foundedParameters);
        TriangleTriplets = readTriangleTriplets(buffer, foundedParameters);
    }
    /**
     * If it found a parameter it adds it inside foundedParameters
     *
     * @param lineToSearch      the line to search
     * @param foundedParameters the map of parameters founded
     */
    private void addParameter(String lineToSearch, LinkedHashMap<String, Float> foundedParameters) {
        for (String parameterName : PARAMETERS) {
            if (lineToSearch.startsWith(parameterName)) {
                String rest = lineToSearch.substring(parameterName.length()).trim();
                float value = rest.isEmpty() ? 0 : Float.parseFloat(rest);
                foundedParameters.put(parameterName, value);
                break;
            }
        }
    }
    /**
     * Reads the vertices from the file buffer.
     *
     * @param buffer the buffer positioned after the header
     * @param foundedParameters the parameters found in the header
     * @return array of vertices read from the file
     * @throws IOException if the file size does not match the declared vertex count
     */
    private Vertex[] readVertices(BufferedReader buffer, LinkedHashMap<String, Float> foundedParameters) throws IOException {
        int verticesNum = foundedParameters.get("element vertex").intValue();
        Vertex[] result = new Vertex[verticesNum];

        // Filtra solo le proprietà che iniziano con "property"
        // Ovvero le proprietà che descrivono i campi di un vertice
        List<String> vertexPropertiesOrder = new ArrayList<>();
        for (String key : foundedParameters.keySet()) {
            if (key.startsWith("property"))
                vertexPropertiesOrder.add(key);
        }

        for (int i = 0; i < verticesNum; i++) {
            String line = buffer.readLine();
            if (line == null)
                throw new IOException("Vertices number does not match the declared size");


            result[i] = parseVertex(line, vertexPropertiesOrder);
        }
        return result;
    }
    /**
     * Parses a single vertex from a line of the PLY file.
     *
     * @param line the line of a vertex
     * @param propertiesOrder the order of the vertex properties as declared in the header
     * @return the parsed vertex
     */
    private Vertex parseVertex(String line, List<String> propertiesOrder) {
        String[] vertexParts = line.trim().split(" ");

        float x = 0, y = 0, z = 0;
        int red = 0, green = 0, blue = 0, alpha = 255;

        for (int j = 0; j < propertiesOrder.size(); j++) {
            switch (propertiesOrder.get(j)) {
                case "property float x"    -> x     = Float.parseFloat(vertexParts[j]);
                case "property float y"    -> y     = Float.parseFloat(vertexParts[j]);
                case "property float z"    -> z     = Float.parseFloat(vertexParts[j]);
                case "property uchar red"  -> red   = Integer.parseInt(vertexParts[j]);
                case "property uchar green"-> green = Integer.parseInt(vertexParts[j]);
                case "property uchar blue" -> blue  = Integer.parseInt(vertexParts[j]);
                case "property uchar alpha"-> alpha = Integer.parseInt(vertexParts[j]);
            }
        }
        return new GenericVertex(x, y, z, new Color(red, green, blue, alpha));
    }
    /**
     * Reads the Faces(triplet of vertices indices) from the file buffer.
     *
     * @param buffer the buffer positioned after the vertices
     * @param foundedParameters the parameters found in the header
     * @return array of TriangleTriplets read from the file
     * @throws IOException if the file size does not match the declared Faces count
     */
    private short[] readTriangleTriplets(BufferedReader buffer, LinkedHashMap<String, Float> foundedParameters) throws IOException, UnsupportedOperationException {
        int facesNum = foundedParameters.get("element face").intValue();
        short[] result = new short[facesNum*3];
        int resultSize = 0;

        for (int i = 0; i < facesNum; i++) {
            String line = buffer.readLine();
            if (line == null)
                throw new IOException("Faces number does not match the declared size");

            String[] faceParts = line.trim().split(" ");
            if (!faceParts[0].equals("3"))
                throw new UnsupportedOperationException("Almeno una faccia del modello non è un triangolo");


            result[resultSize++] = (short) Integer.parseInt(faceParts[1]);
            result[resultSize++] = (short) Integer.parseInt(faceParts[2]);
            result[resultSize++] = (short) Integer.parseInt(faceParts[3]);
        }
        return result;
    }
}