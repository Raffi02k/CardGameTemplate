package com.cardgame.model;

import java.util.Random;

/**
 * Represents a Paper card in the game.
 * Paper cards have high life but low defense and can mute opponent cards.
 */
public class PaperCard extends Card {
    private static final Random random = new Random();

    public PaperCard() {
        super("Regular Paper", "Paper", 10, 1, 2);
    }

    @Override
    public String attack(Card target) {
        String attackResult = super.attack(target);

        // Special ability: mute either defense or attack
        if (!isAttackMuted() && !target.isDefeated()) {
            boolean muteAttack = random.nextBoolean();
            if (muteAttack) {
                target.setAttackMuted(true);
                attackResult += "\n" + getName() + " muted " + target.getName() + "'s attack ability!";
            } else {
                target.setDefenceMuted(true);
                attackResult += "\n" + getName() + " muted " + target.getName() + "'s defense ability!";
            }
        }

        return attackResult;
    }
}
