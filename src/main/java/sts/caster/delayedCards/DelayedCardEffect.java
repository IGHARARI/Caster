package sts.caster.delayedCards;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbPassiveEffect;

import sts.caster.actions.DelayedEffectHideEvokedCard;
import sts.caster.actions.DelayedEffectRemoveAction;
import sts.caster.actions.DelayedEffectShowCardToEvoke;
import sts.caster.actions.NonSkippableWaitAction;
import sts.caster.actions.QueueEvokeCardAction;
import sts.caster.cards.CasterCard;

public class DelayedCardEffect extends AbstractOrb {
	public int turnsUntilFire;

	public static final String ORB_ID = "DelayedCard:";
	public static final float WAIT_TIME_BETWEEN_DELAYED_EFFECTS = 0.9f;
	private static final float GLITTER_MIN_INTERVAL = 0.77f;
	private static final float GLITTER_MAX_INTERVAL = 1.22f;

	private float vfxTimer = 1.0f;
	
	public CasterCard delayedCard = null;
	public CasterCard cardMiniCopy = null;
	public CasterCard cardPreviewCopy = null;
	public CasterCard cardEvokeCopy = null;
	public boolean showEvokeCardOnScreen = false;
	
	public AbstractMonster target = null;
	public int energyOnCast;
	
	public DelayedCardEffect(CasterCard card, int delayTurns, AbstractMonster target) {
		this(card, delayTurns, 0, target);
	}
	public DelayedCardEffect(CasterCard card, int delayTurns, int energyOnCast, AbstractMonster target) {
        super();
        
		ID = ORB_ID + card.uuid;
		name = card.name;
		img = ImageMaster.loadImage("orbs/default_orb.png");
		
		this.delayedCard = card.makeStatIdenticalCopy();
		if (delayedCard.cost == -1) {
			delayedCard.rawDescription += " NL Casted for " + energyOnCast + " Energy.";
			delayedCard.initializeDescription();
		}
		this.turnsUntilFire = delayTurns;
		this.target = target;
		this.energyOnCast = energyOnCast;
		passiveAmount = basePassiveAmount = delayTurns;
		
		this.updateDescription();
		
		channelAnimTimer = 0.5f;
		
		AbstractPlayer p = AbstractDungeon.player;
		cX = p.drawX + p.hb_x;
		cY = p.drawY + p.hb_y + p.hb_h / 2.0f;
		hb = new Hitbox(38.0f * Settings.scale, 65.0f * Settings.scale);
		hb.move(cX, cY);
		
		cardMiniCopy = delayedCard.makeStatIdenticalCopy();
		cardMiniCopy.current_x = cX;
		cardMiniCopy.current_y = cY;
		cardMiniCopy.hb.move(cX, cY);
		cardMiniCopy.drawScale /= 5F;
		

		cardPreviewCopy = delayedCard.makeStatIdenticalCopy();
		cardPreviewCopy.current_x = cX;
		cardPreviewCopy.current_y = cY;
		cardEvokeCopy = delayedCard.makeStatIdenticalCopy();
		cardEvokeCopy.current_x = cX;
		cardEvokeCopy.current_y = cY;
	}
	
	@Override
	public void updateDescription() {}

	@Override
	public void applyFocus() {}

	@Override
	public void update() {
        this.hb.update();
        if (this.hb.hovered && (delayedCard.target == CardTarget.ENEMY || delayedCard.target == CardTarget.SELF_AND_ENEMY) && target != null && target.isDeadOrEscaped()) {
            TipHelper.renderGenericTip(this.tX + 96.0f * Settings.scale, this.tY + 64.0f * Settings.scale, this.name, "The original target for this Spell is now dead.");
        }
        this.fontScale = MathHelper.scaleLerpSnap(this.fontScale, 0.7f);
	}
	
	@Override
	public void onStartOfTurn() {
		turnsUntilFire--;
		passiveAmount = turnsUntilFire;
		delayedCard.onStartOfTurnDelayEffect();
		if (turnsUntilFire <= 0) {
//			evokeCardEffect();
			AbstractDungeon.actionManager.addToBottom(new QueueEvokeCardAction(this));
		}
	}

