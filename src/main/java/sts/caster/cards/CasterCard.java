package sts.caster.cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Predicate;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import basemod.abstracts.CustomCard;
import sts.caster.core.MagicElement;
import sts.caster.interfaces.ActionListMaker;
import sts.caster.powers.BlazedPower;
import sts.caster.powers.FrozenPower;
import sts.caster.powers.MiredPower;
import sts.caster.powers.ShockedPower;
import sts.caster.util.PowersHelper;

public abstract class CasterCard extends CustomCard {
    private static HashSet<String> ineffectivePowers = new HashSet<String>(Arrays.asList(StrengthPower.POWER_ID, DexterityPower.POWER_ID, WeakPower.POWER_ID, VulnerablePower.POWER_ID));
    
    public static final Predicate<AbstractCard> isCardSpellPredicate = (c)-> c.hasTag(CasterCardTags.SPELL);
    
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
    
    private static final String BASE_BG_PATH = "caster/images/card_backgrounds/";
    
    protected void setCardElement(MagicElement element) {
    	cardElement = element;
    	String path = BASE_BG_PATH;
    	switch (type) {
		case ATTACK:
			path += "attack/";
			break;
		case POWER:
			path += "power/";
			break;
		case SKILL:
			path += "skill/";
			break;
		default:
			return;
    	}
    	
    	switch (cardElement) {
		case EARTH:
			setBackgroundTexture(path + "earth_bg_s.png", path + "earth_bg_b.png");
			break;
		case FIRE:
			setBackgroundTexture(path + "fire_bg_s.png", path + "fire_bg_b.png");
			break;
		case ICE:
			setBackgroundTexture(path + "ice_bg_s.png", path + "ice_bg_b.png");
			break;
		case THUNDER:
			setBackgroundTexture(path + "lightning_bg_s.png", path + "lightning_bg_b.png");
			break;
		case DARK:
			break;
		case LIGHT:
			break;
		case NEUTRAL:
			break;
		default:
			break;
    	}
    }
    
    //Return an empty list by default to prevent NPEs on cards accidentally not overriding this.
    public ActionListMaker getActionsMaker(Integer energySpent) {return (c,t)-> {return new ArrayList<AbstractGameAction>();};}
    

