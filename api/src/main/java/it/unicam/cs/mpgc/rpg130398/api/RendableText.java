package it.unicam.cs.mpgc.rpg130398.api;

public interface RendableText extends Positionable {
    String getText ();
    void setText (String text);
    String getFont ();
    void setFont (String font);
    float getSize ();
    void setSize (float size);
    /**
     * @return true se qualcosa è cambiato nell'oggetto testo ecc
     */
    boolean isDirty();
    /**
     * Chiamato dal graphics engine dopo aver letto isDirty
     */
    void clearDirty();
}
