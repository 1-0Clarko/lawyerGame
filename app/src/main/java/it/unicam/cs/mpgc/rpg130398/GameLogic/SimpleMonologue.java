package it.unicam.cs.mpgc.rpg130398.GameLogic;

import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Monologue;
import it.unicam.cs.mpgc.rpg130398.api.RendableText;

public class SimpleMonologue implements Monologue {
    private final String[] pages;
    private final RendableText textObject;
    private final float charsPerFrame;
    private final int endPagesFrames;

    private int currentPage = -1;
    private float charIndex = 0;
    private int pauseCounter = 0;
    private boolean pageFinished = false;
    private boolean hasFinished = false;

    public SimpleMonologue(String[] pages, RendableText textObject, float charsPerFrame, int endPagesFrames) {
        this.pages = pages;
        this.textObject = textObject;
        this.charsPerFrame = charsPerFrame;
        this.endPagesFrames = endPagesFrames;
        textObject.setText("");
    }

    @Override
    public void showNext() {
        if (hasFinished()) return;
        currentPage++;
        charIndex = 0;
        pauseCounter = 0;
        pageFinished = false;
        textObject.setText("");
    }

    @Override
    public void update() {
        if (hasFinished || pageFinished) return;

        String current = pages[currentPage];
        if (charIndex < current.length()) {
            charIndex = Math.min(charIndex + charsPerFrame, current.length());
            textObject.setText(getCurrent());
        } else {
            pauseCounter++;
            if (pauseCounter >= endPagesFrames) {
                pageFinished = true;

                if (currentPage == pages.length-1)
                    hasFinished = true;
            }
        }
    }

    @Override
    public boolean hasFinishedDisplaying() {
        return pageFinished;
    }

    @Override
    public boolean hasFinished() {
        return hasFinished;
    }

    @Override
    public String getCurrent() {
        if (hasFinished) return null;
        return pages[currentPage].substring(0, (int)charIndex);
    }
}