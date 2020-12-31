package sts.caster.powers;

import static sts.caster.core.CasterMod.makePowerPath;

import java.util.function.Predicate;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import sts.caster.cards.CasterCard;
import sts.caster.cards.spells.SoulStrike;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.util.TextureHelper;

//Gain 1 dex for the turn for each card played.

public class ManaOverflowPower extends AbstractPower {
	Predicate<CasterCard> isCardElemental = card -> {
		return (card.cardElement == MagicElement.FIRE || card.cardElement == MagicElement.EARTH || card.cardElement == MagicElement.THUNDER || card.cardElement == MagicElement.ICE);
	};
	Predicate<AbstractCard> isElementalSpellCard = card -> {
		return (CasterCard.isCardSpellPredicate.test(card) && 
				(card instanceof CasterCard) && isCardElemental.test((CasterCard)card));
	};

	public static final String POWER_ID = CasterMod.makeID("ManaOverflow");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureHelper.getTexture(makePowerPath("manaoverflow84.png"));
	private static final Texture tex32 = TextureHelper.getTexture(makePowerPath("manaoverflow32.png"));

	public ManaOverflowPower(final AbstractCreature owner, final int amount) {
		name = NAME;
		ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
		this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

		isTurnBased = false;
		canGoNegative = false;
		type = PowerType.BUFF;
		updateDescription();
	}

	@Override
	public void onPlayCard(AbstractCard card, AbstractMonster m) {
		if (isElementalSpellCard.test(card)) {
			addToBot(new MakeTempCardInHandAction(new SoulStrike(), amount));
		}
	}
	
	@Override
	public void updateDescription() {
    	description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
	}

}