    @Override
    public void applyPowers() {
    	if (this.hasTag(CasterCardTags.SPELL)) {
    		calculateCardDamage(null);
    	} else {
    		super.applyPowers();
    	}
    }
    
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
    	if (this.hasTag(CasterCardTags.SPELL)) {
    		resetCardSpellDamage();
    		resetCardSpellBlock();
    		applyCardDamageModifers(mo);
    		applyCardBlockModifiers(mo);
    	} else {
    		super.calculateCardDamage(mo);
    	}
    }

	private void resetCardSpellBlock() {
    	spellDamage = baseSpellDamage;
    	isSpellDamageModified = false;
	}

	private void resetCardSpellDamage() {
		spellBlock = baseSpellBlock;
		isSpellBlockModified = false;
	}

	private void applyCardDamageModifers(AbstractMonster mo) {
    	float damageCalculation = baseSpellDamage;
    	
    	damageCalculation = customApplyPlayerPowersToSpellDamage(damageCalculation);
    	if (mo != null && target != CardTarget.ALL_ENEMY && target != CardTarget.ALL) {
    		damageCalculation = customApplyEnemyPowersToSpellDamage(damageCalculation, mo);

			if (isCreatureVulnerableTo(mo, cardElement)){
				damageCalculation *= 1.5f;
			}
    		if (damageCalculation < 0.0f) damageCalculation = 0.0f;
    	}
    	isSpellDamageModified = ((int)damageCalculation) != baseSpellDamage;
    	spellDamage = MathUtils.floor(damageCalculation);
	}
	
	private void applyCardBlockModifiers(AbstractMonster mo) {
		float blockCalculation = baseSpellBlock;
		
		blockCalculation = customApplyPlayerPowersToSpellBlock(blockCalculation);
		if (blockCalculation < 0.0f) blockCalculation = 0.0f;
		if (blockCalculation != baseSpellBlock) isSpellBlockModified = true;
		spellBlock = MathUtils.floor(blockCalculation);
	}

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
    
    @Override
    public AbstractCard makeCopy() {
    	AbstractCard copy = super.makeCopy();
    	if (copy instanceof CasterCard) {
    		CasterCard ccCopy = (CasterCard) copy;
    		ccCopy.cardElement = cardElement;
    		ccCopy.delayTurns = delayTurns;
    		ccCopy.baseDelayTurns = baseDelayTurns;
    		ccCopy.upgradedDelayTurns = upgradedDelayTurns;
    		ccCopy.isDelayTurnsModified = isDelayTurnsModified;
    		ccCopy.spellBlock = spellBlock;
    		ccCopy.baseSpellBlock = baseSpellBlock;
    		ccCopy.upgradedSpellBlock = upgradedSpellBlock;
    		ccCopy.isSpellBlockModified = isSpellBlockModified;
    		ccCopy.spellDamage = spellDamage;
    		ccCopy.baseSpellDamage = baseSpellDamage;
    		ccCopy.upgradedSpellDamage = upgradedSpellDamage;
    		ccCopy.isSpellDamageModified = isSpellDamageModified;
    		ccCopy.m2 = m2;
    		ccCopy.baseM2 = baseM2;
    		ccCopy.upgradedM2 = upgradedM2;
    		ccCopy.isM2Modified = isM2Modified;
    		
    		ccCopy.rawDescription = rawDescription;
    		ccCopy.initializeDescription();
    		return ccCopy;
    	}
		return copy;
    }
    
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
    
    
    public static void customApplyPlayerPowersToSpellDamage(DamageInfo info) {
    	info.output = (int) customApplyPlayerPowersToSpellDamage(info.output);
    }
    
    public static void customApplyEnemyPowersToSpellDamage(DamageInfo info, AbstractMonster mo) {
    	info.output = (int) customApplyEnemyPowersToSpellDamage(info.output, mo);
    }
    
    public static float customApplyPlayerPowersToSpellDamage(float damageToCalculate) {
        final AbstractPlayer player = AbstractDungeon.player;
        float tmp = damageToCalculate;
        if (player != null) {
        	for (final AbstractPower p : player.powers) {
        		if (ineffectivePowers.contains(p.ID)) continue;
        		tmp = p.atDamageGive(tmp, DamageType.NORMAL);
        	}
        	for (final AbstractPower p : player.powers) {
        		if (ineffectivePowers.contains(p.ID)) continue;
        		tmp = p.atDamageFinalGive(tmp, DamageType.NORMAL);
        	}
        }
        tmp += PowersHelper.getPlayerPowerAmount(FocusPower.POWER_ID);
        if (tmp < 0.0f) {
            tmp = 0.0f;
        }
        return tmp;
    }
    
    public static float customApplyEnemyPowersToSpellDamage(float damageToCalculate, AbstractMonster mo) {
    	float tmp = damageToCalculate;
        if (mo != null) {
            for (final AbstractPower p : mo.powers) {
            	if (ineffectivePowers.contains(p.ID)) continue;
                tmp = p.atDamageReceive(tmp, DamageType.NORMAL);
            }
        }
        if (mo != null) {
            for (final AbstractPower p : mo.powers) {
            	if (ineffectivePowers.contains(p.ID)) continue;
                tmp = p.atDamageFinalReceive(tmp, DamageType.NORMAL);
            }
        }
        if (tmp < 0.0f) {
            tmp = 0.0f;
        }
    	return tmp;
    }
    
    public static float customApplyPlayerPowersToSpellBlock(float blockToCalculate) {
    	float tmp = blockToCalculate;
    	tmp += PowersHelper.getPlayerPowerAmount(FocusPower.POWER_ID);
    	return tmp;
    }
    
    public void onFrozen() {}
}