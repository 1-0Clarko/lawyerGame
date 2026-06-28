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
import it.unicam.cs.mpgc.rpg130398.api.dialog.ConnectionRequirement;
import it.unicam.cs.mpgc.rpg130398.api.dialog.Dialog;
import it.unicam.cs.mpgc.rpg130398.api.dialog.DialogLoader;
import it.unicam.cs.mpgc.rpg130398.api.dialog.DialogNode;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads a DialogNode array from a json file
 *
 */
public class JSON_DialogLoader implements DialogLoader {

    private String relativePath;
    private ArrayList<DialogNode> nodes;
    private Class<? extends Dialog> dialogClass;
    private Class<? extends DialogNode> nodeClass;
    // Map:  (abbreviate name used in the data -> corresponding Class)
    // for example: ("trust", TrustRequirementClass)
    private Map<String, Class<? extends ConnectionRequirement>> connectionRequirementClasses;

    // Fields used in the json Header (first element of the array).
    // used to obtain the concrete classes that the dialog is using
    private static final String DIALOG_CLASS_FIELD = "DialogClass"; // class used for the dialog
    private static final String NODE_CLASS_FIELD = "NodeClass"; // class used for the node
    private static final String CONNECTION_REQUIREMENT_FIELD = "ConnectionRequirmentClass";
    private static final String CONNECTION_REQUIREMENT_TYPE_FIELD = "type";
    private static final String CONNECTION_REQUIREMENT_CLASS_FIELD = "className";

    public JSON_DialogLoader() {}

    public JSON_DialogLoader(String relativePath) {
        setPath(relativePath);
    }

    @Override
    public void setPath(String relativePath) {
        this.relativePath = relativePath;
    }

    @Override
    public void read() throws java.io.IOException, UnsupportedOperationException {
        nodes = new ArrayList<>();
        String fileContent = Files.readString(Path.of(relativePath));
        JsonArray jsonArray = JsonParser.parseString(fileContent).getAsJsonArray();

        readClassHeader(jsonArray.get(0).getAsJsonObject());

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ConnectionRequirement.class, new RequirementTypeAdapter(connectionRequirementClasses))
                .create();

        for (int i = 1; i < jsonArray.size(); i++) {
            DialogNode currentNode = gson.fromJson(jsonArray.get(i), nodeClass);
            nodes.add(currentNode);
        }
    }

    @Override
    public Class<? extends Dialog> getDialogClass() {
        return dialogClass;
    }

    @Override
    public ArrayList<DialogNode> getNodes() {
        return nodes;
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<? extends T> resolveClass(String className, Class<T> expectedClass) {
        Class<?> resolvedClass;
        try {
            resolvedClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown class " + className, e);
        }
        if (!expectedClass.isAssignableFrom(resolvedClass))
            throw new JsonParseException("Class " + className + " is not a type of " + expectedClass.getName());
        return (Class<? extends T>) resolvedClass;
    }
    private void readClassHeader(JsonObject header) {
        dialogClass = resolveClass(header.get(DIALOG_CLASS_FIELD).getAsString(), Dialog.class);
        nodeClass = resolveClass(header.get(NODE_CLASS_FIELD).getAsString(), DialogNode.class);
        connectionRequirementClasses = readConnectionRequirementMap(header.getAsJsonArray(CONNECTION_REQUIREMENT_FIELD));
    }

    private static Map<String, Class<? extends ConnectionRequirement>> readConnectionRequirementMap(JsonArray ConnectionRequirementAssociations) {
        Map<String, Class<? extends ConnectionRequirement>> result = new HashMap<>();
        for (JsonElement Association : ConnectionRequirementAssociations) {
            JsonObject AssociationObject = Association.getAsJsonObject();
            String type = AssociationObject.get(CONNECTION_REQUIREMENT_TYPE_FIELD).getAsString();
            String className = AssociationObject.get(CONNECTION_REQUIREMENT_CLASS_FIELD).getAsString();
            result.put(type, resolveClass(className, ConnectionRequirement.class));
        }
        return result;
    }
    /**
     * (De)serializes {@link ConnectionRequirement} using the "type" value present on
     * each requirement object, mapped to a concrete class via a type -> class table
     * read from the file's header ("ConnectionRequirmentClass").
     */
    private static class RequirementTypeAdapter
            implements JsonSerializer<ConnectionRequirement>, JsonDeserializer<ConnectionRequirement> {

        private final Map<String, Class<? extends ConnectionRequirement>> requirementTypes;

        private RequirementTypeAdapter(Map<String, Class<? extends ConnectionRequirement>> requirementTypes) {
            this.requirementTypes = requirementTypes;
        }

        @Override
        public JsonElement serialize(ConnectionRequirement src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = context.serialize(src, src.getClass()).getAsJsonObject();
            jsonObject.addProperty(CONNECTION_REQUIREMENT_TYPE_FIELD, src.getType());
            return jsonObject;
        }

        @Override
        public ConnectionRequirement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            if (!jsonObject.has(CONNECTION_REQUIREMENT_TYPE_FIELD))
                throw new JsonParseException("ConnectionRequirement JSON object is missing the \"" + CONNECTION_REQUIREMENT_TYPE_FIELD + "\" field: " + json);

            String type = jsonObject.get(CONNECTION_REQUIREMENT_TYPE_FIELD).getAsString();
            Class<? extends ConnectionRequirement> concreteClass = requirementTypes.get(type);
            if (concreteClass == null)
                throw new JsonParseException("Unknown requirement type: " + type);

            return context.deserialize(json, concreteClass);
        }
    }


    /**
     * Writes a dialog to file in the JSON_DialogLoader format: the first array element
     * is a header declaring the concrete classes used.
     * The rest of the elements are DialogNodes and DialogNodes.Connections.
     *
     * @param path path of the file to write, relative to the working directory
     * @param dialogClass the concrete Dialog implementation this file should be loaded with
     * @param nodeClass the concrete DialogNode implementation used by the nodes
     * @param connectionRequirementClasses the type -> class mapping for ConnectionRequirement declarations
     * @param nodes the dialog nodes to write
     * @throws java.io.IOException if the file cannot be written
     */
    public static void write(String path,
                             Class<? extends Dialog> dialogClass,
                             Class<? extends DialogNode> nodeClass,
                             Map<String, Class<? extends ConnectionRequirement>> connectionRequirementClasses,
                             ArrayList<DialogNode> nodes) throws java.io.IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ConnectionRequirement.class, new RequirementTypeAdapter(connectionRequirementClasses))
                .create();

        JsonArray jsonArray = new JsonArray();

        JsonObject header = new JsonObject();
        header.addProperty(DIALOG_CLASS_FIELD, dialogClass.getName());
        header.addProperty(NODE_CLASS_FIELD, nodeClass.getName());
        JsonArray requirementDeclarations = new JsonArray();
        for (Map.Entry<String, Class<? extends ConnectionRequirement>> entry : connectionRequirementClasses.entrySet()) {
            JsonObject declaration = new JsonObject();
            declaration.addProperty(CONNECTION_REQUIREMENT_TYPE_FIELD, entry.getKey());
            declaration.addProperty(CONNECTION_REQUIREMENT_CLASS_FIELD, entry.getValue().getName());
            requirementDeclarations.add(declaration);
        }
        header.add(CONNECTION_REQUIREMENT_FIELD, requirementDeclarations);
        jsonArray.add(header);

        for (DialogNode node : nodes)
            jsonArray.add(gson.toJsonTree(node, nodeClass));

        Files.writeString(Path.of(path), gson.toJson(jsonArray));
    }
}