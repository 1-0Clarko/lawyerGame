package it.unicam.cs.mpgc.rpg130398.GameLogic;

import it.unicam.cs.mpgc.rpg130398.api.DialogNode;

import java.util.Arrays;

public class GenericDialogNode implements DialogNode {
    private final int id;                 // Identificatore univoco del nodo di dialogo.
    private final String text;            // Testo mostrato al giocatore quando il nodo viene raggiunto.
    private final String flag;            // Flag opzionale sbloccata raggiungendo questo nodo.
                                          // Utilizzata nella fase processuale per registrare
                                          // informazioni importanti ottenute dal cliente.

    private final int reputationGain;   // Modifica della fiducia/reputazione applicata
                                        // quando il nodo viene visitato.
                                        // Valori positivi aumentano la fiducia,
                                        // valori negativi la diminuiscono.
    private final Connection[] connections;    // Possibili collegamenti verso altri nodi.
                                               // Se contiene un solo elemento, il dialogo
                                               // prosegue automaticamente.

    private transient boolean visited = false; // visited does not need to be loaded from the file

    public GenericDialogNode(int id, String text, String flag, int reputationGain, Connection[] connections) {
        this.id = id;
        this.text = text;
        this.flag = flag;
        this.connections = connections != null ? connections : new Connection[0];
        this.reputationGain = reputationGain;
    }

    @Override public int getId() { return id; }
    @Override public String getText() { return text; }
    @Override public Connection[] getConnection() { return connections; }
    @Override public String getFlag() { return flag; }
    @Override public boolean isLeaf() { return connections.length == 0; }
    @Override public void markVisited() { visited = true; }
    @Override public boolean isVisited() { return visited; }
    @Override public int getReputationGain() { return reputationGain; }

    @Override
    public String toString() {
        return "SimpleDialogNode{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", flag='" + flag + '\'' +
                ", conections=" + Arrays.toString(connections) +
                ", visited=" + visited +
                '}';
    }
}