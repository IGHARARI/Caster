package sts.caster.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import sts.caster.actions.ElectrifyCardsAction;
import sts.caster.core.CasterMod;
import sts.caster.interfaces.OnElectrifyPower;
import sts.caster.util.TextureHelper;

import static sts.caster.core.CasterMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class GainPower extends TwoAmountPower implements OnElectrifyPower {
	public AbstractCreature source;

	public static final String POWER_ID = CasterMod.makeID("Gain");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureHelper.getTexture(makePowerPath("static84.png"));
	private static final Texture tex32 = TextureHelper.getTexture(makePowerPath("static32.png"));

	private static final int ELECTRIFY_TRIGGER_AMOUNT = 4;

	public GainPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
		name = NAME;
		ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.amount2 = 0;
		this.source = source;
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
	public void atStartOfTurnPostDraw() {
		addToBot(new ElectrifyCardsAction(1, 1, false));
		addToBot(new DrawCardAction(AbstractDungeon.player, 1));
	}

	@Override
	public void onElectrify(AbstractCard c) {
//		amount2++;
//		if (amount2 >= ELECTRIFY_TRIGGER_AMOUNT) {
//			amount2 = 0;
//			addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, amount), amount));
//		}
	}
}
