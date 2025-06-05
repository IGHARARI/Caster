package sts.caster.powers;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.caster.cards.mods.ElectrifiedCardMod;
import sts.caster.cards.mods.IgnitedCardMod;
import sts.caster.core.CasterMod;
import sts.caster.stances.ElectroplasmaStance;
import sts.caster.util.TextureHelper;

import static sts.caster.core.CasterMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class AetherflameCatalystPower extends AbstractPower {
	public AbstractCreature source;

	public static final String POWER_ID = CasterMod.makeID("AetherflameCatalystPower");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureHelper.getTexture(makePowerPath("izanagi84.png"));
	private static final Texture tex32 = TextureHelper.getTexture(makePowerPath("izanagi32.png"));

	public AetherflameCatalystPower(final AbstractCreature owner) {
		name = NAME;
		ID = POWER_ID;
		this.owner = owner;
		this.amount = 1;
		this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
		this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

		isTurnBased = false;
		canGoNegative = false;
		type = PowerType.BUFF;
		updateDescription();
	}

	public void triggerElectroPlasma(AbstractCard card) {
		addToBot(new AbstractGameAction() {
			@Override
			public void update() {
				CardModifierManager.removeModifiersById(card, IgnitedCardMod.ID, true);
				CardModifierManager.removeModifiersById(card, ElectrifiedCardMod.ID, true);
				isDone = true;
			}
		});
		addToBot(new ChangeStanceAction(new ElectroplasmaStance()));
	}

	@Override
	public void updateDescription() {
    	description = DESCRIPTIONS[0];
	}
}
