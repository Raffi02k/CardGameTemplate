package com.cardgame.controller;

import com.cardgame.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the main menu screen.
 */
public class MainMenuController {
    @FXML
    private TextField player1NameField;

    @FXML
    private TextField player2NameField;

    @FXML
    private Button startGameButton;

    @FXML
    private Button rulesButton;

    @FXML
    private Button exitButton;

    /**
     * Starts a new game with the entered player names.
     */
    @FXML
    private void startGame(ActionEvent event) {
        String player1Name = player1NameField.getText().trim();
        String player2Name = player2NameField.getText().trim();

        // Validate player names
        if (player1Name.isEmpty() || player2Name.isEmpty()) {
            showAlert("Invalid Names", "Please enter names for both players.");
            return;
        }

        try {
            // Load the game screen
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/GameScreen.fxml"));
            Parent gameScreen = loader.load();

            // Get the controller and initialize the game
            GameScreenController controller = loader.getController();
            controller.initializeGame(player1Name, player2Name);

            // Show the game screen
            Scene scene = new Scene(gameScreen);
            scene.getStylesheets().add(Main.class.getResource("/css/styles.css").toExternalForm());

            Stage stage = (Stage) startGameButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Rock-Paper-Scissors Card Battle");
            stage.setResizable(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to start the game: " + e.getMessage());
        }
    }

    /**
     * Shows the game rules.
     */
    @FXML
    private void showRules(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Rules");
        alert.setHeaderText("Rock-Paper-Scissors Card Battle Rules");

        String rules = "GAME RULES:\n\n" +
                "- Each player starts with 5 random cards.\n" +
                "- The first five cards from the deck are showing.\n" +
                "- For each round, a player can either:\n" +
                "  1. Attack an opponent's card with one of their cards (once per round).\n" +
                "  2. Pick up cards from the deck to switch with their table-cards (1-5 cards).\n\n" +
                "- When a card is attacked, its defence is reduced first. When defence reaches 0,\n" +
                "  the card's life is reduced.\n" +
                "- When a card is killed (life reaches 0), that slot is locked and can't be used anymore.\n" +
                "- The goal is to kill all of your opponent's 5 cards.\n\n" +
                "CARD TYPES:\n\n" +
                "Stone:\n" +
                "- Life: 2, Defence: 10, Attack: 2\n\n" +
                "Paper:\n" +
                "- Life: 10, Defence: 1, Attack: 2\n" +
                "- Special: Mutes either defence or attack on the attacked card until after\n" +
                "  the player has made another turn.\n\n" +
                "Scissors:\n" +
                "- Life: 5, Defence: 3, Attack: 3\n" +
                "- Special: Causes damage over time. First attack does max damage, and subsequent attacks (up to 3 total) do random damage from 1 to max attack.\n";

        alert.setContentText(rules);
        alert.showAndWait();
    }

    /**
     * Exits the application.
     */
    @FXML
    private void exitGame(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
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
