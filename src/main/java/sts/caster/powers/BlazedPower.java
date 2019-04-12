package sts.caster.powers;

import static sts.caster.core.CasterMod.makePowerPath;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import sts.caster.core.CasterMod;
import sts.caster.util.TextureHelper;

//Gain 1 dex for the turn for each card played.

public class BlazedPower extends AbstractPower {
	public AbstractCreature source;

	public static final String POWER_ID = CasterMod.makeID("Blazed");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureHelper.getTexture(makePowerPath("placeholder_power84.png"));
	private static final Texture tex32 = TextureHelper.getTexture(makePowerPath("placeholder_power32.png"));

	public BlazedPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
		name = NAME;
		ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.source = source;
		this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
		this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

		isTurnBased = false;
		canGoNegative = false;
		type = PowerType.DEBUFF;
		updateDescription();
	}

	private boolean attackedThisTurn = false;
	
	@Override
	public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
		AbstractDungeon.actionManager.addToBottom(new LoseHPAction(owner, source, amount, AttackEffect.FIRE));
		attackedThisTurn = true;
	}
	
	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if (attackedThisTurn) {
			int amountToLose = (int) Math.ceil(((float)amount)/2f);
			AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, source, POWER_ID, amountToLose));
		}
		attackedThisTurn = false;
	}
	
	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
	}

}
