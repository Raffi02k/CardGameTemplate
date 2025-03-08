package com.cardgame.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages the game state and rules.
 */
public class Game {
    private final Player player1;
    private final Player player2;
    private final Deck deck;
    private final IntegerProperty currentPlayerIndex = new SimpleIntegerProperty(0);
    private final StringProperty gameStatus = new SimpleStringProperty("Game started");
    private boolean gameOver = false;

    private static final int INITIAL_HAND_SIZE = 5;
    private static final int STONE_CARDS = 10;
    private static final int PAPER_CARDS = 10;
    private static final int SCISSORS_CARDS = 10;

    /**
     * Creates a new game with two players.
     * @param player1Name Name of the first player
     * @param player2Name Name of the second player
     */
    public Game(String player1Name, String player2Name) {
        this.player1 = new Player(player1Name);
        this.player2 = new Player(player2Name);

        // Create and shuffle the deck
        this.deck = new Deck(STONE_CARDS, PAPER_CARDS, SCISSORS_CARDS);

        // Deal initial cards to players
        dealInitialCards();
    }

    /**
     * Deals the initial cards to both players.
     */
    private void dealInitialCards() {
        // Deal cards to player 1
        List<Card> player1Cards = deck.drawCards(INITIAL_HAND_SIZE);
        for (Card card : player1Cards) {
            player1.addCard(card);
        }

        // Deal cards to player 2
        List<Card> player2Cards = deck.drawCards(INITIAL_HAND_SIZE);
        for (Card card : player2Cards) {
            player2.addCard(card);
        }
    }

    /**
     * Performs an attack from one player's card to an opponent's card.
     * @param attackingCardIndex Index of the attacking card
     * @param targetCardIndex Index of the target card
     * @return Result of the attack as a string
     */
    public String performAttack(int attackingCardIndex, int targetCardIndex) {
        if (gameOver) {
            return "Game is already over!";
        }

        Player currentPlayer = getCurrentPlayer();
        Player opponent = getOpponent();

        // Check if player has already attacked this turn
        if (currentPlayer.hasAttackedThisTurn()) {
            return "You have already attacked this turn!";
        }

        // Validate card indices
        if (attackingCardIndex < 0 || attackingCardIndex >= currentPlayer.getCards().size()) {
            return "Invalid attacking card index: " + attackingCardIndex;
        }

        if (targetCardIndex < 0 || targetCardIndex >= opponent.getCards().size()) {
            return "Invalid target card index: " + targetCardIndex;
        }

        // Get the cards
        Card attackingCard = currentPlayer.getCards().get(attackingCardIndex);
        Card targetCard = opponent.getCards().get(targetCardIndex);

        // Check if attacking card is defeated
        if (attackingCard.isDefeated()) {
            return "Cannot attack with a defeated card!";
        }

        // Check if target card is already defeated
        if (targetCard.isDefeated()) {
            return "Target card is already defeated!";
        }

        // Perform the attack
        String attackResult = attackingCard.attack(targetCard);
        currentPlayer.setHasAttackedThisTurn(true);

        // Check if the game is over
        if (opponent.hasLost()) {
            gameOver = true;
            gameStatus.set(currentPlayer.getName() + " has won the game!");
        }

        return attackResult;
    }

    /**
     * Switches cards from the player's hand with cards from the deck.
     * @param cardIndices Indices of the cards to switch
     * @return Result of the switch operation as a string
     */
    public String switchCards(List<Integer> cardIndices) {
        if (gameOver) {
            return "Game is already over!";
        }

        Player currentPlayer = getCurrentPlayer();

        // Check if player has already attacked this turn
        if (currentPlayer.hasAttackedThisTurn()) {
            return "You cannot switch cards after attacking!";
        }

        // Check if there are enough cards in the deck
        if (deck.getRemainingCards() < cardIndices.size()) {
            return "Not enough cards in the deck! Only " + deck.getRemainingCards() + " cards available.";
        }

        // Sort indices in descending order to avoid index shifting issues
        List<Integer> sortedIndices = new ArrayList<>(cardIndices);
        Collections.sort(sortedIndices);
        Collections.reverse(sortedIndices);

        // Validate card indices
        for (int index : sortedIndices) {
            if (index < 0 || index >= currentPlayer.getCards().size()) {
                return "Invalid card index: " + index;
            }

            // Check if the card at this index is defeated
            if (currentPlayer.getCards().get(index).isDefeated()) {
                return "Cannot switch a defeated card at index: " + index;
            }
        }

        // Switch the cards
        StringBuilder result = new StringBuilder("Switched cards:\n");

        for (int index : sortedIndices) {
            Card newCard = deck.drawCard();
            if (newCard == null) {
                result.append("- No more cards in the deck!\n");
                break;
            }

            Card oldCard = currentPlayer.replaceCard(index, newCard);
            result.append("- Replaced ").append(oldCard.getName())
                    .append(" with ").append(newCard.getName()).append("\n");
        }

        return result.toString();
    }

    /**
     * Ends the current player's turn and switches to the other player.
     */
    public void endTurn() {
        // Switch to the other player
        currentPlayerIndex.set((currentPlayerIndex.get() + 1) % 2);

        // Reset the new current player's state for the new turn
        getCurrentPlayer().startNewTurn();

        gameStatus.set(getCurrentPlayer().getName() + "'s turn");
    }

    /**
     * Gets the current player.
     * @return The current player
     */
    public Player getCurrentPlayer() {
        return currentPlayerIndex.get() == 0 ? player1 : player2;
    }

    /**
     * Gets the opponent of the current player.
     * @return The opponent player
     */
    public Player getOpponent() {
        return currentPlayerIndex.get() == 0 ? player2 : player1;
    }

    // Getters
    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Deck getDeck() {
        return deck;
    }

    public IntegerProperty currentPlayerIndexProperty() {
        return currentPlayerIndex;
    }

    public StringProperty gameStatusProperty() {
        return gameStatus;
    }

    public String getGameStatus() {
        return gameStatus.get();
    }

    public boolean isGameOver() {
        return gameOver;
    }
}

