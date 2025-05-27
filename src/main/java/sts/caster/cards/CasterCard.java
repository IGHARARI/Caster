package sts.caster.cards;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import sts.caster.cards.special.Charred;
import sts.caster.core.MagicElement;
import sts.caster.interfaces.ActionListSupplier;
import sts.caster.patches.spellCardType.CasterCardType;
import sts.caster.powers.ManaImbalancePower;
import sts.caster.powers.ShortenedChantPower;
import sts.caster.util.PowersHelper;

import java.util.*;
import java.util.function.Predicate;

public abstract class CasterCard extends CustomCard {
	private final static HashSet<String> ineffectivePowers = new HashSet<String>(Arrays.asList(PenNibPower.POWER_ID, StrengthPower.POWER_ID, DexterityPower.POWER_ID, WeakPower.POWER_ID, VulnerablePower.POWER_ID));
	
	public static final Predicate<AbstractCard> isCardSpellPredicate = (c)-> c.type == CasterCardType.SPELL;
	private static final String BASE_BG_PATH = "caster/images/card_backgrounds/";
	private static final UIStrings SPELL_DESC = CardCrawlGame.languagePack.getUIString("SpellsDescription");

	public int delayTurnsModificationForTurn;
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

		delayTurnsModificationForTurn = 0;
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
	public List<TooltipInfo> getCustomTooltips() {
		List<TooltipInfo> curTips = super.getCustomTooltips();
		if (type == CasterCardType.SPELL) {
			if (curTips == null) curTips = new ArrayList<TooltipInfo>();
			curTips.add(new TooltipInfo(SPELL_DESC.TEXT[0], SPELL_DESC.TEXT[1]));
//			switch (cardElement) {
//				case EARTH:
//					curTips.add(new TooltipInfo(EARTH_DESC.TEXT[0], EARTH_DESC.TEXT[1]));
//					break;
//				case FIRE:
//					curTips.add(new TooltipInfo(FIRE_DESC.TEXT[0], FIRE_DESC.TEXT[1]));
//					break;
//				case ICE:
//					curTips.add(new TooltipInfo(ICE_DESC.TEXT[0], ICE_DESC.TEXT[1]));
//					break;
//				case ELECTRIC:
//					curTips.add(new TooltipInfo(LIGHTNING_DESC.TEXT[0], LIGHTNING_DESC.TEXT[1]));
//					break;
//				default:
//					break;
//			}
		}
		return curTips;
	}
	
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
		case ELECTRIC:
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
	
	// Return an empty list by default to prevent NPEs on cards accidentally not overriding this.
	public ActionListSupplier actionListSupplier(Integer energySpent) {return (c, t)-> {return new ArrayList<AbstractGameAction>();};}

//	@Override
//	public void triggerOnGlowCheck() {
//		if (willCauseManaStruck(this)) {
//			this.glowColor = Color.PURPLE.cpy();
//		} else {
//			this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
//		}
//	}


	@Override
	public void applyPowers() {
		int realBaseDamage = baseDamage;
		int realBaseSpellDamage = baseSpellDamage;

		if (this.cardElement == MagicElement.FIRE) {
			baseDamage += getLavaModifiers();
			baseSpellDamage += getLavaModifiers();
		}

		if (this.type == CasterCardType.SPELL) {
			calculateCardDamage(null);
		} else {
			super.applyPowers();
		}

		baseDamage = realBaseDamage;
		baseSpellDamage = realBaseSpellDamage;
		isDamageModified = damage != baseDamage;
		isSpellDamageModified = spellDamage != baseSpellDamage;
	}

