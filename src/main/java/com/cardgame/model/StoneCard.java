package com.cardgame.model;

/**
 * Represents a Stone card in the game.
 * Stone cards have high defense but low attack and life.
 */
public class StoneCard extends Card {

    public StoneCard() {
        super("Regular Stone", "Stone", 2, 10, 2);
    }

    // Stone cards have no special abilities in the base version
}
