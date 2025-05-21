package sts.caster.delayedCards;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.PenNibPower;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbPassiveEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sts.caster.actions.*;
import sts.caster.cards.CasterCard;
import sts.caster.cards.mods.RecurringSpellCardMod;
import sts.caster.core.CasterMod;
import sts.caster.powers.SpellDamageDisplayPower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CastingSpellCard extends AbstractOrb {
	public int turnsUntilFire;

	public static final String ID_PREFIX = "DelayedCard:";
	public static final float WAIT_TIME_BETWEEN_DELAYED_EFFECTS = 0.1f;
	public static final float ORIGINAL_WAIT_TIME_BETWEEN_DELAYED_EFFECTS = 0.66f;
	private static final float GLITTER_MIN_INTERVAL = 0.77f;
	private static final float GLITTER_MAX_INTERVAL = 1.22f;

	private float vfxTimer = 1.0f;
	
	public CasterCard spellCard;
	public CasterCard cardMiniCopy;
	public CasterCard cardPreviewCopy;

	public AbstractMonster target;
	public Integer energyOnCast;

	private Boolean isPowersApplied = false;
	private boolean beingEvoked;
	private SpellDamageDisplayPower damageDisplayPower;

	public CastingSpellCard(CasterCard card, int delayTurns, AbstractMonster target) {
		this(card, delayTurns, 0, target);
	}
	public CastingSpellCard(CasterCard card, int delayTurns, Integer energyOnCast, AbstractMonster target) {
        super();
        
		ID = ID_PREFIX + card.uuid;
		name = card.name;
		this.spellCard = card.makeStatIdenticalCopy();
		if (spellCard.cost == -1) {
			spellCard.rawDescription += " NL Casted for " + energyOnCast + " Energy.";
			spellCard.initializeDescription();
		}
		this.turnsUntilFire = delayTurns;
		this.target = target;
		this.energyOnCast = energyOnCast;
		this.updateDescription();
		
		channelAnimTimer = 0.5f;
		
		AbstractPlayer p = AbstractDungeon.player;
		cX = p.drawX + p.hb_x;
		cY = p.drawY + p.hb_y + p.hb_h / 2.0f;
		hb = new Hitbox(38.0f * Settings.scale, 65.0f * Settings.scale);
		hb.move(cX, cY);
		
		cardMiniCopy = spellCard.makeStatIdenticalCopy();
		cardMiniCopy.current_x = cX;
		cardMiniCopy.current_y = cY;
		cardMiniCopy.hb.move(cX, cY);
		cardMiniCopy.drawScale /= 5F;
		cardMiniCopy.targetDrawScale = cardMiniCopy.drawScale;

		cardPreviewCopy = spellCard.makeStatIdenticalCopy();
		cardPreviewCopy.current_x = cX;
		cardPreviewCopy.current_y = cY;

		beingEvoked = false;
		if (this.spellCard.target == CardTarget.ENEMY){
			this.damageDisplayPower = new SpellDamageDisplayPower(target, p, this);
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, p, damageDisplayPower));
		}
	}

	static final Logger logger = LogManager.getLogger(CasterMod.class.getName());
	public void modifyCastingDelay(int modifyAmount) {
		turnsUntilFire += modifyAmount;
		if (turnsUntilFire <= 0) {
			cardFireEvent();
		}
	}

	public List<CasterCard> getAllCardCopies(){
		return Arrays.asList(spellCard, cardMiniCopy, cardPreviewCopy);
	}

	@Override
	public void updateDescription() {}

	@Override
	public void applyFocus() {}

	@Override
	public void update() {
        if (hb.hovered && hb.justHovered) {
			applyPowersToAllCardCopies();
        }
        this.hb.update();
        if (this.hb.hovered && (spellCard.target == CardTarget.ENEMY || spellCard.target == CardTarget.SELF_AND_ENEMY) && target != null && target.isDeadOrEscaped()) {
            TipHelper.renderGenericTip(this.tX + 96.0f * Settings.scale, this.tY + 64.0f * Settings.scale, this.name, "The original target for this Spell is now dead.");
        }
        this.fontScale = MathHelper.scaleLerpSnap(this.fontScale, 0.7f);
	}
	
	@Override
	public void onStartOfTurn() {
		spellCard.onStartOfTurnDelayEffect();
//		turnsUntilFire--;
//		spellCard.onStartOfTurnDelayEffect();
//		if (turnsUntilFire <= 0) {
//			cardFireEvent();
//		}
	}

	@Override
	public void onEndOfTurn() {
		turnsUntilFire--;
		spellCard.onEndOfTurnDelayEffect();
		if (turnsUntilFire <= 0) {
			cardFireEvent();
		}
	}

	public void cardFireEvent() {
		SpellCardsArea.cardsBeingEvoked.add(this);
		SpellCardsArea.spellCardsBeingCasted.remove(this);
		this.beingEvoked = true;
		SpellCardsArea.redrawEvokeCards();

		AbstractDungeon.actionManager.addToBottom(new DelayedEffectRemoveAction(this));
		AbstractDungeon.actionManager.addToBottom(new QueueRedrawMiniCardsAction(false));
		AbstractDungeon.actionManager.addToBottom(new QueueEvokeCardAction(this)); // modified

		processRecurrenceMods();
	}

	private void processRecurrenceMods() {
		if (CardModifierManager.hasModifier(this.spellCard, RecurringSpellCardMod.ID)) {
			ArrayList<AbstractCardModifier> mods = CardModifierManager.getModifiers(this.spellCard, RecurringSpellCardMod.ID);
			for (AbstractCardModifier mod : mods) {
				RecurringSpellCardMod recurMod = (RecurringSpellCardMod) mod;

				if (recurMod.recurAmount > 0) {
					CasterCard cardToCast = this.spellCard;
					recurMod.reduceRecurrence(cardToCast);
					recurMod.onAfterRecurringAction(cardToCast);
					AbstractDungeon.actionManager.addToBottom(new QueueRecurringEffectAction(cardToCast, cardToCast.delayTurns, energyOnCast, target));
				}

				if (recurMod.recurAmount <= 0) {
					CardModifierManager.removeSpecificModifier(this.spellCard, mod, true);
				}
			}
		}
	}

	public void evokeCardEffect(){
		if (damageDisplayPower != null) {
			AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(target, AbstractDungeon.player, damageDisplayPower));
		}
		penNibCheck();
		// Helper call to apply/update elemental affliction and then apply manastruck
		//ElementsHelper.updateElementalAffliction(spellCard, target);
		AbstractDungeon.actionManager.addToTop(new RemoveEvokingSpellCard(this)); // modified
		applyPowersToAllCardCopies();
