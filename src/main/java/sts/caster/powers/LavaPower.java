package sts.caster.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.util.TextureHelper;

import static sts.caster.core.CasterMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class LavaPower extends AbstractPower {
	public AbstractCreature source;

	public static final String POWER_ID = CasterMod.makeID("Lava");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureHelper.getTexture(makePowerPath("ashen84.png"));
	private static final Texture tex32 = TextureHelper.getTexture(makePowerPath("ashen32.png"));

	public LavaPower(final AbstractCreature owner, int amount) {
		name = NAME;
		ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
		this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

		isTurnBased = true;
		canGoNegative = false;
		type = PowerType.BUFF;
		updateDescription();
	}

	@Override
	public void atEndOfTurn(boolean isPlayer) {
		addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
	}

	@Override
	public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
		if (type == DamageInfo.DamageType.NORMAL && card instanceof CasterCard && ((CasterCard)card).cardElement == MagicElement.FIRE) {
			return damage + amount;
		}
		return damage;
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
	}

}
