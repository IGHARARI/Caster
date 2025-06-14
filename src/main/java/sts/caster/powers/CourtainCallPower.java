package sts.caster.powers;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.caster.cards.mods.RecurringSpellCardMod;
import sts.caster.core.CasterMod;
import sts.caster.util.TextureHelper;

import static sts.caster.core.CasterMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class CourtainCallPower extends AbstractPower {
	public AbstractCreature source;

	public static final String POWER_ID = CasterMod.makeID("CourtainCall");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureHelper.getTexture(makePowerPath("izanagi84.png"));
	private static final Texture tex32 = TextureHelper.getTexture(makePowerPath("izanagi32.png"));

	public CourtainCallPower(final AbstractCreature owner) {
		name = NAME;
		ID = POWER_ID;
		this.owner = owner;
		this.amount = 0;
		this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
		this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

		isTurnBased = false;
		canGoNegative = false;
		type = PowerType.BUFF;
		this.priority = 0;
		updateDescription();
	}

	@Override
	public float atDamageFinalGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
		damage = super.atDamageFinalGive(damage, type, card);
		if (CardModifierManager.hasModifier(card, RecurringSpellCardMod.ID)) {
			return damage * 2;
		}

		return damage;
	}

	@Override
	public void updateDescription() {
    	description = DESCRIPTIONS[0];
	}
}
