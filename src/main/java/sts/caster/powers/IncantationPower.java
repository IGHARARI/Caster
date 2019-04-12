package sts.caster.powers;

import static sts.caster.core.CasterMod.makePowerPath;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import sts.caster.actions.QuickCastAction;
import sts.caster.core.CasterMod;
import sts.caster.util.TextureHelper;

//Gain 1 dex for the turn for each card played.

public class IncantationPower extends AbstractPower {
	public AbstractCreature source;

	public static final String POWER_ID = CasterMod.makeID("Incantation");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureHelper.getTexture(makePowerPath("placeholder_power84.png"));
	private static final Texture tex32 = TextureHelper.getTexture(makePowerPath("placeholder_power32.png"));

	public IncantationPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
		name = NAME;
		ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.source = source;
		this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
		this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

		this.priority = 11;
		
		isTurnBased = false;
		canGoNegative = false;
		type = PowerType.BUFF;
		updateDescription();
	}

	@Override
	public void atStartOfTurnPostDraw() {
		for (int i = 0; i < amount; i++) {
			AbstractDungeon.actionManager.addToBottom(new QuickCastAction(1, false));
		}
	}
	
	@Override
	public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + DESCRIPTIONS[1];
        } else if (amount > 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
	}

}
