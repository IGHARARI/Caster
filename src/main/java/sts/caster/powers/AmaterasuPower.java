package sts.caster.powers;

import static sts.caster.core.CasterMod.makePowerPath;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import sts.caster.actions.ModifyCardInBattleSpellDamageAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.patches.spellCardType.CasterCardType;
import sts.caster.util.TextureLoader;

//Gain 1 dex for the turn for each card played.

public class AmaterasuPower extends AbstractPower {
	public AbstractCreature source;

	public static final String POWER_ID = CasterMod.makeID("Amaterasu");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
	private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

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
	public void onPlayCard(AbstractCard card, AbstractMonster m) {
		if (card.type == CasterCardType.SPELL && (card instanceof CasterCard)) {
			CasterCard casterCard = (CasterCard)card;
			float modAmount = casterCard.baseSpellDamage;
			if (modAmount > 0 && casterCard.cardElement == MagicElement.FIRE) {
				modAmount = (modAmount * (float)amount)/100f;
				//Make it so it always increases at least 1
				modAmount = Math.max(1, modAmount);
				AbstractDungeon.actionManager.addToBottom(new ModifyCardInBattleSpellDamageAction(casterCard, (int)modAmount));
			}
		}
	}
	
	@Override
	public void updateDescription() {
    	description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
	}

}