	public void evokeCardEffect(){
//		AbstractDungeon.actionManager.addToBottom(new DelayedEffectShowCardToEvoke(this));
//		AbstractDungeon.actionManager.addToBottom(new NonSkippableWaitAction(WAIT_TIME_BETWEEN_DELAYED_EFFECTS));
//		delayedCard.calculateCardDamage(target);
//		ArrayList<AbstractGameAction> delayedActions = delayedCard.getActionsMaker().getActionList(delayedCard, target);
//		for (AbstractGameAction action : delayedActions) {
//			AbstractDungeon.actionManager.addToBottom(action);
//		}
//		AbstractDungeon.actionManager.addToBottom(new NonSkippableWaitAction(WAIT_TIME_BETWEEN_DELAYED_EFFECTS/1.5f));
//		AbstractDungeon.actionManager.addToBottom(new VFXAction(new ExhaustCardEffect(cardEvokeCopy)));
//		AbstractDungeon.actionManager.addToBottom(new DelayedEffectHideEvokedCard(this));
//       	AbstractDungeon.actionManager.addToBottom(new DelayedEffectRemoveAction(this));

       	
		AbstractDungeon.actionManager.addToTop(new DelayedEffectRemoveAction(this));
		AbstractDungeon.actionManager.addToTop(new DelayedEffectHideEvokedCard(this));
		AbstractDungeon.actionManager.addToTop(new VFXAction(new ExhaustCardEffect(cardEvokeCopy)));
		AbstractDungeon.actionManager.addToTop(new NonSkippableWaitAction(WAIT_TIME_BETWEEN_DELAYED_EFFECTS/1.5f));
		delayedCard.calculateCardDamage(target);
		ArrayList<AbstractGameAction> delayedActions = delayedCard.getActionsMaker().getActionList(delayedCard, target);
		for (int i = delayedActions.size() -1; i >= 0; i--) {
			AbstractGameAction action = delayedActions.get(i);
			AbstractDungeon.actionManager.addToTop(action);
		}
		AbstractDungeon.actionManager.addToTop(new NonSkippableWaitAction(WAIT_TIME_BETWEEN_DELAYED_EFFECTS));
		AbstractDungeon.actionManager.addToTop(new DelayedEffectShowCardToEvoke(this));
		
	}
	
	@Override
	public void updateAnimation() {
		super.updateAnimation();
		vfxTimer -= Gdx.graphics.getDeltaTime();
		if (vfxTimer < 0.0f) {
			AbstractDungeon.effectList.add(new FrostOrbPassiveEffect(this.cX, this.cY));
			vfxTimer = MathUtils.random(GLITTER_MIN_INTERVAL, GLITTER_MAX_INTERVAL);
		}
	}

	// Render the orb.
	@Override
	public void render(SpriteBatch sb) {
		cardMiniCopy.current_x = cX;
		cardMiniCopy.current_y = cY;
		cardMiniCopy.render(sb);
		renderText(sb);
		hb.render(sb);
		if (showEvokeCardOnScreen) {
			renderCardCopy(sb, cardEvokeCopy, Settings.WIDTH/2f, Settings.HEIGHT/2f);
		}
	}
	
	public boolean renderPreviewIfHovered(SpriteBatch sb) {
		if (hb.hovered) {
			cardPreviewCopy.calculateCardDamage(target);
			renderCardCopy(sb, cardPreviewCopy, cX, cY);
			
	        switch (this.delayedCard.target) {
	            case ENEMY: {
	            	if (target != null && !target.isDeadOrEscaped()) {
	            		target.renderReticle(sb);
	            	}
	                break;
	            }
	            case ALL_ENEMY: {
	                AbstractDungeon.getCurrRoom().monsters.renderReticle(sb);
	                break;
	            }
	            case SELF: {
	                AbstractDungeon.player.renderReticle(sb);
	                break;
	            }
	            case SELF_AND_ENEMY: {
	            	AbstractDungeon.player.renderReticle(sb);
	            	if (target != null && !target.isDeadOrEscaped()) {
	            		target.renderReticle(sb);
	            	}
	                break;
	            }
	            case ALL: {
	            	AbstractDungeon.player.renderReticle(sb);
	                AbstractDungeon.getCurrRoom().monsters.renderReticle(sb);
	                break;
	            }
				default:
				break;
	        }
		}
		return hb.hovered;
	}
	
	private void renderCardCopy(SpriteBatch sb, AbstractCard card, float targetX, float targetY) {
		card.current_x = targetX;
		card.current_y = targetY;
		card.hb.cX = targetX;
		card.hb.cY = targetY;
		card.render(sb);
	}
	
	@Override
	public void triggerEvokeAnimation() {}

	@Override
	public void playChannelSFX() {
		CardCrawlGame.sound.play("ATTACK_FIRE", 0.1f);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new DelayedCardEffect(this.delayedCard, turnsUntilFire, energyOnCast, target);
	}

	@Override
	public void onEvoke() {}
}
