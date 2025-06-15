package sts.caster.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseBlockPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import sts.caster.core.CasterMod;
import sts.caster.util.TextureHelper;

import static sts.caster.core.CasterMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class WallOfRocksPower extends TwoAmountPower implements OnLoseBlockPower, NonStackablePower {
	public static final String POWER_ID = CasterMod.makeID("WallOfRocksPower");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureHelper.getTexture(makePowerPath("manaoverflow84.png"));
	private static final Texture tex32 = TextureHelper.getTexture(makePowerPath("manaoverflow32.png"));

	public WallOfRocksPower(final AbstractCreature owner, final int triggers, final int strengthDecrease) {
		name = NAME;
		ID = POWER_ID;
		this.owner = owner;
		this.amount = triggers;
		this.amount2 = strengthDecrease;
		this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
		this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

//		isTurnBased = true;
		canGoNegative = false;
		type = PowerType.BUFF;
		updateDescription();
	}


	public void stackPower(int stackAmount) {
		super.stackPower(stackAmount);
		updateDescription();
	}

	public void atStartOfTurn() {
		this.flashWithoutSound();
		this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0] + amount2 + DESCRIPTIONS[1];
	}

	@Override
	public int onLoseBlock(DamageInfo damageInfo, int damageAmount) {
		AbstractPlayer p = AbstractDungeon.player;
		if (p != damageInfo.owner && damageInfo.type != DamageInfo.DamageType.HP_LOSS && p.currentBlock > 0 && damageAmount <= p.currentBlock) {
			AbstractCreature target = damageInfo.owner;
			addToTop(new ApplyPowerAction(target, owner, new StrengthPower(target, -amount2)));
			if (this.amount > 1) {
				addToBot(new ReducePowerAction(owner, owner, POWER_ID, 1));
			} else {
				addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
			}
		}
		return damageAmount;
	}
}
