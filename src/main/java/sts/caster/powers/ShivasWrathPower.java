package sts.caster.powers;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BlurPower;
import sts.caster.cards.mods.ElectrifiedCardMod;
import sts.caster.cards.mods.FrozenCardMod;
import sts.caster.core.CasterMod;
import sts.caster.stances.ShatteredStance;
import sts.caster.util.TextureHelper;

import static sts.caster.core.CasterMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class ShivasWrathPower extends AbstractPower {
	public AbstractCreature source;

	public static final String POWER_ID = CasterMod.makeID("Shiva");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureHelper.getTexture(makePowerPath("shiva84.png"));
	private static final Texture tex32 = TextureHelper.getTexture(makePowerPath("shiva32.png"));

	public ShivasWrathPower(final AbstractCreature owner) {
		name = NAME;
		ID = POWER_ID;
		this.owner = owner;
		this.amount = 0;
		this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
		this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

		isTurnBased = false;
		canGoNegative = false;
		type = PowerType.BUFF;
		updateDescription();
	}

	public void triggerShattered(AbstractCard card) {
		addToBot(new AbstractGameAction() {
			@Override
			public void update() {
				CardModifierManager.removeModifiersById(card, FrozenCardMod.ID, true);
				CardModifierManager.removeModifiersById(card, ElectrifiedCardMod.ID, true);
				isDone = true;
			}
		});
		AbstractPlayer p = AbstractDungeon.player;
		if (!(p.stance instanceof ShatteredStance)) {
			addToBot(new ChangeStanceAction(new ShatteredStance()));
			addToBot(new ApplyPowerAction(p, p, new BlurPower(p, 1), 1));
		}

	}

	@Override
	public void updateDescription() {
    	description = DESCRIPTIONS[0];
	}
}
