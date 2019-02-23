package sts.caster.delayedCards;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbPassiveEffect;

import sts.caster.actions.DelayedEffectHideEvokedCard;
import sts.caster.actions.DelayedEffectRemoveAction;
import sts.caster.actions.DelayedEffectShowCardToEvoke;
import sts.caster.actions.NonSkippableWaitAction;
import sts.caster.cards.CasterCard;

public class DelayedCardEffect extends AbstractOrb {
	private static final Logger logger = LogManager.getLogger(DelayedCardEffect.class.getName());
	
	public int turnsUntilFire;
	private ArrayList<AbstractGameAction> delayedActions;

	public static final String ORB_ID = "DelayedCard:";
	public static final float WAIT_TIME_BETWEEN_DELAYED_EFFECTS = 0.9f;
	private static final float GLITTER_MIN_INTERVAL = 0.77f;
	private static final float GLITTER_MAX_INTERVAL = 1.22f;

	private float vfxTimer = 1.0f;
	
	public AbstractCard delayedCard = null;
	public AbstractCard cardMiniCopy = null;
	public AbstractCard cardPreviewCopy = null;
	public AbstractCard cardEvokeCopy = null;
	public boolean showEvokeCardOnScreen = false;
	
	public DelayedCardEffect(AbstractCard card, int delayTurns, ArrayList<AbstractGameAction> delayedActions) {
        super();
        
		ID = ORB_ID + card.uuid;
		name = "ORB: " + card.name;
		img = ImageMaster.loadImage("orbs/default_orb.png");
		
		this.delayedCard = card.makeStatEquivalentCopy();
		this.turnsUntilFire = delayTurns;
		this.delayedActions = delayedActions;
		passiveAmount = basePassiveAmount = delayTurns;
		
		this.updateDescription();
		
		channelAnimTimer = 0.5f;
		
		AbstractPlayer p = AbstractDungeon.player;
		cX = p.drawX + p.hb_x;
		cY = p.drawY + p.hb_y + p.hb_h / 2.0f;
		hb = new Hitbox(38.0f * Settings.scale, 65.0f * Settings.scale);
		hb.move(cX, cY);
		
		cardMiniCopy = delayedCard.makeStatEquivalentCopy();
		cardMiniCopy.current_x = cX;
		cardMiniCopy.current_y = cY;
		cardMiniCopy.hb.move(cX, cY);
		cardMiniCopy.drawScale /= 5F;
		

		cardPreviewCopy = delayedCard.makeStatEquivalentCopy();
		cardPreviewCopy.current_x = cX;
		cardPreviewCopy.current_y = cY;
		cardEvokeCopy = delayedCard.makeStatEquivalentCopy();
		cardEvokeCopy.current_x = cX;
		cardEvokeCopy.current_y = cY;
	}
	
	private String getDynamicValue(final String key) {
//		return Integer.toString(turnsUntilFire);
		if (key.length() == 1){
			switch (key.charAt(0)) {
				case 'B': {
					if (!delayedCard.isBlockModified) {
						return Integer.toString(delayedCard.baseBlock);
					}
					if (delayedCard.block >= delayedCard.baseBlock) {
						return "[#7fff00]" + Integer.toString(delayedCard.block) + "[]";
					}
					return "[#ff6563]" + Integer.toString(delayedCard.block) + "[]";
				}
				case 'D': {
					if (!delayedCard.isDamageModified) {
						return Integer.toString(delayedCard.baseDamage);
					}
					if (delayedCard.damage >= delayedCard.baseDamage) {
						return "[#7fff00]" + Integer.toString(delayedCard.damage) + "[]";
					}
					return "[#ff6563]" + Integer.toString(delayedCard.damage) + "[]";
				}
				case 'M': {
					if (!delayedCard.isMagicNumberModified) {
						return Integer.toString(delayedCard.baseMagicNumber);
					}
					if (delayedCard.magicNumber >= delayedCard.baseMagicNumber) {
						return "[#7fff00]" + Integer.toString(delayedCard.magicNumber) + "[]";
					}
					return "[#ff6563]" + Integer.toString(delayedCard.magicNumber) + "[]";
				}
				default: {
					DelayedCardEffect.logger.info("KEY: " + key);
					return Integer.toString(-99);
				}
			}
		}
		else {
			Object value = null;
			try {
			Field field = delayedCard.getClass().getField("someField");

				value = field.get(delayedCard);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			return (String) value;
		}
	}

	@Override
	public void updateDescription() {
		delayedCard.initializeDescription();
		String rawDescription = "";
		for (int i=0; i<delayedCard.description.size(); i++){
			rawDescription += delayedCard.description.get(i).text;
			description = "";
			for (String tmp : rawDescription.split(" ")) {
				tmp += ' ';
				if (tmp.length() > 0 && tmp.charAt(0) == '*') {
					tmp = tmp.substring(1);
					String punctuation = "";
					if (tmp.length() > 1 && !Character.isLetter(tmp.charAt(tmp.length() - 2))) {
						punctuation += tmp.charAt(tmp.length() - 2);
						tmp = tmp.substring(0, tmp.length() - 2);
						punctuation += ' ';
					}
					description += tmp;
					description += punctuation;
				}
				else if (tmp.length() > 0 && tmp.charAt(0) == '!') {
					String key = "";
					for (int j=1; j<tmp.length(); j++){
						if (tmp.charAt(j) == '!'){
							description += getDynamicValue(key);
							description += tmp.substring(j+1);
						}
						else {
						key += tmp.charAt(j);
						}
					}
				}
				else{
					description += tmp;
				}
			}
		}
	}

	@Override
	public void applyFocus() {}

	@Override
	public void onStartOfTurn() {
		turnsUntilFire--;
		passiveAmount = turnsUntilFire;
		if (delayedCard instanceof CasterCard) ((CasterCard) delayedCard).onStartOfTurnDelayEffect();
		if (turnsUntilFire <= 0) {
			evokeCardEffect();
		}
	}

	public void evokeCardEffect(){
		AbstractDungeon.actionManager.addToBottom(new DelayedEffectShowCardToEvoke(this));
		AbstractDungeon.actionManager.addToBottom(new NonSkippableWaitAction(WAIT_TIME_BETWEEN_DELAYED_EFFECTS));
		for (AbstractGameAction action : delayedActions) {
			AbstractDungeon.actionManager.addToBottom(action);
		}
		AbstractDungeon.actionManager.addToBottom(new NonSkippableWaitAction(WAIT_TIME_BETWEEN_DELAYED_EFFECTS/1.5f));
		AbstractDungeon.actionManager.addToBottom(new VFXAction(new ExhaustCardEffect(cardEvokeCopy)));
		AbstractDungeon.actionManager.addToBottom(new DelayedEffectHideEvokedCard(this));
       	AbstractDungeon.actionManager.addToBottom(new DelayedEffectRemoveAction(this));
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
			renderCardCopy(sb, cardPreviewCopy, cX, cY);
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
		return new DelayedCardEffect(this.delayedCard, turnsUntilFire, delayedActions);
	}

	@Override
	public void onEvoke() {}
}
