package it.unicam.cs.mpgc.rpg130398.GameLogic;

import it.unicam.cs.mpgc.rpg130398.api.DialogNode;

import java.util.Arrays;

public class SimpleDialogNode implements DialogNode {
    private final int id;
    private final String text;
    private final String flag;
    private final Child[] Children;
    private transient boolean visited = false; // visited does not need to be loaded from the file

    public SimpleDialogNode(int id, String text, Child[] Children, String flag) {
        this.id = id;
        this.text = text;
        this.flag = flag;
        this.Children = Children != null ? Children : new Child[0];
    }

    @Override public int getId() { return id; }
    @Override public String getText() { return text; }
    @Override public Child[] getChildrens() { return Children; }
    @Override public String getFlag() { return flag; }
    @Override public boolean isLeaf() { return Children.length == 0; }
    @Override public void markVisited() { visited = true; }
    @Override public boolean isVisited() { return visited; }

    @Override
    public String toString() {
        return "SimpleDialogNode{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", flag='" + flag + '\'' +
                ", Children=" + Arrays.toString(Children) +
                ", visited=" + visited +
                '}';
    }
}