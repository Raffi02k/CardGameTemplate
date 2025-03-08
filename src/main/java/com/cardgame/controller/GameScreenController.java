package com.cardgame.controller;

import com.cardgame.Main;
import com.cardgame.model.Card;
import com.cardgame.model.Game;
import com.cardgame.model.Player;
import com.cardgame.view.CardView;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the game screen.
 */
public class GameScreenController {
    private boolean debugMode = true; // Set to false for production

    @FXML
    private Label gameStatusLabel;

    @FXML
    private Button endTurnButton;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Label player1NameLabel;

    @FXML
    private Label player2NameLabel;

    @FXML
    private HBox player1CardsContainer;

    @FXML
    private HBox player2CardsContainer;

    @FXML
    private StackPane deckContainer;

    @FXML
    private Label cardsRemainingLabel;

    @FXML
    private HBox availableCardsContainer;

    @FXML
    private Button switchCardsButton;

    @FXML
    private ScrollPane logScrollPane;

    @FXML
    private TextArea gameLogTextArea;

    private Game game;
    private CardView selectedPlayerCard;
    private CardView selectedOpponentCard;
    private final List<CardView> selectedPlayerCards = new ArrayList<>();
    private final List<CardView> selectedDeckCards = new ArrayList<>();
    private boolean switchModeActive = false;
    private boolean hasSwitchedThisTurn = false;

    /**
     * Logs debug information if debug mode is enabled.
     */
    private void debugLog(String message) {
        if (debugMode) {
            appendToGameLog("[DEBUG] " + message);
        }
    }

    /**
     * Initializes the game with the specified player names.
     */
    public void initializeGame(String player1Name, String player2Name) {
        // Create a new game
        game = new Game(player1Name, player2Name);

        // Set up UI bindings
        gameStatusLabel.textProperty().bind(game.gameStatusProperty());
        player1NameLabel.setText(player1Name);
        player2NameLabel.setText(player2Name);

        // Set up card displays
        setupPlayerCards();
        setupDeckDisplay();

        // Add initial log entry
        appendToGameLog("Game started! " + player1Name + " vs " + player2Name);
        appendToGameLog(player1Name + "'s turn");
        appendToGameLog("To switch cards: First click on your cards to select them, then click the 'Switch Selected Cards' button");

        // Set up switch button
        switchCardsButton.setDisable(true);
        switchCardsButton.setOnAction(this::switchCards);

        if (debugMode) {
            debugLog("Player 1 has " + game.getPlayer1().getCards().size() + " cards");
            debugLog("Player 2 has " + game.getPlayer2().getCards().size() + " cards");
            debugLog("Deck has " + game.getDeck().getRemainingCards() + " cards remaining");
            debugLog("Game initialized in debug mode - extra logging enabled");
        }
    }

    /**
     * Sets up the player card displays.
     */
    private void setupPlayerCards() {
        // Clear existing cards
        player1CardsContainer.getChildren().clear();
        player2CardsContainer.getChildren().clear();

        // Set up player 1's cards
        for (int i = 0; i < game.getPlayer1().getCards().size(); i++) {
            Card card = game.getPlayer1().getCards().get(i);
            CardView cardView = new CardView(card, i);

            // Add click handler for player 1's cards
            final int cardIndex = i;
            cardView.setOnMouseClicked(event -> {
                if (!card.isDefeated()) {
                    debugLog("Clicked player 1 card at index " + cardIndex);
                    handlePlayerCardClick(cardView, cardIndex);
                }
            });

            player1CardsContainer.getChildren().add(cardView);
        }

        // Set up player 2's cards
        for (int i = 0; i < game.getPlayer2().getCards().size(); i++) {
            Card card = game.getPlayer2().getCards().get(i);
            CardView cardView = new CardView(card, i);

            // Add click handler for player 2's cards
            final int cardIndex = i;
            cardView.setOnMouseClicked(event -> {
                if (!card.isDefeated()) {
                    handlePlayerCardClick(cardView, cardIndex);
                }
            });

            player2CardsContainer.getChildren().add(cardView);
        }

        // Listen for changes in card properties
        game.getPlayer1().getCards().forEach(card -> {
            card.currentLifeProperty().addListener((obs, oldVal, newVal) -> updateCardViews());
            card.currentDefenceProperty().addListener((obs, oldVal, newVal) -> updateCardViews());
        });

        game.getPlayer2().getCards().forEach(card -> {
            card.currentLifeProperty().addListener((obs, oldVal, newVal) -> updateCardViews());
            card.currentDefenceProperty().addListener((obs, oldVal, newVal) -> updateCardViews());
        });
    }

