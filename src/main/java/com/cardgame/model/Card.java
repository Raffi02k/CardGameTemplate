package com.cardgame.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Base class for all cards in the game.
 */
public abstract class Card {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty type = new SimpleStringProperty();
    private final IntegerProperty maxLife = new SimpleIntegerProperty();
    private final IntegerProperty currentLife = new SimpleIntegerProperty();
    private final IntegerProperty maxDefence = new SimpleIntegerProperty();
    private final IntegerProperty currentDefence = new SimpleIntegerProperty();
    private final IntegerProperty attack = new SimpleIntegerProperty();
    private boolean isMuted = false;
    private boolean isAttackMuted = false;
    private boolean isDefenceMuted = false;

    public Card(String name, String type, int life, int defence, int attack) {
        this.name.set(name);
        this.type.set(type);
        this.maxLife.set(life);
        this.currentLife.set(life);
        this.maxDefence.set(defence);
        this.currentDefence.set(defence);
        this.attack.set(attack);
    }

    /**
     * Performs an attack on the target card.
     * @param target The card being attacked
     * @return A string describing the attack result
     */
    public String attack(Card target) {
        if (isAttackMuted()) {
            return getName() + " is muted and cannot attack!";
        }

        int attackValue = getAttack();
        return performAttack(target, attackValue);
    }

    /**
     * Applies damage to the target card.
     * @param target The card being attacked
     * @param damage The amount of damage to apply
     * @return A string describing the attack result
     */
    protected String performAttack(Card target, int damage) {
        StringBuilder result = new StringBuilder();
        result.append(getName()).append(" attacks ").append(target.getName()).append(" for ").append(damage).append(" damage!");

        // Apply damage to defence first
        if (target.getCurrentDefence() > 0) {
            int defenceBefore = target.getCurrentDefence();
            target.setCurrentDefence(Math.max(0, target.getCurrentDefence() - damage));
            int defenceDamage = defenceBefore - target.getCurrentDefence();
            damage -= defenceDamage;

            result.append("\n").append(target.getName()).append("'s defence reduced by ").append(defenceDamage)
                    .append(" (").append(target.getCurrentDefence()).append(" remaining)");
        }

        // If there's remaining damage, apply to life
        if (damage > 0 && !target.isDefenceMuted()) {
            int lifeBefore = target.getCurrentLife();
            target.setCurrentLife(Math.max(0, target.getCurrentLife() - damage));
            int lifeDamage = lifeBefore - target.getCurrentLife();

            result.append("\n").append(target.getName()).append("'s life reduced by ").append(lifeDamage)
                    .append(" (").append(target.getCurrentLife()).append(" remaining)");

            if (target.getCurrentLife() <= 0) {
                result.append("\n").append(target.getName()).append(" has been defeated!");
            }
        }

        return result.toString();
    }

    /**
     * Resets any temporary effects on the card at the end of a turn.
     */
    public void endTurn() {
        // To be overridden by subclasses if needed
    }

    // Getters and setters with JavaFX property support
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public int getMaxLife() {
        return maxLife.get();
    }

    public IntegerProperty maxLifeProperty() {
        return maxLife;
    }

    public int getCurrentLife() {
        return currentLife.get();
    }

    public void setCurrentLife(int life) {
        this.currentLife.set(life);
    }

    public IntegerProperty currentLifeProperty() {
        return currentLife;
    }

    public int getMaxDefence() {
        return maxDefence.get();
    }

    public IntegerProperty maxDefenceProperty() {
        return maxDefence;
    }

    public int getCurrentDefence() {
        return currentDefence.get();
    }

    public void setCurrentDefence(int defence) {
        this.currentDefence.set(defence);
    }

    public IntegerProperty currentDefenceProperty() {
        return currentDefence;
    }

    public int getAttack() {
        return attack.get();
    }

    public void setAttack(int attack) {
        this.attack.set(attack);
    }

    public IntegerProperty attackProperty() {
        return attack;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean muted) {
        this.isMuted = muted;
    }

    public boolean isAttackMuted() {
        return isAttackMuted;
    }

    public void setAttackMuted(boolean attackMuted) {
        this.isAttackMuted = attackMuted;
    }

    public boolean isDefenceMuted() {
        return isDefenceMuted;
    }

    public void setDefenceMuted(boolean defenceMuted) {
        this.isDefenceMuted = defenceMuted;
    }

    public boolean isDefeated() {
        return currentLife.get() <= 0;
    }

    @Override
    public String toString() {
        return getName() + " (" + getType() + ") - Life: " + getCurrentLife() + "/" + getMaxLife() +
                ", Defence: " + getCurrentDefence() + "/" + getMaxDefence() + ", Attack: " + getAttack();
    }
}
