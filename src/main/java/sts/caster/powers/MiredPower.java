package sts.caster.powers;

import static sts.caster.core.CasterMod.makePowerPath;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import sts.caster.core.CasterMod;
import sts.caster.util.TextureLoader;

//Gain 1 dex for the turn for each card played.

public class MiredPower extends AbstractPower {
	public AbstractCreature source;

	public static final String POWER_ID = CasterMod.makeID("Mired");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
	private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

	public static final int HEAL_AMOUNT = 2;
	private boolean attackUsed;
	
	public MiredPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
		name = NAME;
		ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.source = source;
		this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
		this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

		isTurnBased = false;
		canGoNegative = false;
		type = PowerType.DEBUFF;
		updateDescription();
		attackUsed = false;
		
	}
	
	@Override
	public int onAttacked(DamageInfo info, int damageAmount) {
		if (damageAmount > 0 && attackUsed) {
			AbstractDungeon.actionManager.addToBottom(new HealAction(AbstractDungeon.player, owner, HEAL_AMOUNT));
			AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, POWER_ID, 1));
		}
		return super.onAttacked(info, damageAmount);
	}
	
    @Override
    public void onUseCard(final AbstractCard card, final UseCardAction action) {
        if (card.type == CardType.ATTACK) {
            attackUsed = true;
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
    	attackUsed = false;
    }
    
    
	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0];
	}

}
