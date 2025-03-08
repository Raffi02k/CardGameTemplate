package com.cardgame.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Represents the deck of cards in the game.
 */
public class Deck {
    private final List<Card> cards = new ArrayList<>();
    private final ObservableList<Card> visibleCards = FXCollections.observableArrayList();
    private static final int VISIBLE_CARDS_COUNT = 5;
    private static final Random random = new Random();

    /**
     * Creates a new deck with the specified number of each card type.
     * @param stoneCount Number of stone cards
     * @param paperCount Number of paper cards
     * @param scissorsCount Number of scissors cards
     */
    public Deck(int stoneCount, int paperCount, int scissorsCount) {
        // Add stone cards
        for (int i = 0; i < stoneCount; i++) {
            cards.add(new StoneCard());
        }

        // Add paper cards
        for (int i = 0; i < paperCount; i++) {
            cards.add(new PaperCard());
        }

        // Add scissors cards
        for (int i = 0; i < scissorsCount; i++) {
            cards.add(new ScissorsCard());
        }

        // Shuffle the deck
        shuffle();

        // Initialize visible cards
        refreshVisibleCards();
    }

    /**
     * Shuffles the deck.
     */
    public void shuffle() {
        Collections.shuffle(cards, random);
    }

    /**
     * Draws a specified number of cards from the deck.
     * @param count Number of cards to draw
     * @return List of drawn cards
     */
    public List<Card> drawCards(int count) {
        List<Card> drawnCards = new ArrayList<>();

        for (int i = 0; i < count && !cards.isEmpty(); i++) {
            drawnCards.add(cards.remove(0));
        }

        // Refresh visible cards after drawing
        refreshVisibleCards();

        return drawnCards;
    }

    /**
     * Draws a single card from the deck.
     * @return The drawn card, or null if the deck is empty
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            return null;
        }

        Card card = cards.remove(0);
        refreshVisibleCards();
        return card;
    }

    /**
     * Removes a specific card from the visible cards and from the deck.
     * @param cardToRemove The card to remove
     * @return true if the card was found and removed, false otherwise
     */
    public boolean removeVisibleCard(Card cardToRemove) {
        boolean removed = visibleCards.remove(cardToRemove);

        // Also remove from the main deck if it's there
        cards.remove(cardToRemove);

        return removed;
    }

    /**
     * Forcibly refreshes the visible cards from the top of the deck.
     */
    public void refreshVisibleCards() {
        visibleCards.clear();

        for (int i = 0; i < VISIBLE_CARDS_COUNT && i < cards.size(); i++) {
            visibleCards.add(cards.get(i));
        }
    }

    /**
     * Gets the current visible cards from the top of the deck.
     * @return Observable list of visible cards
     */
    public ObservableList<Card> getVisibleCards() {
        return visibleCards;
    }

    /**
     * Gets the number of cards remaining in the deck.
     * @return Number of remaining cards
     */
    public int getRemainingCards() {
        return cards.size();
    }

    /**
     * Checks if the deck is empty.
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }
}

