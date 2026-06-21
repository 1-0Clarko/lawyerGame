package it.unicam.cs.mpgc.rpg130398.GameLogic;

import it.unicam.cs.mpgc.rpg130398.api.*;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.*;

public class GenericDialog implements Dialog, DialogState {

    private final HashMap<Integer, DialogNode> nodes = new HashMap<>();   // id node -> node
    private final Set<Integer> visitedNodes = new HashSet<>();           // id of already visited nodes
    private final HashSet<String> unlockedFlags = new HashSet<>();
    private DialogNode currentNode;
    private int trust;

    public GenericDialog(ArrayList<DialogNode> nodes) {
        setup(nodes);
    }

    public GenericDialog(@NonNull DialogLoader loader) {
        try {
            loader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setup(loader.getNodes());
    }

    private void setup(@NonNull ArrayList<DialogNode> nodeList) {
        nodes.clear();
        for (DialogNode node : nodeList)
            nodes.put(node.getId(), node);
        currentNode = nodes.get(0); // The Start of the conversation is the node with id=0
        unlockedFlags.clear();
        visitedNodes.clear();
    }

    @Override
    public ArrayList<DialogNode.Connection> getValidChoices() {
        ArrayList<DialogNode.Connection> result = new ArrayList<>();
        for (DialogNode.Connection connection : currentNode.getConnection())
            if (isAValidChoice(connection))
                result.add(connection);
        return result;
    }

    @Override
    public DialogNode getCurrentNode() {
        return currentNode;
    }

    @Override
    public boolean makeChoices(DialogNode.Connection connection) {
        if (!getValidChoices().contains(connection))
            return false;

        currentNode = nodes.get(connection.idOther());
        trust += connection.trustDelta();

        if (currentNode.getFlag() != null && !currentNode.getFlag().isEmpty())
            unlockedFlags.add(currentNode.getFlag());

        visitedNodes.add(currentNode.getId());
        currentNode.markVisited();
        return true;
    }

    @Override
    public int getTrust() {
        return trust;
    }

    @Override
    public Set<String> getOpinionatedFlags() {
        return unlockedFlags;
    }

    // --- DialogState: vista in sola lettura esposta ai Requirement ---

    @Override
    public Set<String> getCollectedFlags() {
        return unlockedFlags;
    }

    @Override
    public Set<Integer> getVisitedNodes() {
        return visitedNodes;
    }

    /**
     * A connection is a valid choice when it targets an existing node, has not
     * already been used (engine rule: no repeating the same connection, which
     * also prevents loops), and all of its {@link ConnectionRequirement}
     * are satisfied by the current state.
     */
    private boolean isAValidChoice(DialogNode.Connection connection) {
        if (!nodes.containsKey(connection.idOther()))
            return false;
        if (visitedNodes.contains(connection.idOther()))
            return false;
        return connection.isSatisfiedBy(this);
    }
}