    /**
     * Sets up the deck display and available cards.
     */
    private void setupDeckDisplay() {
        // Clear existing content
        deckContainer.getChildren().clear();

        // Set up deck visualization
        StackPane deckPane = new StackPane();
        deckPane.getStyleClass().add("deck");
        deckContainer.getChildren().add(deckPane);

        // Update cards remaining label
        cardsRemainingLabel.setText("Cards: " + game.getDeck().getRemainingCards());

        // Set up available cards display
        updateAvailableCards();

        // Listen for changes in the deck
        game.getDeck().getVisibleCards().addListener((ListChangeListener<Card>) change -> {
            updateAvailableCards();
        });
    }

    /**
     * Updates the available cards display.
     */
    private void updateAvailableCards() {
        availableCardsContainer.getChildren().clear();

        for (int i = 0; i < game.getDeck().getVisibleCards().size(); i++) {
            Card card = game.getDeck().getVisibleCards().get(i);
            CardView cardView = new CardView(card, i);

            // Add click handler for available cards
            final int cardIndex = i;  // Create a final copy of i
            cardView.setOnMouseClicked(event -> {
                debugLog("Clicked available card at index " + cardIndex);
                handleAvailableCardClick(cardView);
            });

            availableCardsContainer.getChildren().add(cardView);
        }

        // Update cards remaining label
        cardsRemainingLabel.setText("Cards: " + game.getDeck().getRemainingCards());
    }

    /**
     * Updates all card views to reflect the current game state.
     */
    private void updateCardViews() {
        // Update player 1's card views
        for (Node node : player1CardsContainer.getChildren()) {
            if (node instanceof CardView) {
                ((CardView) node).updateCardView();
            }
        }

        // Update player 2's card views
        for (Node node : player2CardsContainer.getChildren()) {
            if (node instanceof CardView) {
                ((CardView) node).updateCardView();
            }
        }
    }

    /**
     * Handles a click on a player's card.
     */
    private void handlePlayerCardClick(CardView cardView, int cardIndex) {
        Player currentPlayer = game.getCurrentPlayer();
        Player opponent = game.getOpponent();
        Card clickedCard = cardView.getCard();

        // Debug information
        debugLog("Card clicked: " + clickedCard.getName() + " (index: " + cardIndex + ")");

        // Check if the card belongs to the current player
        boolean isCurrentPlayerCard = (currentPlayer == game.getPlayer1() && player1CardsContainer.getChildren().contains(cardView)) ||
                (currentPlayer == game.getPlayer2() && player2CardsContainer.getChildren().contains(cardView));

        // Check if the card belongs to the opponent
        boolean isOpponentCard = (currentPlayer == game.getPlayer1() && player2CardsContainer.getChildren().contains(cardView)) ||
                (currentPlayer == game.getPlayer2() && player1CardsContainer.getChildren().contains(cardView));

        debugLog("Is current player's card: " + isCurrentPlayerCard);
        debugLog("Is opponent's card: " + isOpponentCard);

        // If the card belongs to the current player
        if (isCurrentPlayerCard) {
            // If player has already attacked this turn
            if (currentPlayer.hasAttackedThisTurn()) {
                appendToGameLog("You have already attacked this turn and cannot select cards.");
                return;
            }

            // If we're in switch mode or cards are selected for switching
            if (switchModeActive || !selectedPlayerCards.isEmpty()) {
                togglePlayerCardForSwitch(cardView);
            } else if (selectedPlayerCard == null) {
                // Select this card for attack
                selectedPlayerCard = cardView;
                selectedPlayerCard.setSelected(true);
                appendToGameLog("Selected " + cardView.getCard().getName() + " for attack. Now select an opponent's card.");
            } else {
                // Deselect previous card and select this one
                selectedPlayerCard.setSelected(false);
                selectedPlayerCard = cardView;
                selectedPlayerCard.setSelected(true);
                appendToGameLog("Selected " + cardView.getCard().getName() + " for attack. Now select an opponent's card.");
            }
        }
        // If the card belongs to the opponent
        else if (isOpponentCard) {
            // If we have a selected player card, perform attack
            if (selectedPlayerCard != null) {
                selectedOpponentCard = cardView;

                // Perform the attack
                String attackResult = game.performAttack(selectedPlayerCard.getIndex(), cardIndex);
                appendToGameLog(attackResult);

                // Reset selections
                selectedPlayerCard.setSelected(false);
                selectedPlayerCard = null;
                selectedOpponentCard = null;

                // Update UI
                updateCardViews();
            } else {
                appendToGameLog("Select one of your cards first to attack with.");
            }
        }
    }

