package it.unicam.cs.mpgc.rpg130398.GameLogic.GameScenes.Helper;

import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Monologue;
import it.unicam.cs.mpgc.rpg130398.api.RendableText;

public class MonologueAnimation implements Monologue {
    private final String[] pages;
    private final RendableText textObject;
    private final float charsPerFrame;
    private final int endPagesWait;
    private final int endSentenceWait;

    private int currentPage = -1;
    private float charIndex = 0;
    private int timeToWait = 0;
    private boolean showNext;

    public MonologueAnimation(String[] pages, RendableText textObject, float charsPerFrame, int endPagesWait, int endSentenceWait) {
        this.pages = pages;
        this.textObject = textObject;
        this.charsPerFrame = charsPerFrame;
        this.endPagesWait = endPagesWait;
        this.endSentenceWait = endSentenceWait;
        textObject.setText("");
        showNext();
    }

    @Override
    public void showNext() {
        if (hasFinished()) return;
        currentPage++;
        if (!hasFinished()) {
            charIndex = 0;
            timeToWait = 0;
            textObject.setText("");
        }
    }
    private void clear() {
        charIndex = 0;
        timeToWait = 0;
        textObject.setText("");
    }

    @Override
    public void update() {
        if (timeToWait != 0) {
            timeToWait--;
            return;
        }
        if (showNext) {
            showNext();
            showNext = false;
        }
        if (hasFinished()) return;

        int currentPageSize = pages[currentPage].length();
        charIndex = Math.min(charIndex + charsPerFrame, currentPageSize);
        textObject.setText(getCurrent());

        controlPageEnd();
        controlSentenceEnd();
    }
    private void controlPageEnd() {
        if (hasCurrentItemFinished()) {
            timeToWait = endPagesWait;
            showNext = true;
        }
    }
    private void controlSentenceEnd() {
        if (getCurrent().endsWith("\n") || getCurrent().endsWith("?"))
            timeToWait = endSentenceWait;
    }

    @Override
    public boolean hasCurrentItemFinished() {
        if (hasFinished())
            return true;

        return charIndex == pages[currentPage].length();
    }

    @Override
    public boolean hasFinished() {
        return currentPage == pages.length;
    }

    @Override
    public String getCurrent() {
        if (hasFinished())
            return null;

        return pages[currentPage].substring(0, (int)charIndex);
    }
}