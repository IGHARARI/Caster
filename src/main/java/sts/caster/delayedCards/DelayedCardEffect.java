package sts.caster.delayedCards;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbPassiveEffect;

import sts.caster.characters.TheCaster;

public class DelayedCardEffect extends AbstractOrb {
	
	public int turnsUntilFire;
	private ArrayList<AbstractGameAction> delayedActions;

	private static final Logger logger = LogManager.getLogger(DelayedCardEffect.class.getName());

	// Standard ID/Description
	public static final String ORB_ID = "DelayedCard:";
	public static final float WAIT_TIME_BETWEEN_DELAYED_EFFECTS = 0.5f;
	public static final float CARD_AREA_X_RIGHT_OFFSET = 400f * Settings.scale;
	public static final float CARD_AREA_COLUMN_WIDTH = 100f * Settings.scale;
	public static final float CARD_AREA_COLUMN_HEIGH = 280f * Settings.scale;

	private float vfxTimer = 1.0f;
	private float vfxIntervalMin = 0.1f;
	private float vfxIntervalMax = 0.4f;
	
	public AbstractCard delayedCard = null;
	
	public DelayedCardEffect(AbstractCard card, int delayTurns, ArrayList<AbstractGameAction> delayedActions) {
		super();
		logger.info("Creating delayed card");
		if (AbstractDungeon.player instanceof TheCaster) {
			TheCaster caster = (TheCaster) AbstractDungeon.player;
			ID = ORB_ID + card.uuid;
			name = "ORB: " + card.name;
			img = ImageMaster.loadImage("orbs/default_orb.png");
			
			this.delayedCard = card;
			this.turnsUntilFire = delayTurns;
			this.delayedActions = delayedActions;
			passiveAmount = this.basePassiveAmount = delayTurns;
			
			this.updateDescription();
			
			angle = MathUtils.random(360.0f); // More Animation-related Numbers
			channelAnimTimer = 0.5f;
			
			this.cX = caster.drawX + caster.hb_x;
			this.cY = caster.drawY + caster.hb_y + caster.hb_h / 2.0f;
			for (int i = 0; i < caster.delayedCards.size(); ++i) {
				caster.delayedCards.get(i).setSlot(i, caster.delayedCards.size());
			}
			logger.info("Delayed card created and centered around character");
		}
	}
	
	public void removeFromPlayer() {
		if (AbstractDungeon.player instanceof TheCaster) {
			TheCaster caster = (TheCaster) AbstractDungeon.player;
			
			caster.delayedCards.remove(this);
			for (int i = 0; i < caster.delayedCards.size(); ++i) {
				caster.delayedCards.get(i).setSlot(i, caster.delayedCards.size());
			}
		}
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
	public void updateDescription() { // Set the on-hover description of the orb
//		logger.info("updating description for delayed card");
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
	public void applyFocus() {
		//Not affected by focus
	}

	@Override
	public void onStartOfTurn() {
		turnsUntilFire--;
		if (turnsUntilFire <= 0) {
			for (AbstractGameAction action : delayedActions) {
				AbstractDungeon.actionManager.addToBottom(action);
			}
			AbstractDungeon.actionManager.addToBottom(new WaitAction(WAIT_TIME_BETWEEN_DELAYED_EFFECTS));
		}
	}

	@Override
	public void updateAnimation() {
		super.updateAnimation();
		angle += Gdx.graphics.getDeltaTime() * 45.0f;
		vfxTimer -= Gdx.graphics.getDeltaTime();
		if (this.vfxTimer < 0.0f) {
			AbstractDungeon.effectList.add(new FrostOrbPassiveEffect(this.cX, this.cY));
			this.vfxTimer = MathUtils.random(this.vfxIntervalMin, this.vfxIntervalMax);
		}
	}

	// Render the orb.
	@Override
	public void render(SpriteBatch sb) {
		AbstractCard displayCopy = delayedCard.makeStatEquivalentCopy();
		displayCopy.current_x = cX;
		displayCopy.current_y = cY;
		displayCopy.drawScale /= 5F;
		displayCopy.render(sb);
		renderText(sb);
		hb.render(sb);
	}
	
	public boolean renderIfHovered(SpriteBatch sb) {
		if (hb.hovered) {
			AbstractCard bigCopy = delayedCard.makeStatEquivalentCopy();
			bigCopy.current_x = cX;
			bigCopy.current_y = cY;
			bigCopy.hb.cX = cX;
			bigCopy.hb.cY = cY;
			bigCopy.render(sb);
		}
		return hb.hovered;
	}
	
	@Override
    public void setSlot(final int slotNum, final int maxOrbs) {
        final float dist = 160.0f * Settings.scale + maxOrbs * 10.0f * Settings.scale;
        float angle = 100.0f + maxOrbs * 12.0f;
        final float offsetAngle = angle / 2.0f;
        angle *= slotNum / (maxOrbs - 1.0f);
        angle += 90.0f - offsetAngle;
        this.tX = dist * MathUtils.cosDeg(angle) + AbstractDungeon.player.drawX;
        this.tY = dist * MathUtils.sinDeg(angle) + AbstractDungeon.player.drawY + AbstractDungeon.player.hb_h / 2.0f;
        if (maxOrbs == 1) {
            this.tX = AbstractDungeon.player.drawX;
            this.tY = 160.0f * Settings.scale + AbstractDungeon.player.drawY + AbstractDungeon.player.hb_h / 2.0f;
        }
        this.hb.move(this.tX, this.tY);
    }

	@Override
	public void triggerEvokeAnimation() {}

	@Override
	public void playChannelSFX() { // When you channel this orb, the ATTACK_FIRE effect plays ("Fwoom").
		CardCrawlGame.sound.play("ATTACK_FIRE", 0.1f);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new DelayedCardEffect(this.delayedCard, turnsUntilFire, delayedActions);
	}

	@Override
	public void onEvoke() {}
}
