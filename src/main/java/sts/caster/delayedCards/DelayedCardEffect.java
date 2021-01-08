package sts.caster.delayedCards;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.PenNibPower;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbPassiveEffect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sts.caster.actions.*;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.elements.ElementsHelper;

public class DelayedCardEffect extends AbstractOrb {
	public int turnsUntilFire;

	public static final String ORB_ID = "DelayedCard:";
	public static final float WAIT_TIME_BETWEEN_DELAYED_EFFECTS = 0.1f;
	public static final float ORIGINAL_WAIT_TIME_BETWEEN_DELAYED_EFFECTS = 0.66f;
	private static final float GLITTER_MIN_INTERVAL = 0.77f;
	private static final float GLITTER_MAX_INTERVAL = 1.22f;

	private float vfxTimer = 1.0f;
	
	public CasterCard delayedCard = null;
	public CasterCard cardMiniCopy = null;
	public CasterCard cardPreviewCopy = null;
	public CasterCard cardEvokeCopy = null;
	public boolean showEvokeCardOnScreen = false;
	
	public AbstractMonster target = null;
	public Integer energyOnCast;

	private Boolean isPowersApplied = false;
	private boolean beingEvoked;

	public DelayedCardEffect(CasterCard card, int delayTurns, AbstractMonster target) {
		this(card, delayTurns, 0, target);
	}
	public DelayedCardEffect(CasterCard card, int delayTurns, Integer energyOnCast, AbstractMonster target) {
        super();
        
		ID = ORB_ID + card.uuid;
		name = card.name;

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
		cardMiniCopy.targetDrawScale = cardMiniCopy.drawScale;

		cardPreviewCopy = delayedCard.makeStatIdenticalCopy();
		cardPreviewCopy.current_x = cX;
		cardPreviewCopy.current_y = cY;
		cardEvokeCopy = delayedCard.makeStatIdenticalCopy();
		cardEvokeCopy.current_x = cX;
		cardEvokeCopy.current_y = cY;

		beingEvoked = false;
	}

	public void increaseDelay(int amount) {
		turnsUntilFire += amount;
		passiveAmount = turnsUntilFire;
	}

	@Override
	public void updateDescription() {}

	@Override
	public void applyFocus() {}

	@Override
	public void update() {
        if (hb.hovered && hb.justHovered) {
        	delayedCard.calculateCardDamage(target);
        	cardPreviewCopy.calculateCardDamage(target);
        	cardEvokeCopy.calculateCardDamage(target);
        	cardMiniCopy.calculateCardDamage(target);
        }
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
			System.out.println("Adding to Evoke Area: " + this.ID);
			DelayedCardsArea.evokingCards.add(this);
			DelayedCardsArea.delayedCards.remove(this);
			this.beingEvoked = true;
			DelayedCardsArea.redrawEvokeCards();

			AbstractDungeon.actionManager.addToBottom(new DelayedEffectRemoveAction(this));
			AbstractDungeon.actionManager.addToBottom(new QueueRedrawMiniCardsAction(false));
			AbstractDungeon.actionManager.addToBottom(new QueueEvokeCardAction(this)); // modified
		}
	}

	public void evokeCardEffect(){
		penNibCheck();
		// Helper call to apply/update elemental affliction and then apply manastruck
		ElementsHelper.updateElementalAffliction(delayedCard, target);
//		AbstractDungeon.actionManager.addToTop(new DelayedEffectRemoveAction(this));
		AbstractDungeon.actionManager.addToTop(new DelayedEffectHideEvokedCard(this)); // modified
//		AbstractDungeon.actionManager.addToTop(new VFXAction(new ExhaustCardEffect(cardEvokeCopy)));
//		AbstractDungeon.actionManager.addToTop(new NonSkippableWaitAction(WAIT_TIME_BETWEEN_DELAYED_EFFECTS/1.5f));
//		AbstractDungeon.actionManager.addToTop(new NonSkippableWaitAction(WAIT_TIME_BETWEEN_DELAYED_EFFECTS));
		applyPowersToAllCardCopies();
		delayedCard.calculateCardDamage(target);
		ArrayList<AbstractGameAction> delayedActions = delayedCard.buildActionsSupplier(energyOnCast).getActionList(delayedCard, target);
		for (int i = delayedActions.size() -1; i >= 0; i--) {
			AbstractGameAction action = delayedActions.get(i);
			AbstractDungeon.actionManager.addToTop(action);
		}
//		AbstractDungeon.actionManager.addToTop(new NonSkippableWaitAction(WAIT_TIME_BETWEEN_DELAYED_EFFECTS));
//		AbstractDungeon.actionManager.addToTop(new DelayedEffectShowCardToEvoke(this)); // modified
	}

	private void penNibCheck() {
		if (AbstractDungeon.player.hasPower(PenNibPower.POWER_ID) && delayedCard.damage > 0) {
			AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, PenNibPower.POWER_ID));
		}
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

	// Render the card.
	@Override
	public void render(SpriteBatch sb) {
		cardMiniCopy.current_x = cX;
		cardMiniCopy.current_y = cY;
		cardMiniCopy.drawScale = MathHelper.cardScaleLerpSnap(cardMiniCopy.drawScale, cardMiniCopy.targetDrawScale);
		cardMiniCopy.render(sb);
		// renderText(sb);
		// Reimplement render text so other patches on orbs don't break the spells
		// Looking at you replay the spire.
		if (!beingEvoked) renderSpellDelay(sb);
		hb.render(sb);
		if (showEvokeCardOnScreen) {
			renderCardCopy(sb, cardEvokeCopy, Settings.WIDTH/2f, Settings.HEIGHT/2f);
		}
	}

	private void renderSpellDelay(SpriteBatch sb) {
		FontHelper.renderFontCentered(sb,
				FontHelper.cardEnergyFont_L,
				Integer.toString(this.passiveAmount),
				this.cX + NUM_X_OFFSET,
				this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET,
				this.c,
				this.fontScale
		);
	}

	public static final Logger logger = LogManager.getLogger(CasterMod.class.getName());
	public boolean renderPreviewIfHovered(SpriteBatch sb) {
		if (hb.hovered) {
			if (!isPowersApplied) {
				isPowersApplied = true;
				applyPowersToAllCardCopies();
			}
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
		} else {
			if (isPowersApplied) isPowersApplied = false;
		}
		return hb.hovered;
	}

	private void applyPowersToAllCardCopies() {
		delayedCard.applyPowers();
		delayedCard.calculateCardDamage(target);
		cardPreviewCopy.applyPowers();
		cardPreviewCopy.calculateCardDamage(target);
		cardMiniCopy.applyPowers();
		cardMiniCopy.calculateCardDamage(target);
		cardEvokeCopy.applyPowers();
		cardEvokeCopy.calculateCardDamage(target);
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
