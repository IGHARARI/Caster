package sts.caster.powers;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.caster.actions.IgniteSpecificCardAction;
import sts.caster.cards.mods.IgnitedCardMod;
import sts.caster.core.CasterMod;
import sts.caster.patches.spellCardType.CasterCardType;
import sts.caster.util.TextureHelper;

import static sts.caster.core.CasterMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class AmaterasuPower extends AbstractPower {
	public AbstractCreature source;

	public static final String POWER_ID = CasterMod.makeID("Amaterasu");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureHelper.getTexture(makePowerPath("amaterasu84.png"));
	private static final Texture tex32 = TextureHelper.getTexture(makePowerPath("amaterasu32.png"));

	public AmaterasuPower(final AbstractCreature owner, final int amount) {
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
	public void atStartOfTurnPostDraw() {
		super.atStartOfTurnPostDraw();
		AbstractPlayer p = AbstractDungeon.player;
		addToBot(
			new SelectCardsAction(
				p.exhaustPile.group,
				1,
				"Select a card to fetch and Ignite",
				false,
				c -> {
					return (
						(c.type == AbstractCard.CardType.ATTACK  || c.type == CasterCardType.SPELL)
						&& CardModifierManager.hasModifier(c, IgnitedCardMod.ID)
					);
				},
				selected -> selected.stream().forEach(
						c -> {
							addToTop(new IgniteSpecificCardAction(c, amount));
							addToTop(new ExhaustToHandAction(c));
						}
				)
			)
		);
	}

	@Override
	public void updateDescription() {
    	description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
	}

}
