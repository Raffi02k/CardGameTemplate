package com.cardgame.model;

import java.util.Random;

/**
 * Represents a Scissors card in the game.
 * Scissors cards deal damage over time with up to 3 iterations.
 */
public class ScissorsCard extends Card {
    private static final Random random = new Random();
    private static final int MAX_ITERATIONS = 3;

    public ScissorsCard() {
        super("Regular Scissors", "Scissors", 5, 3, 3);
    }

    @Override
    public String attack(Card target) {
        if (isAttackMuted()) {
            return getName() + " is muted and cannot attack!";
        }

        StringBuilder result = new StringBuilder();

        // First attack is always max damage
        int damage = getAttack();
        result.append(performAttack(target, damage));

        // Additional damage iterations (up to 2 more)
        for (int i = 1; i < MAX_ITERATIONS && !target.isDefeated(); i++) {
            // Random damage from 1 to max attack for subsequent iterations
            damage = random.nextInt(getAttack()) + 1;
            result.append("\n\nContinuing attack: ");
            result.append(performAttack(target, damage));
        }

        return result.toString();
    }
}
