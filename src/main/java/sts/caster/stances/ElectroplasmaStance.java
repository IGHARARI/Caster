package sts.caster.stances;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;
import com.megacrit.cardcrawl.vfx.stance.CalmParticleEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.patches.spellCardType.CasterCardType;

//Gain 1 dex for the turn for each card played.

public class ElectroplasmaStance extends AbstractStance {

	public static final String STANCE_ID = CasterMod.makeID("ElectroplasmaStance");
	private static long sfxId = -1L;

	public ElectroplasmaStance() {
		this.ID = STANCE_ID;
		this.name = CardCrawlGame.languagePack.getStanceString(STANCE_ID).NAME;
		this.updateDescription();
		CasterMod.logger.info("Stance created");
	}

	@Override
	public void updateAnimation() {
		if (!Settings.DISABLE_EFFECTS) {
			this.particleTimer -= Gdx.graphics.getDeltaTime();
			if (this.particleTimer < 0.0F) {
				this.particleTimer = 0.04F;
				AbstractDungeon.effectsQueue.add(new CalmParticleEffect());
			}
		}

		this.particleTimer2 -= Gdx.graphics.getDeltaTime();
		if (this.particleTimer2 < 0.0F) {
			this.particleTimer2 = MathUtils.random(0.45F, 0.55F);
			AbstractDungeon.effectsQueue.add(new StanceAuraEffect("Calm"));
		}
	}

	@Override
	public void onEnterStance() {
		CasterMod.logger.info("on enter stance");
		if (sfxId != -1L) {
			stopIdleSfx();
		}

		CardCrawlGame.sound.play("MONSTER_DONU_DEFENSE");

		AbstractDungeon.actionManager.addToTop(new VFXAction(AbstractDungeon.player, new IntenseZoomEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, false), 0.05F, true));
		AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.GOLDENROD, true));
	}


	@Override
	public void onPlayCard(AbstractCard card) {
		CasterMod.logger.info("on play card stance");
		super.onPlayCard(card);
		if (card instanceof CasterCard && card.type == CasterCardType.SPELL) {
			AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
		}
	}

	@Override
	public void atStartOfTurn() {
		CasterMod.logger.info("Stance start of trn");
		AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction("Neutral"));
	}

	@Override
	public void updateDescription() {
		this.description = CardCrawlGame.languagePack.getStanceString(STANCE_ID).DESCRIPTION[0];
	}
}