    /**
     * Handles a click on an available card.
     */
    private void handleAvailableCardClick(CardView cardView) {
        if (game.getCurrentPlayer().hasAttackedThisTurn() || hasSwitchedThisTurn) {
            appendToGameLog("You cannot switch cards after attacking or having already switched this turn.");
            return;
        }

        if (switchModeActive) {
            toggleDeckCardForSwitch(cardView);
        } else {
            appendToGameLog("Click 'Switch Mode' button first to activate card switching.");
        }
    }

    /**
     * Toggles a deck card for switching.
     */
    private void toggleDeckCardForSwitch(CardView cardView) {
        if (selectedDeckCards.contains(cardView)) {
            // Deselect the card
            selectedDeckCards.remove(cardView);
            cardView.setSelected(false);
            debugLog("Deselected deck card: " + cardView.getCard().getName());
        } else {
            // Select the card
            selectedDeckCards.add(cardView);
            cardView.setSelected(true);
            debugLog("Selected deck card: " + cardView.getCard().getName());
        }

        // Update switch button status
        updateSwitchButtonStatus();
    }

    /**
     * Toggles a player card for switching with deck cards.
     */
    private void togglePlayerCardForSwitch(CardView cardView) {
        if (selectedPlayerCards.contains(cardView)) {
            // Deselect the card
            selectedPlayerCards.remove(cardView);
            cardView.setSelected(false);
            debugLog("Deselected player card for switching: " + cardView.getCard().getName());
        } else {
            // Select the card
            selectedPlayerCards.add(cardView);
            cardView.setSelected(true);
            debugLog("Selected player card for switching: " + cardView.getCard().getName());
        }

        // Update switch button status
        updateSwitchButtonStatus();
    }

    /**
     * Updates the switch button status.
     */
    private void updateSwitchButtonStatus() {
        // Enable switch button only if same number of player and deck cards are selected
        boolean canSwitch = !selectedPlayerCards.isEmpty() &&
                (selectedPlayerCards.size() == selectedDeckCards.size());
        switchCardsButton.setDisable(!canSwitch);

        if (canSwitch) {
            appendToGameLog("Ready to switch " + selectedPlayerCards.size() + " card(s). Click 'Switch Selected Cards' to confirm.");
        }
    }

    /**
     * Activates switch mode to select cards for switching.
     */
    @FXML
    private void activateSwitchMode() {
        if (game.getCurrentPlayer().hasAttackedThisTurn()) {
            appendToGameLog("You cannot switch cards after attacking.");
            return;
        }

        if (hasSwitchedThisTurn) {
            appendToGameLog("You can only switch cards once per turn.");
            return;
        }

        switchModeActive = true;

        // Clear any attack selection
        if (selectedPlayerCard != null) {
            selectedPlayerCard.setSelected(false);
            selectedPlayerCard = null;
        }

        appendToGameLog("Switch mode activated. First select the cards from your hand you want to replace, " +
                "then select an equal number of cards from the available deck cards. " +
                "Finally, click 'Switch Selected Cards' to complete the exchange.");
    }

