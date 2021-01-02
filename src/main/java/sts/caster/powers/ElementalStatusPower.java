package sts.caster.powers;

import basemod.Pair;
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
import sts.caster.core.MagicElement;
import sts.caster.util.TextureHelper;

import java.util.HashMap;
import java.util.HashSet;

import static sts.caster.core.CasterMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class ElementalStatusPower extends AbstractPower {
	public AbstractCreature source;

	public static final String POWER_ID = CasterMod.makeID("ElementalStatusPower");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureHelper.getTexture(makePowerPath("frost84.png"));
	private static final Texture tex32 = TextureHelper.getTexture(makePowerPath("frost32.png"));

	public MagicElement element;

	public ElementalStatusPower(final AbstractCreature owner, final MagicElement element) {
		name = NAME;
		ID = POWER_ID;
		this.owner = owner;
		this.amount = -1;
		this.element = element;

		this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
		this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

		isTurnBased = false;
		canGoNegative = false;
		type = PowerType.DEBUFF;
		updateDescription();
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0] + element.toString() + DESCRIPTIONS[1];
	}
}
