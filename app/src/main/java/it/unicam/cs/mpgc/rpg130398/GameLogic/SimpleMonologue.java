package it.unicam.cs.mpgc.rpg130398.GameLogic;

import it.unicam.cs.mpgc.rpg130398.GameLogic.Interface.Monologue;
import it.unicam.cs.mpgc.rpg130398.api.RendableText;

public class SimpleMonologue implements Monologue {
    private final String[] pages;
    private final RendableText textObject;
    private final int charsPerFrame;
    private final int pauseFrames;

    private int currentPage = 0;
    private int charIndex = 0;
    private int pauseCounter = 0;
    private boolean pageFinished = false;

    public SimpleMonologue(String[] pages, RendableText textObject, int charsPerFrame, int pauseFrames) {
        this.pages = pages;
        this.textObject = textObject;
        this.charsPerFrame = charsPerFrame;
        this.pauseFrames = pauseFrames;
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
        if (hasFinished() || pageFinished) return;

        String current = pages[currentPage];
        if (charIndex < current.length()) {
            charIndex = Math.min(charIndex + charsPerFrame, current.length());
            textObject.setText(current.substring(0, charIndex));
        } else {
            pauseCounter++;
            if (pauseCounter >= pauseFrames)
                pageFinished = true;
        }
    }

    @Override
    public boolean hasFinishedDisplaying() {
        return pageFinished;
    }

    @Override
    public boolean hasFinished() {
        return currentPage >= pages.length;
    }

    @Override
    public String getCurrent() {
        if (hasFinished()) return null;
        return pages[currentPage].substring(0, charIndex);
    }
}