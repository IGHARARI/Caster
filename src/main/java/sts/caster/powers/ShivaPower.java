package sts.caster.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import sts.caster.core.CasterMod;
import sts.caster.interfaces.OnFreezePower;
import sts.caster.util.TextureHelper;

import static sts.caster.core.CasterMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class ShivaPower extends AbstractPower implements OnFreezePower {
	public AbstractCreature source;

	public static final String POWER_ID = CasterMod.makeID("Shiva");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureHelper.getTexture(makePowerPath("shiva84.png"));
	private static final Texture tex32 = TextureHelper.getTexture(makePowerPath("shiva32.png"));

	public ShivaPower(final AbstractCreature owner, final int amount) {
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
	public void updateDescription() {
    	description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
	}

	@Override
	public void onFreeze(AbstractCard c) {
		addToBot(new ApplyPowerAction(owner, owner, new PlatedArmorPower(owner, amount), amount));
	}
}