	private int getLavaModifiers() {
		int totalModifier = 0;
		for (AbstractCard c : AbstractDungeon.player.hand.group) {
			if (Objects.equals(c.cardID, Charred.ID)) {
				totalModifier += c.magicNumber;
			}
		}
		return totalModifier;
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		int realBaseDamage = baseDamage;
		int realBaseSpellDamage = baseSpellDamage;

		if (this.cardElement == MagicElement.FIRE) {
			baseDamage += getLavaModifiers();
			baseSpellDamage += getLavaModifiers();
		}

		if (this.type == CasterCardType.SPELL) {
			resetCardSpellDamage();
			resetCardSpellBlock();
			applyCardDamageModifers(mo);
			applyCardBlockModifiers(mo);
			int totalDelayModification = delayTurnsModificationForTurn - PowersHelper.getPlayerPowerAmount(ShortenedChantPower.POWER_ID);
			delayTurns = Math.max(0, baseDelayTurns + totalDelayModification);
			if (delayTurns != baseDelayTurns) isDelayTurnsModified = true;
		} else {
			super.calculateCardDamage(mo);
		}
		baseDamage = realBaseDamage;
		baseSpellDamage = realBaseSpellDamage;
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
		
		damageCalculation = customApplyPlayerPowersToSpellDamage(damageCalculation, this);
		if (mo != null && target != CardTarget.ALL_ENEMY && target != CardTarget.ALL) {
			damageCalculation = customApplyEnemyPowersToSpellDamage(damageCalculation, cardElement, mo);

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

	private static boolean isCreatureVulnerableTo(AbstractCreature mo, MagicElement element) {
		return mo.hasPower(ManaImbalancePower.POWER_ID);
//		switch (element) {
//			case FIRE:
//				return mo.hasPower(MiredPower.POWER_ID);
//			case ICE:
//				return mo.hasPower(BlazedPower.POWER_ID);
//			case THUNDER:
//				return mo.hasPower(FrostPower.POWER_ID);
//			case EARTH:
//				return mo.hasPower(ShockedPower.POWER_ID);
//			default:
//				return false;
//		}
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
		delayTurnsModificationForTurn = 0;
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
	public void onEndOfTurnDelayEffect() {}

	@Override
	public AbstractCard makeStatEquivalentCopy() {
		CasterCard card = (CasterCard) super.makeStatEquivalentCopy();
		card.cardElement = cardElement;
		card.baseDelayTurns = baseDelayTurns;
		card.baseSpellBlock = baseSpellBlock;
		card.baseSpellDamage = baseSpellDamage;
		card.baseM2 = baseM2;
		
		return card;
	}
	
	public CasterCard makeStatIdenticalCopy() {
		CasterCard copy = (CasterCard) this.makeStatEquivalentCopy();
		copy.cardElement = cardElement;
		copy.delayTurnsModificationForTurn = delayTurnsModificationForTurn;
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
		copy.magicNumber = magicNumber;
		copy.baseMagicNumber = baseMagicNumber;
		copy.upgradedMagicNumber = upgradedMagicNumber;
		copy.isMagicNumberModified = isMagicNumberModified;

		copy.rawDescription = rawDescription;
		copy.initializeDescription();
		
		return copy;
	}
	
	public static void customApplyEnemyPowersToSpellDamage(DamageInfo info, MagicElement elem, AbstractCreature target) {
		info.output = (int) customApplyEnemyPowersToSpellDamage(info.output, elem, target);
	}
	
	public static float customApplyPlayerPowersToSpellDamage(float damageToCalculate, CasterCard casterCard) {
		final AbstractPlayer player = AbstractDungeon.player;
		float tmp = damageToCalculate;
		if (player != null) {
			for (final AbstractPower p : player.powers) {
				if (ineffectivePowers.contains(p.ID)) continue;
				tmp = p.atDamageGive(tmp, DamageType.NORMAL, casterCard);
			}
			for (final AbstractPower p : player.powers) {
				if (ineffectivePowers.contains(p.ID)) continue;
				tmp = p.atDamageFinalGive(tmp, DamageType.NORMAL, casterCard);
			}
		}
		tmp += PowersHelper.getPlayerPowerAmount(FocusPower.POWER_ID);
		if (tmp < 0.0f) {
			tmp = 0.0f;
		}
		return tmp;
	}
	
	public static float customApplyEnemyPowersToSpellDamage(float damageToCalculate, MagicElement elem, AbstractCreature mo) {
		float tmp = damageToCalculate;
		if (mo != null) {
			for (final AbstractPower p : mo.powers) {
				if (ineffectivePowers.contains(p.ID)) continue;
				tmp = p.atDamageReceive(tmp, DamageType.NORMAL);
			}
			for (final AbstractPower p : mo.powers) {
				if (ineffectivePowers.contains(p.ID)) continue;
				tmp = p.atDamageFinalReceive(tmp, DamageType.NORMAL);
			}
			if (isCreatureVulnerableTo(mo, elem)){
				tmp *= 1.5f;
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
	public void onThaw() {}
	public int getIntentNumber() { return target == CardTarget.SELF ? spellBlock : spellDamage; }
}