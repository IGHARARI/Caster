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
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbPassiveEffect;

import sts.caster.characters.TheCaster;

public class DelayedCardEffect extends AbstractOrb {
	
	public int turnsUntilFire;
	private ArrayList<AbstractGameAction> delayedActions;

	private static final Logger logger = LogManager.getLogger(DelayedCardEffect.class.getName());

	// Standard ID/Description
	public static final String ORB_ID = "DelayedCard:";
	public static final float WAIT_TIME_BETWEEN_DELAYED_EFFECTS = 0.5f;
	public static final float CARD_AREA_X_RIGHT_OFFSET = 200f * Settings.scale;
	public static final float VERT_SPACE_BTWN_CARDS = 65f * Settings.scale;
	public static final float CARD_AREA_COLUMN_WIDTH = 100f * Settings.scale;
	public static final float CARD_AREA_COLUMN_SPACER = 10f * Settings.scale;
	public static final float CARD_AREA_COLUMN_HEIGH = 280f * Settings.scale;

	private float vfxTimer = 1.0f;
	private float vfxIntervalMin = 0.33f;
	private float vfxIntervalMax = 0.88f;
	
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
			
			this.hb = new Hitbox(38.0f * Settings.scale, 65.0f * Settings.scale);
			this.cX = caster.drawX + caster.hb_x;
			this.cY = caster.drawY + caster.hb_y + caster.hb_h / 2.0f;
			
			logger.info("Delayed card created and centered around character");
		}
	}
	
	public static void redrawMiniCards() {
		if (AbstractDungeon.player instanceof TheCaster) {
			TheCaster caster = (TheCaster) AbstractDungeon.player;
			for (int turnsRemaining = 1 ; turnsRemaining < 4; turnsRemaining++) {
				int columnIndex = 0;
				for (DelayedCardEffect card : caster.delayedCards) {
					if (card.turnsUntilFire == turnsRemaining) {
						card.setSlot(turnsRemaining, columnIndex);
						columnIndex++;
					}
				}
			}
			int columnIndex = 0;
			for (DelayedCardEffect card : caster.delayedCards) {
				if (card.turnsUntilFire > 3) {
					card.setSlot(4, columnIndex);
					columnIndex++;
				}
			}
		}
	}
	
	public void removeFromPlayer() {
		if (AbstractDungeon.player instanceof TheCaster) {
			TheCaster caster = (TheCaster) AbstractDungeon.player;
			
			caster.delayedCards.remove(this);
			DelayedCardEffect.redrawMiniCards();
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
	public void applyFocus() {
		//Not affected by focus
	}

	@Override
	public void onStartOfTurn() {
		turnsUntilFire--;
		passiveAmount = turnsUntilFire;
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
	
	public boolean renderPreviewIfHovered(SpriteBatch sb) {
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
    public void setSlot(final int columnNumber, int indexInColumn) {
        final float rightBorder = AbstractDungeon.player.drawX + CARD_AREA_X_RIGHT_OFFSET;
        float columnXOffset = 0;
        if (indexInColumn >= 5) {
        	columnXOffset = CARD_AREA_COLUMN_WIDTH/2f;
        	indexInColumn %= 5;
        }
        this.tX = rightBorder - (CARD_AREA_COLUMN_WIDTH + CARD_AREA_COLUMN_SPACER) * (columnNumber-1) - columnXOffset;
        this.tY = AbstractDungeon.player.drawY + AbstractDungeon.player.hb_h + 20f + VERT_SPACE_BTWN_CARDS * indexInColumn;
        this.hb.move(this.tX, this.tY);
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
