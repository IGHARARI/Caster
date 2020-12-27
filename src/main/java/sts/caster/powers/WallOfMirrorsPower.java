package sts.caster.powers;

import static sts.caster.core.CasterMod.makePowerPath;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import sts.caster.core.CasterMod;
import sts.caster.util.TextureHelper;

//Gain 1 dex for the turn for each card played.

public class WallOfMirrorsPower extends TwoAmountPower {
	public AbstractCreature source;

	public static final String POWER_ID = CasterMod.makeID("WallOfMirrors");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureHelper.getTexture(makePowerPath("mirrors84.png"));
	private static final Texture tex32 = TextureHelper.getTexture(makePowerPath("mirrors32.png"));

	public WallOfMirrorsPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
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
		type = PowerType.DEBUFF;
		updateDescription();
	}
	
	@Override
	public float atDamageFinalGive(float damage, DamageType type) {
		amount2 = (int) damage;
		updateDescription();
		damage = 0;
		return super.atDamageFinalGive(damage, type);
	}
	
	
	@Override
	public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
		if (info.owner == owner && info.type != DamageType.THORNS && info.type != DamageType.HP_LOSS) {
			AbstractDungeon.actionManager.addToBottom(new DamageAction(owner, new DamageInfo(owner, amount2, DamageType.THORNS), AttackEffect.SHIELD));
		}
		super.onAttack(info, damageAmount, target);
	}
	
	@Override
	public void atEndOfRound() {
		amount2 = 0;
		AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, this, 1));
	}
	
	@Override
	public void updateDescription() {
    	description = DESCRIPTIONS[0] + amount2 + DESCRIPTIONS[1];
	}

}
