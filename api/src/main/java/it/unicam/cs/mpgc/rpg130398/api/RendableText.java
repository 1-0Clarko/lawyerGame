package it.unicam.cs.mpgc.rpg130398.api;

/**
 * Represents a text object that can be rendered in the game world.
 * Has a x, y, z position but z is not used.
 * Holds the text content, optional font path, size and position.
 *
 * The dirty flag signals the graphics engine that the object has changed and needs to be re-rendered.
 */
public interface RendableText extends Positionable {

    /**
     * @return the text to display
     */
    String getText();

    /**
     * @param text the text to display
     */
    void setText(String text);

    /**
     * @return path of the font file relative to the working directory
     */
    String getFontPath();

    /**
     * @param fontPath path of the font file relative to the working directory, .ttf files are supported
     */
    void setFontPath(String fontPath);

    /**
     * @return size of the text in game units
     */
    float getSize();

    /**
     * @param size size of the text in game units.
     * Not necessarily consistent with the FRUSTUM, but doubling the size will double the effective size.
     */
    void setSize(float size);

    /**
     * @return true if the text object has changed and needs to be re-rendered
     */
    boolean isDirty();

    /**
     * Called by the graphics engine after reading isDirty.
     */
    void clearDirty();
}