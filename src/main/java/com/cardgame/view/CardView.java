package com.cardgame.view;

import com.cardgame.model.Card;
import com.cardgame.model.PaperCard;
import com.cardgame.model.ScissorsCard;
import com.cardgame.model.StoneCard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Visual representation of a card in the game.
 */
public class CardView extends VBox {
    private final Card card;
    private final int index;
    private final Label nameLabel;
    private final Label typeLabel;
    private final Label lifeLabel;
    private final Label defenceLabel;
    private final Label attackLabel;
    private final Label statusLabel;

    private static final double CARD_WIDTH = 120;
    private static final double CARD_HEIGHT = 180;

    /**
     * Creates a new card view for the specified card.
     * @param card The card to visualize
     * @param index The index of the card in the player's hand or deck
     */
    public CardView(Card card, int index) {
        this.card = card;
        this.index = index;

        // Set up the card view
        setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        setAlignment(Pos.CENTER);
        setSpacing(5);
        setPadding(new Insets(10));
        getStyleClass().add("card-view");

        // Set background color based on card type
        setCardBackground();

        // Create labels for card information
        nameLabel = new Label(card.getName());
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        nameLabel.setWrapText(true);
        nameLabel.setAlignment(Pos.CENTER);

        typeLabel = new Label(card.getType());
        typeLabel.setFont(Font.font("System", 10));

        lifeLabel = new Label("Life: " + card.getCurrentLife() + "/" + card.getMaxLife());
        defenceLabel = new Label("Defence: " + card.getCurrentDefence() + "/" + card.getMaxDefence());
        attackLabel = new Label("Attack: " + card.getAttack());

        statusLabel = new Label();
        statusLabel.setFont(Font.font("System", FontWeight.BOLD, 10));
        statusLabel.setTextFill(Color.RED);

        // Add labels to the card view
        getChildren().addAll(nameLabel, typeLabel, lifeLabel, defenceLabel, attackLabel, statusLabel);

        // Update the card view
        updateCardView();
    }

    /**
     * Updates the card view to reflect the current state of the card.
     */
    public void updateCardView() {
        // Update labels
        lifeLabel.setText("Life: " + card.getCurrentLife() + "/" + card.getMaxLife());
        defenceLabel.setText("Defence: " + card.getCurrentDefence() + "/" + card.getMaxDefence());
        attackLabel.setText("Attack: " + card.getAttack());

        // Update status
        if (card.isDefeated()) {
            statusLabel.setText("DEFEATED");
            setDisable(true);
            setOpacity(0.7);
        } else {
            StringBuilder status = new StringBuilder();

            if (card.isAttackMuted()) {
                status.append("Attack Muted");
            }

            if (card.isDefenceMuted()) {
                if (status.length() > 0) {
                    status.append(", ");
                }
                status.append("Defence Muted");
            }

            statusLabel.setText(status.toString());
        }
    }

    /**
     * Sets the background color of the card based on its type.
     */
    private void setCardBackground() {
        Color bgColor;

        if (card instanceof StoneCard) {
            bgColor = Color.rgb(150, 150, 150, 0.7); // Gray for Stone
        } else if (card instanceof PaperCard) {
            bgColor = Color.rgb(255, 255, 200, 0.7); // Light yellow for Paper
        } else if (card instanceof ScissorsCard) {
            bgColor = Color.rgb(200, 200, 255, 0.7); // Light blue for Scissors
        } else {
            bgColor = Color.rgb(240, 240, 240, 0.7); // Default light gray
        }

        setBackground(new Background(new BackgroundFill(bgColor, new CornerRadii(10), Insets.EMPTY)));
    }

    /**
     * Sets whether this card is selected.
     * @param selected true if the card is selected, false otherwise
     */
    public void setSelected(boolean selected) {
        if (selected) {
            setStyle("-fx-border-color: gold; -fx-border-width: 3; -fx-border-radius: 10;");
        } else {
            setStyle("");
        }
    }

    /**
     * Gets the card represented by this view.
     * @return The card
     */
    public Card getCard() {
        return card;
    }

    /**
     * Gets the index of the card.
     * @return The index
     */
    public int getIndex() {
        return index;
    }
}
