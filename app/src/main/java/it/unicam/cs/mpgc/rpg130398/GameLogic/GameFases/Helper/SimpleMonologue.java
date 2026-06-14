package it.unicam.cs.mpgc.rpg130398.GameLogic.GameFases.Helper;

import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Monologue;
import it.unicam.cs.mpgc.rpg130398.api.RendableText;

public class SimpleMonologue implements Monologue {
    private final String[] pages;
    private final RendableText textObject;
    private final float charsPerFrame;
    private final int endPagesWait;

    private int currentPage = -1;
    private float charIndex = 0;
    private int pauseCounter = 0;
    private boolean pageFinished = false;
    private boolean hasFinished = false;

    public SimpleMonologue(String[] pages, RendableText textObject, float charsPerFrame, int endPagesWait) {
        this.pages = pages;
        this.textObject = textObject;
        this.charsPerFrame = charsPerFrame;
        this.endPagesWait = endPagesWait;
        textObject.setText("");
        showNext();
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
        if (hasFinished) return;
        if (pageFinished) {
            showNext();
            return;
        }

        String current = pages[currentPage];
        if (charIndex < current.length()) {
            charIndex = Math.min(charIndex + charsPerFrame, current.length());
            textObject.setText(getCurrent());
        } else {
            pauseCounter++;
            if (pauseCounter >= endPagesWait) {
                pageFinished = true;

                if (currentPage == pages.length-1)
                    hasFinished = true;
            }
        }
    }

    @Override
    public boolean hasCurrentItemFinished() {
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