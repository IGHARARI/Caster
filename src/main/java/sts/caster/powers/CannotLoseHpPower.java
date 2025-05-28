package sts.caster.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.caster.core.CasterMod;
import sts.caster.util.TextureHelper;

import static sts.caster.core.CasterMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class CannotLoseHpPower extends AbstractPower {
	public static final String POWER_ID = CasterMod.makeID("CannotLoseHpPower");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureHelper.getTexture(makePowerPath("manaoverflow84.png"));
	private static final Texture tex32 = TextureHelper.getTexture(makePowerPath("manaoverflow32.png"));

	public CannotLoseHpPower(final AbstractCreature owner, final int amount) {
		name = NAME;
		ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
		this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

//		isTurnBased = true;
		canGoNegative = false;
		type = PowerType.BUFF;
		updateDescription();
	}


	public void stackPower(int stackAmount) {
		super.stackPower(stackAmount);
		updateDescription();
	}

	public void atStartOfTurn() {
		this.flashWithoutSound();
		if (this.amount == 0) {
			this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
		} else {
			this.addToTop(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
		}

	}

	@Override
	public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
		return 0;
	}

	@Override
	public void updateDescription() {
		if (amount == 1) {
			description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
		} else {
			description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
		}
	}

}
