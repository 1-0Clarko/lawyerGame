package it.unicam.cs.mpgc.rpg130398.GameLogic;

import it.unicam.cs.mpgc.rpg130398.api.Dialog;
import it.unicam.cs.mpgc.rpg130398.api.DialogNode;
import it.unicam.cs.mpgc.rpg130398.api.DialogTreeLoader;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.*;

public class GenericDialog implements Dialog {
    //      id node     node
    HashMap<Integer, DialogNode> Nodes;

    DialogNode CurrentNode;
    int thrust;
    ArrayList<String> unlockedFlags;

    public GenericDialog(ArrayList<DialogNode> Nodes) {
        setup(Nodes);
    }
    public GenericDialog(@NonNull DialogTreeLoader Loader) {
        try {
            Loader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setup(Loader.getNodes());
    }
    private void setup(@NonNull ArrayList<DialogNode> Nodes) {
        this.Nodes = new HashMap<>();
        for (DialogNode node : Nodes)
            this.Nodes.put(node.getId(), node);
        CurrentNode = this.Nodes.get(0); // The Start of the conversation is the node with id=0
    }

    @Override
    public ArrayList<DialogNode.Connection> getValidChoices() {
        ArrayList<DialogNode.Connection> result = new ArrayList<>();

        // if there is only one connection, it is considered always valid
        if (CurrentNode.getConnection().length == 1) {
            DialogNode.Connection soleConnection = CurrentNode.getConnection()[0];
            result.add(soleConnection);
            return result;
        }

        for (DialogNode.Connection connection : CurrentNode.getConnection()) {
            if (isA_ValidChoices(connection))
                result.add(connection);
        }
        return result;
    }

    @Override
    public DialogNode getCurrentNode() {
        return CurrentNode;
    }

    @Override
    public boolean makeChoices(DialogNode.Connection connection) {
        if (!getValidChoices().contains(connection))
            return false;

        CurrentNode = Nodes.get(connection.id());
        thrust += CurrentNode.getReputationGain();
        CurrentNode.markVisited();
        return true;
    }

    @Override
    public int getTrust() {
        return thrust;
    }

    @Override
    public Collection<String> getOpinionatedFlags() {
        return unlockedFlags;
    }
    private boolean isA_ValidChoices(DialogNode.Connection connection) {
        if (!Nodes.containsKey(connection.id()))
            return false;
        if (connection.requiredTrust() > thrust)
            return false;
        if (Nodes.get(connection.id()).isVisited())
            return false;
        return true;
    }
}
