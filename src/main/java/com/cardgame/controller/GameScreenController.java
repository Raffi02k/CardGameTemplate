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
    private final List<CardView> selectedCardsForSwitch = new ArrayList<>();
    private boolean switchModeActive = false;

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
            cardView.setOnMouseClicked(event -> {
                appendToGameLog("Available cards are shown for reference only. Select your cards and use the 'Switch Selected Cards' button to switch.");
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

            // If we're in switch mode or no card is selected yet
            if (switchModeActive || selectedCardsForSwitch.size() > 0) {
                toggleCardForSwitch(cardView);
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
     * Toggles a card for switching with deck cards.
     */
    private void toggleCardForSwitch(CardView cardView) {
        if (selectedCardsForSwitch.contains(cardView)) {
            // Deselect the card
            selectedCardsForSwitch.remove(cardView);
            cardView.setSelected(false);
            debugLog("Deselected card for switching: " + cardView.getCard().getName());
        } else {
            // Select the card
            selectedCardsForSwitch.add(cardView);
            cardView.setSelected(true);
            debugLog("Selected card for switching: " + cardView.getCard().getName());
        }

        // Enable/disable switch button based on selection
        switchCardsButton.setDisable(selectedCardsForSwitch.isEmpty());

        // Update UI feedback
        if (!selectedCardsForSwitch.isEmpty()) {
            appendToGameLog("Selected " + selectedCardsForSwitch.size() + " card(s) for switching. Click 'Switch Selected Cards' to proceed.");
        }
    }

    /**
     * Activates switch mode to select cards for switching.
     */
    @FXML
    private void activateSwitchMode() {
        switchModeActive = true;

        // Clear any attack selection
        if (selectedPlayerCard != null) {
            selectedPlayerCard.setSelected(false);
            selectedPlayerCard = null;
        }

        appendToGameLog("Switch mode activated. Select cards to switch, then click 'Switch Selected Cards'.");
    }

    /**
     * Switches the selected cards with cards from the deck.
     */
    @FXML
    private void switchCards(ActionEvent event) {
        if (selectedCardsForSwitch.isEmpty()) {
            appendToGameLog("No cards selected for switching.");
            return;
        }

        debugLog("Switch button clicked with " + selectedCardsForSwitch.size() + " cards selected");

        // Get the indices of the selected cards
        List<Integer> cardIndices = new ArrayList<>();
        for (CardView cardView : selectedCardsForSwitch) {
            cardIndices.add(cardView.getIndex());
            debugLog("Adding card index for switching: " + cardView.getIndex());
        }

        // Perform the switch
        String switchResult = game.switchCards(cardIndices);
        appendToGameLog(switchResult);

        // Reset selections
        for (CardView cardView : new ArrayList<>(selectedCardsForSwitch)) {
            cardView.setSelected(false);
        }
        selectedCardsForSwitch.clear();
        switchCardsButton.setDisable(true);
        switchModeActive = false;

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

        for (CardView cardView : new ArrayList<>(selectedCardsForSwitch)) {
            cardView.setSelected(false);
        }
        selectedCardsForSwitch.clear();
        switchCardsButton.setDisable(true);
        switchModeActive = false;

        // End the turn
        game.endTurn();

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

