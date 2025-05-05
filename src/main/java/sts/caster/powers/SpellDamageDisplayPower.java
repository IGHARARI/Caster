package sts.caster.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.caster.core.CasterMod;
import sts.caster.delayedCards.CastingSpellCard;
import sts.caster.util.TextureHelper;

import static sts.caster.core.CasterMod.makePowerPath;

public class SpellDamageDisplayPower extends AbstractPower implements HealthBarRenderPower, InvisiblePower, NonStackablePower {
	public AbstractCreature source;
	public CastingSpellCard spell;

	public static final String POWER_ID = CasterMod.makeID("Blazed");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureHelper.getTexture(makePowerPath("blazed84.png"));
	private static final Texture tex32 = TextureHelper.getTexture(makePowerPath("blazed32.png"));

	public SpellDamageDisplayPower(final AbstractCreature owner, final AbstractCreature source, final CastingSpellCard spell) {
		name = NAME;
		ID = POWER_ID;
		this.owner = owner;
		this.amount = 1;
		this.source = source;
		this.spell = spell;
		this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
		this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

		isTurnBased = false;
		canGoNegative = false;
		type = PowerType.BUFF;

		updateDescription();
	}

	@Override
	public int getHealthBarAmount() {
		return spell.getDamageAmount();
	}

	@Override
	public Color getColor() {
		return Color.CYAN.cpy();
	}
}
