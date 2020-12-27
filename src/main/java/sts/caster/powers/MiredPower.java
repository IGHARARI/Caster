package sts.caster.powers;

import static sts.caster.core.CasterMod.makePowerPath;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import sts.caster.actions.ConditionalAction;
import sts.caster.core.CasterMod;
import sts.caster.util.PowersHelper;
import sts.caster.util.TextureHelper;

import java.util.ArrayList;
import java.util.List;

//Gain 1 dex for the turn for each card played.

public class MiredPower extends AbstractPower {
	public AbstractCreature source;

	public static final String POWER_ID = CasterMod.makeID("Mired");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureHelper.getTexture(makePowerPath("mired84.png"));
	private static final Texture tex32 = TextureHelper.getTexture(makePowerPath("mired32.png"));

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
		if (attackUsed && info.owner != null && damageAmount > 0 && info.type != DamageType.HP_LOSS && info.type != DamageType.THORNS) {
			List<AbstractGameAction> procActions = new ArrayList<>();
			procActions.add(new HealAction(AbstractDungeon.player, owner, HEAL_AMOUNT));
			procActions.add(new ReducePowerAction(owner, owner, POWER_ID, 1));
			ConditionalAction miredProcAction = new ConditionalAction(
				() -> PowersHelper.getCreaturePowerAmount(POWER_ID, owner) > 0,
				procActions
			);
			addToBot(miredProcAction);
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
