package it.unicam.cs.mpgc.rpg130398.GameLogic;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import it.unicam.cs.mpgc.rpg130398.api.DialogTreeLoader;
import it.unicam.cs.mpgc.rpg130398.api.DialogNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class JSON_DialogTreeLoader implements DialogTreeLoader {
    String relativePath;

    Map<Integer, DialogNode> Nodes;

    public JSON_DialogTreeLoader() {}
    public JSON_DialogTreeLoader(String relativePath) {
        setPath(relativePath);
    }

    @Override
    public void setPath(String relativePath) {
        this.relativePath = relativePath;
    }

    @Override
    public void read() throws IOException, UnsupportedOperationException {
        Nodes = new HashMap<Integer, DialogNode>();

        String contenutoFile = Files.readString(Path.of(relativePath));

        Gson gson = new Gson();
        JsonArray NodeJsonArray = JsonParser.parseString(contenutoFile).getAsJsonArray();
        for (JsonElement NodeJson : NodeJsonArray) {
            DialogNode CurrentNode = gson.fromJson(NodeJson, SimpleDialogNode.class);

            Nodes.put(CurrentNode.getId(), CurrentNode);
        }
    }

    @Override
    public Map<Integer, DialogNode> getNodes() {
        return Nodes;// Nodes;
    }
}
