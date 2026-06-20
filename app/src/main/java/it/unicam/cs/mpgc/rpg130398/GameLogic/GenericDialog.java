package it.unicam.cs.mpgc.rpg130398.GameLogic;

import it.unicam.cs.mpgc.rpg130398.api.Dialog;
import it.unicam.cs.mpgc.rpg130398.api.DialogNode;
import it.unicam.cs.mpgc.rpg130398.api.DialogLoader;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.*;

public class GenericDialog implements Dialog {
    //      id node     node
    HashMap<Integer, DialogNode> Nodes;
    Set<Integer> visitedNodes;    // id of already visited nodes

    DialogNode CurrentNode;
    int thrust;
    HashSet<String> unlockedFlags;

    public GenericDialog(ArrayList<DialogNode> Nodes) {
        setup(Nodes);
    }
    public GenericDialog(@NonNull DialogLoader Loader) {
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
        unlockedFlags = new HashSet<>();
        visitedNodes = new HashSet<>();
    }

    @Override
    public ArrayList<DialogNode.Connection> getValidChoices() {
        ArrayList<DialogNode.Connection> result = new ArrayList<>();

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

        CurrentNode = Nodes.get(connection.idOther());
        thrust += connection.TrustDelta();
        if (CurrentNode.getFlag() != null && !CurrentNode.getFlag().isEmpty())
            unlockedFlags.add(CurrentNode.getFlag());

        visitedNodes.add(CurrentNode.getId());
        CurrentNode.markVisited();
        return true;
    }

    @Override
    public int getTrust() {
        return thrust;
    }

    @Override
    public Set<String> getOpinionatedFlags() {
        return unlockedFlags;
    }
    private boolean isA_ValidChoices(DialogNode.Connection connection) {
        if (!Nodes.containsKey(connection.idOther()))
            return false;
        if (thrust < connection.minRequiredTrust())
            return false;
        if (thrust > connection.maxRequiredTrust())
            return false;
        if (visitedNodes.contains(connection.idOther()))
            return false;
        return true;
    }
}
