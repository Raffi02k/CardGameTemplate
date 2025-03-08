package com.cardgame.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents a player in the game with their hand of cards.
 */
public class Player {
    private final StringProperty name = new SimpleStringProperty();
    private final ObservableList<Card> cards = FXCollections.observableArrayList();
    private boolean hasAttackedThisTurn = false;

    public Player(String name) {
        this.name.set(name);
    }

    /**
     * Adds a card to the player's hand.
     * @param card The card to add
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Replaces a card at the specified position.
     * @param index The position of the card to replace
     * @param newCard The new card
     * @return The old card that was replaced
     */
    public Card replaceCard(int index, Card newCard) {
        if (index < 0 || index >= cards.size()) {
            throw new IndexOutOfBoundsException("Invalid card index: " + index);
        }

        Card oldCard = cards.get(index);
        cards.set(index, newCard);
        return oldCard;
    }

    /**
     * Checks if the player has lost (all cards defeated).
     * @return true if all cards are defeated, false otherwise
     */
    public boolean hasLost() {
        for (Card card : cards) {
            if (!card.isDefeated()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Resets the player's attack status for a new turn.
     */
    public void startNewTurn() {
        hasAttackedThisTurn = false;

        // Reset any turn-based effects on cards
        for (Card card : cards) {
            card.endTurn();
        }
    }

    // Getters and setters
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public ObservableList<Card> getCards() {
        return cards;
    }

    public boolean hasAttackedThisTurn() {
        return hasAttackedThisTurn;
    }

    public void setHasAttackedThisTurn(boolean hasAttackedThisTurn) {
        this.hasAttackedThisTurn = hasAttackedThisTurn;
    }

    @Override
    public String toString() {
        return "Player: " + getName() + " with " + cards.size() + " cards";
    }
}
