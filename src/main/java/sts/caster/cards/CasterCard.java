package sts.caster.cards;

import basemod.abstracts.CustomCard;

public abstract class CasterCard extends CustomCard {

    public int delayTurns;        // Just like magic number, or any number for that matter, we want our regular, modifiable stat
    public int baseDelayTurns;    // And our base stat - the number in it's base state. It will reset to that by default.
    public boolean upgradedDelayTurns; // A boolean to check whether the number has been upgraded or not.
    public boolean isDelayTurnsModified; // A boolean to check whether the number has been modified or not, for coloring purposes. (red/green)

    public CasterCard(final String id, final String name, final String img, final int cost, final String rawDescription,
                               final CardType type, final CardColor color,
                               final CardRarity rarity, final CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);

        delayTurns = baseDelayTurns = 0;
        isCostModified = false;
        isCostModifiedForTurn = false;
        isDamageModified = false;
        isBlockModified = false;
        isMagicNumberModified = false;
        isDelayTurnsModified = false;
    }

    public void displayUpgrades() { // Display the upgrade - when you click a card to upgrade it

        if (upgradedDelayTurns) { // If we set upgradedDefaultSecondMagicNumber = true in our card.
            delayTurns = baseDelayTurns; // Show how the number changes, as out of combat, the base number of a card is shown.
            isDelayTurnsModified = true; // Modified = true, color it green to highlight that the number is being changed.
        }

    }

    public void upgradeDelayTurns(int amount) { // If we're upgrading (read: changing) the number. Note "upgrade" and NOT "upgraded" - 2 different things. One is a boolean, and then this one is what you will usually use - change the integer by how much you want to upgrade.
        baseDelayTurns += amount; // Upgrade the number by the amount you provide in your card.
        delayTurns = baseDelayTurns; // Set the number to be equal to the base value.
        upgradedDelayTurns = true; // Upgraded = true - which does what the above method does.
    }
}