//		spellCard.calculateCardDamage(target);
		ArrayList<AbstractGameAction> delayedActions = spellCard.actionListSupplier(energyOnCast).getActionList(spellCard, target);
		for (int i = delayedActions.size() -1; i >= 0; i--) {
			AbstractGameAction action = delayedActions.get(i);
			AbstractDungeon.actionManager.addToTop(action);
		}
	}

	private void penNibCheck() {
		if (AbstractDungeon.player.hasPower(PenNibPower.POWER_ID) && spellCard.damage > 0) {
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
	}

	public int getDamageAmount() {
		if (!isPowersApplied) {
			isPowersApplied = true;
			applyPowersToAllCardCopies();
		}

		int d = spellCard.spellDamage;
		isPowersApplied = false;
		return d;
	}

	private void renderSpellDelay(SpriteBatch sb) {
		FontHelper.renderFontCentered(sb,
				FontHelper.cardEnergyFont_L,
				Integer.toString(this.turnsUntilFire),
				this.cX + NUM_X_OFFSET,
				this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET,
				this.c,
				this.fontScale
		);
	}

	public boolean renderPreviewIfHovered(SpriteBatch sb) {
		if (hb.hovered) {
			if (!isPowersApplied) {
				isPowersApplied = true;
				applyPowersToAllCardCopies();
			}

			renderSpecificCard(sb, cardPreviewCopy, cX, cY);
			
	        switch (this.spellCard.target) {
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
		cardPreviewCopy.calculateCardDamage(target);
		cardPreviewCopy.applyPowers();
		cardMiniCopy.calculateCardDamage(target);
		cardMiniCopy.applyPowers();
		spellCard.calculateCardDamage(target);
		spellCard.applyPowers();
	}

	private void renderSpecificCard(SpriteBatch sb, AbstractCard card, float targetX, float targetY) {
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
		return new CastingSpellCard(this.spellCard, turnsUntilFire, energyOnCast, target);
	}

	@Override
	public void onEvoke() {}
}
