package sts.caster.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.caster.core.CasterMod;
import sts.caster.util.TextureHelper;

import static sts.caster.core.CasterMod.makePowerPath;

public class ShockedPower extends AbstractPower {
	public AbstractCreature source;

	public static final String POWER_ID = CasterMod.makeID("Shocked");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureHelper.getTexture(makePowerPath("shocked84.png"));
	private static final Texture tex32 = TextureHelper.getTexture(makePowerPath("shocked32.png"));

	public ShockedPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
		name = NAME;
		ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.source = source;
		this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
		this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

		isTurnBased = true;
		canGoNegative = false;
		type = PowerType.DEBUFF;
		updateDescription();
	}

	@Override
	public void atEndOfTurn(final boolean isPlayer) {
		if (!owner.isDeadOrEscaped() && owner.currentBlock > 0) {
			flash();
			int blockLost = Math.max(1, owner.currentBlock /2);
			addToBot(new LoseBlockAction(owner, owner, blockLost));
		}
        if (this.amount == 0) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        } else {
            addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
        }
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0];
	}

}
