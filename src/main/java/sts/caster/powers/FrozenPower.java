package sts.caster.powers;

import static sts.caster.core.CasterMod.makePowerPath;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import sts.caster.actions.FrozenTriggerAction;
import sts.caster.core.CasterMod;
import sts.caster.util.TextureLoader;

//Gain 1 dex for the turn for each card played.

public class FrozenPower extends AbstractPower {
	public AbstractCreature source;

	public static final String POWER_ID = CasterMod.makeID("Frost");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
	private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

	public FrozenPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
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

	@Override
	public int onAttacked(DamageInfo info, int damageAmount) {
		if (damageAmount > 0 && DamageType.NORMAL.equals(info.type)) {
			this.flash();
			AbstractDungeon.actionManager.addToTop(new FrozenTriggerAction(this.owner));
		}
		return super.onAttacked(info, damageAmount);
	}
	
	@Override
	public void updateDescription() {
		if (this.owner == null || this.owner.isPlayer) {
			description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
		} else  {
			description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
		}
	}

}