    /**
     * Switches the selected cards with cards from the deck.
     */
    @FXML
    private void switchCards(ActionEvent event) {
        if (selectedPlayerCards.isEmpty() || selectedDeckCards.isEmpty()) {
            appendToGameLog("Please select equal numbers of your cards and deck cards to switch.");
            return;
        }

        if (selectedPlayerCards.size() != selectedDeckCards.size()) {
            appendToGameLog("Please select equal numbers of your cards and deck cards to switch.");
            return;
        }

        if (hasSwitchedThisTurn) {
            appendToGameLog("You have already switched cards this turn.");
            return;
        }

        debugLog("Switch button clicked with " + selectedPlayerCards.size() + " player cards and " +
                selectedDeckCards.size() + " deck cards selected");

        // Create mappings of player card indices to deck card indices
        List<Integer> playerCardIndices = new ArrayList<>();
        List<Card> deckCardsToUse = new ArrayList<>();

        for (CardView playerCardView : selectedPlayerCards) {
            playerCardIndices.add(playerCardView.getIndex());
        }

        for (CardView deckCardView : selectedDeckCards) {
            deckCardsToUse.add(deckCardView.getCard());
        }

        // Perform the switch with custom cards
        String switchResult = game.switchCardsWithChosen(playerCardIndices, deckCardsToUse);
        appendToGameLog(switchResult);

        // Reset selections
        for (CardView cardView : new ArrayList<>(selectedPlayerCards)) {
            cardView.setSelected(false);
        }
        for (CardView cardView : new ArrayList<>(selectedDeckCards)) {
            cardView.setSelected(false);
        }

        selectedPlayerCards.clear();
        selectedDeckCards.clear();
        switchCardsButton.setDisable(true);
        switchModeActive = false;
        hasSwitchedThisTurn = true;

        // Update UI
        setupPlayerCards(); // Rebuild player cards to ensure correct indices
        updateCardViews();
        updateAvailableCards();
    }

    /**
     * Ends the current player's turn.
     */
    @FXML
    private void endTurn(ActionEvent event) {
        // Reset selections
        if (selectedPlayerCard != null) {
            selectedPlayerCard.setSelected(false);
            selectedPlayerCard = null;
        }

        for (CardView cardView : new ArrayList<>(selectedPlayerCards)) {
            cardView.setSelected(false);
        }
        for (CardView cardView : new ArrayList<>(selectedDeckCards)) {
            cardView.setSelected(false);
        }

        selectedPlayerCards.clear();
        selectedDeckCards.clear();
        switchCardsButton.setDisable(true);
        switchModeActive = false;

        // End the turn
        game.endTurn();

        // Reset switch status for new turn
        hasSwitchedThisTurn = false;

        // Update UI
        updateCardViews();
        updateAvailableCards();

        appendToGameLog(game.getCurrentPlayer().getName() + "'s turn");
    }

    /**
     * Returns to the main menu.
     */
    @FXML
    private void returnToMainMenu(ActionEvent event) {
        try {
            // Load the main menu
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/MainMenu.fxml"));
            Parent mainMenu = loader.load();

            // Show the main menu
            Scene scene = new Scene(mainMenu);
            scene.getStylesheets().add(Main.class.getResource("/css/styles.css").toExternalForm());

            Stage stage = (Stage) mainMenuButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Rock-Paper-Scissors Card Battle");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to return to main menu: " + e.getMessage());
        }
    }

    /**
     * Appends a message to the game log.
     */
    private void appendToGameLog(String message) {
        gameLogTextArea.appendText(message + "\n\n");

        // Auto-scroll to bottom
        Platform.runLater(() -> {
            gameLogTextArea.setScrollTop(Double.MAX_VALUE);
        });
    }

    /**
     * Shows an alert dialog with the specified title and message.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

