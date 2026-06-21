package it.unicam.cs.mpgc.rpg130398.GameLogic;

import it.unicam.cs.mpgc.rpg130398.api.DialogNode;

import java.util.Arrays;

public class GenericDialogNode implements DialogNode {

    private final int id;                       // Identificatore univoco del nodo di dialogo.
    private final String text;                  // Testo mostrato al giocatore quando il nodo viene raggiunto.
    private final String flag;                  // Flag opzionale, è impostato solo nei nodi importanti, puo essere null.
    private final Connection[] connections;      // Collegamenti verso altri nodi.
    // Se contiene un solo elemento, il dialogo
    // prosegue automaticamente.
    private transient boolean visited = false;   // se è stato visitato

    public GenericDialogNode(int id, String text, String flag, Connection[] connections) {
        this.id = id;
        this.text = text;
        this.flag = flag;
        this.connections = connections != null ? connections : new Connection[0];
    }

    @Override public int getId() { return id; }
    @Override public String getText() { return text; }
    @Override public Connection[] getConnection() { return connections; }
    @Override public String getFlag() { return flag; }
    @Override public boolean isLeaf() { return connections.length == 0; }
    @Override public void markVisited() { visited = true; }
    @Override public boolean isVisited() { return visited; }

    @Override
    public String toString() {
        return "GenericDialogNode{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", flag='" + flag + '\'' +
                ", connections=" + Arrays.toString(connections) +
                ", visited=" + visited +
                '}';
    }
}