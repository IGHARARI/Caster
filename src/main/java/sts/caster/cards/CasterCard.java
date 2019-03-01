package sts.caster.cards;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;

import basemod.abstracts.CustomCard;
import sts.caster.core.MagicElement;
import sts.caster.powers.BlazedPower;
import sts.caster.powers.FrozenPower;
import sts.caster.powers.MiredPower;
import sts.caster.powers.ShockedPower;
import sts.caster.util.PowersHelper;

public abstract class CasterCard extends CustomCard {

    public int delayTurns;        
    public int baseDelayTurns;    
    public boolean upgradedDelayTurns; 
    public boolean isDelayTurnsModified; 
    public MagicElement cardElement; 

    public int spellBlock;        
    public int baseSpellBlock;    
    public boolean upgradedSpellBlock; 
    public boolean isSpellBlockModified; 
    
    public int spellDamage;        
    public int baseSpellDamage;    
    public boolean upgradedSpellDamage; 
    public boolean isSpellDamageModified; 
    
    public int m2;        
    public int baseM2;    
    public boolean upgradedM2; 
    public boolean isM2Modified; 

    public CasterCard(final String id, final String name, final String img, final int cost, final String rawDescription,
                               final CardType type, final CardColor color,
                               final CardRarity rarity, final CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);

        delayTurns = baseDelayTurns = 0;
        spellBlock = baseSpellBlock = 0;
        spellDamage = baseSpellDamage = 0;
        m2 = baseM2 = 0;
        isCostModified = false;
        isCostModifiedForTurn = false;
        isDamageModified = false;
        isBlockModified = false;
        isMagicNumberModified = false;
        isDelayTurnsModified = false;
        isSpellDamageModified = false;
        isSpellBlockModified = false;
        isM2Modified = false;
        cardElement = MagicElement.NEUTRAL;
    }

    @Override
    public void applyPowers() {
    	super.applyPowers();
    	isSpellDamageModified = false;
    	spellDamage = baseSpellDamage;
    	spellDamage += PowersHelper.getPlayerPowerAmount(FocusPower.POWER_ID);
    	if (spellDamage != baseSpellDamage) isSpellDamageModified = true;
    	
    	isSpellBlockModified = false;
    	spellBlock = baseSpellBlock;
    	spellBlock += PowersHelper.getPlayerPowerAmount(FocusPower.POWER_ID);
    	if (spellBlock != baseSpellBlock) isSpellBlockModified = true;
    }
    
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
    	super.calculateCardDamage(mo);
    	spellDamage = baseSpellDamage;
    	spellBlock = baseSpellBlock;
    	isSpellBlockModified = false;
    	isSpellDamageModified = false;
    	if (mo != null) {
    		float damageCalculation = baseSpellDamage;
    		damageCalculation += PowersHelper.getPlayerPowerAmount(FocusPower.POWER_ID);
    		if (damageCalculation > 0) {
    			if (isCreatureVulnerableTo(mo, cardElement)){
    				damageCalculation *= 1.5f;
    			}
    		}
    		if (damageCalculation < 0.0f) damageCalculation = 0.0f;
    		isSpellDamageModified = ((int)damageCalculation) != baseSpellDamage;
    		spellDamage = MathUtils.floor(damageCalculation);
    	}
    	
    	isSpellBlockModified = false;
    	spellBlock = baseSpellBlock;
    	spellBlock += PowersHelper.getPlayerPowerAmount(FocusPower.POWER_ID);
    	if (spellBlock != baseSpellBlock) isSpellBlockModified = true;
    }
    
//    @Override
//    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp) {
//    	if (mo != null && tmp > 0) {
//    		if (isCreatureVulnerableTo(mo, cardElement)){
//    			tmp *= 1.5f;
//    		}
//    	}
//    	return super.calculateModifiedCardDamage(player, mo, tmp);
//    }
    
    private boolean isCreatureVulnerableTo(AbstractMonster mo, MagicElement element) {
    	switch (element) {
    		case FIRE:
    			return mo.hasPower(MiredPower.POWER_ID);
    		case ICE:
    			return mo.hasPower(BlazedPower.POWER_ID);
    		case THUNDER:
    			return mo.hasPower(FrozenPower.POWER_ID);
    		case EARTH:
    			return mo.hasPower(ShockedPower.POWER_ID);
    		default:
    			return false;
    	}
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
        if (upgradedM2) {
        	m2 = baseM2;
        	isM2Modified = true;
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
    	m2 = baseM2; 
    	isM2Modified = false; 
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
    
    public void upgradeM2(int amount) { 
    	baseM2 += amount; 
    	m2 = baseM2; 
    	upgradedM2 = true; 
    }
    
    public void onStartOfTurnDelayEffect() {}
    
    public CasterCard makeStatIdenticalCopy() {
    	CasterCard copy = (CasterCard) this.makeStatEquivalentCopy();
    	copy.cardElement = cardElement;
    	copy.delayTurns = delayTurns;
    	copy.baseDelayTurns = baseDelayTurns;
    	copy.upgradedDelayTurns = upgradedDelayTurns;
    	copy.isDelayTurnsModified = isDelayTurnsModified;
    	copy.spellBlock = spellBlock;
    	copy.baseSpellBlock = baseSpellBlock;
    	copy.upgradedSpellBlock = upgradedSpellBlock;
    	copy.isSpellBlockModified = isSpellBlockModified;
    	copy.spellDamage = spellDamage;
    	copy.baseSpellDamage = baseSpellDamage;
    	copy.upgradedSpellDamage = upgradedSpellDamage;
    	copy.isSpellDamageModified = isSpellDamageModified;
    	copy.m2 = m2;
    	copy.baseM2 = baseM2;
    	copy.upgradedM2 = upgradedM2;
    	copy.isM2Modified = isM2Modified;
    	
    	copy.rawDescription = rawDescription;
    	copy.initializeDescription();
    	
		return copy;
    }
}