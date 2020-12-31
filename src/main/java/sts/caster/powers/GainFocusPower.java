package sts.caster.powers;

import static sts.caster.core.CasterMod.makePowerPath;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FocusPower;

import sts.caster.core.CasterMod;
import sts.caster.util.TextureHelper;

//Gain 1 dex for the turn for each card played.

public class GainFocusPower extends AbstractPower {
	public AbstractCreature source;

	public static final String POWER_ID = CasterMod.makeID("GainFocusPower");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureHelper.getTexture(makePowerPath("gainfocus84.png"));
	private static final Texture tex32 = TextureHelper.getTexture(makePowerPath("gainfocus32.png"));

	private static int procThisTurn=1;
	private static int procNextTurn=2;
	boolean isNextTurn;
	
	public GainFocusPower(final AbstractCreature owner, final int amount, Boolean isNextTurn) {
		name = NAME;
		ID = POWER_ID + (isNextTurn ? procNextTurn : procThisTurn);
		this.isNextTurn = isNextTurn;
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
	public void atStartOfTurnPostDraw() {
		if (isNextTurn) {
			this.flash();
			addToBot(new RemoveSpecificPowerAction(owner, owner, ID));
			addToBot(new ApplyPowerAction(owner, owner, new GainFocusPower(owner, amount, false), amount, true, AttackEffect.NONE));
		}
	}
	
	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if (!isNextTurn) {
			this.flash();
			addToBot(new RemoveSpecificPowerAction(owner, owner, ID));
			addToBot(new ApplyPowerAction(this.owner, this.owner, new FocusPower(this.owner, this.amount), this.amount));
		}
	}
	
	@Override
	public void updateDescription() {
		if (isNextTurn) {
			description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
		} else {
			description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
		}
	}

}
