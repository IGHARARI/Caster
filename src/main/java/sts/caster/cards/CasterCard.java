package sts.caster.cards;

import basemod.abstracts.CustomCard;

public abstract class CasterCard extends CustomCard {

    public int delayTurns;        
    public int baseDelayTurns;    
    public boolean upgradedDelayTurns; 
    public boolean isDelayTurnsModified; 

    public int spellBlock;        
    public int baseSpellBlock;    
    public boolean upgradedSpellBlock; 
    public boolean isSpellBlockModified; 
    
    public int spellDamage;        
    public int baseSpellDamage;    
    public boolean upgradedSpellDamage; 
    public boolean isSpellDamageModified; 

    public CasterCard(final String id, final String name, final String img, final int cost, final String rawDescription,
                               final CardType type, final CardColor color,
                               final CardRarity rarity, final CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);

        delayTurns = baseDelayTurns = 0;
        spellBlock = baseSpellBlock = 0;
        spellDamage = baseSpellDamage = 0;
        isCostModified = false;
        isCostModifiedForTurn = false;
        isDamageModified = false;
        isBlockModified = false;
        isMagicNumberModified = false;
        isDelayTurnsModified = false;
        isSpellDamageModified = false;
        isSpellBlockModified = false;
    }

    public void displayUpgrades() {
    	super.displayUpgrades();
        if (upgradedDelayTurns) {
            delayTurns = baseDelayTurns;
            isDelayTurnsModified = true;
        }
        if (upgradedSpellBlock) {
        	spellBlock = baseSpellBlock;
        	isSpellBlockModified = true;
        }
        if (upgradedSpellDamage) {
        	spellDamage = baseSpellDamage;
        	isSpellDamageModified = true;
        }
    }

    @Override
    public void resetAttributes() {
    	super.resetAttributes();
    	delayTurns = baseDelayTurns;
    	isDelayTurnsModified = false;
    	spellDamage = baseSpellDamage; 
    	isSpellDamageModified = false;
    	spellBlock = baseSpellBlock; 
    	isSpellBlockModified = false; 
    }
    
    public void upgradeDelayTurns(int amount) { 
        baseDelayTurns += amount; 
        delayTurns = baseDelayTurns;
        upgradedDelayTurns = true;
    }
    
    public void upgradeSpellDamage(int amount) { 
    	baseSpellDamage += amount; 
    	spellDamage = baseSpellDamage; 
    	upgradedSpellDamage = true; 
    }
    
    public void upgradeSpellBlock(int amount) { 
    	baseSpellBlock += amount; 
    	spellBlock = baseSpellBlock; 
    	upgradedSpellBlock = true; 
    }
    
    public void onStartOfTurnDelayEffect() {}
}