package it.unicam.cs.mpgc.rpg130398.GameLogic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.InterrogatoryScene.TrustRequirement;
import it.unicam.cs.mpgc.rpg130398.api.ConnectionRequirement;
import it.unicam.cs.mpgc.rpg130398.api.DialogLoader;
import it.unicam.cs.mpgc.rpg130398.api.DialogNode;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public class JSON_DialogLoader implements DialogLoader {

    private String relativePath;
    private ArrayList<DialogNode> nodes;

    // Registro tipo -> classe concreta. Aggiungere qui una riga per ogni
    // nuova implementazione di ConnectionRequirement.
    // E necessario memorizzare un identificativo di che implementazione di TrustRequirement è memorizzata.
    // Perche le diverse implementazioni di TrustRequirement hanno attributi diversi, con nomi diversi da memorizzare
    private static final Map<String, Class<? extends ConnectionRequirement>> REQUIREMENT_TYPES = Map.of(
            "trust", TrustRequirement.class
    );

    public JSON_DialogLoader() {}

    public JSON_DialogLoader(String relativePath) {
        setPath(relativePath);
    }

    public static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(ConnectionRequirement.class, new RequirementTypeAdapter())
                .create();
    }

    @Override
    public void setPath(String relativePath) {
        this.relativePath = relativePath;
    }

    @Override
    public void read() throws java.io.IOException, UnsupportedOperationException {
        nodes = new ArrayList<>();
        String fileContent = Files.readString(Path.of(relativePath));

        Gson gson = createGson();

        JsonArray nodeJsonArray = JsonParser.parseString(fileContent).getAsJsonArray();
        for (JsonElement nodeJson : nodeJsonArray) {
            DialogNode currentNode = gson.fromJson(nodeJson, GenericDialogNode.class);
            nodes.add(currentNode);
        }
    }

    @Override
    public ArrayList<DialogNode> getNodes() {
        return nodes;
    }

    /**
     * (De)serializes {@link ConnectionRequirement} using the stable "type"
     * value returned by {@link ConnectionRequirement#getType()} as the JSON
     * discriminant, mapped to a concrete class via {@link #REQUIREMENT_TYPES}.
     * <p>
     * Adding a new ConnectionRequirement kind requires one entry in
     * REQUIREMENT_TYPES; renaming or moving the implementing class requires
     * no change at all, since the type string is independent of the class name.
     */
    private static class RequirementTypeAdapter
            implements JsonSerializer<ConnectionRequirement>, JsonDeserializer<ConnectionRequirement> {

        private static final String TYPE_FIELD = "type";

        @Override
        public JsonElement serialize(ConnectionRequirement src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = context.serialize(src, src.getClass()).getAsJsonObject();
            jsonObject.addProperty(TYPE_FIELD, src.getType());
            return jsonObject;
        }

        @Override
        public ConnectionRequirement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            if (!jsonObject.has(TYPE_FIELD))
                throw new JsonParseException("ConnectionRequirement JSON object is missing the \"" + TYPE_FIELD + "\" field: " + json);

            String type = jsonObject.get(TYPE_FIELD).getAsString();
            Class<? extends ConnectionRequirement> concreteClass = REQUIREMENT_TYPES.get(type);
            if (concreteClass == null)
                throw new JsonParseException("Unknown requirement type: " + type);

            return context.deserialize(json, concreteClass);
        }
    }

    /**
     * Prints an example of a correct json to standard output.
     * It is not formated
     **/
    private void printExampleFile() {
        Gson gson = JSON_DialogLoader.createGson();

        ArrayList<ConnectionRequirement> requirements = new ArrayList<>();
        requirements.add(new TrustRequirement(0, 5));

        DialogNode.Connection[] connections = {
                new DialogNode.Connection(1, "Vai al nodoID1, devi avere fiducia tra 0 e 5", requirements, 1),
                new DialogNode.Connection(2, "Vai al nodoID2", null, -1)
        };

        ArrayList<DialogNode> nodes = new ArrayList<>();
        nodes.add(new GenericDialogNode(0, "Testo nodo id0", "Flag nodo id0", connections));
        nodes.add(new GenericDialogNode(1, "Testo nodo id1", "Flag nodo id1", null));
        nodes.add(new GenericDialogNode(2, "Testo nodo id2", null, null));

        System.out.println(gson.toJson(nodes));
    }
}