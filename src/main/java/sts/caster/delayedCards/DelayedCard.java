package sts.caster.delayedCards;

import java.lang.reflect.Field;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbPassiveEffect;

import sts.caster.characters.TheCaster;

public class DelayedCard extends AbstractOrb {
	
	int turnsUntilFire;

	private static final Logger logger = LogManager.getLogger(DelayedCard.class.getName());

	// Standard ID/Description
	public static final String ORB_ID = "DelayedCard:";

	// Animation Rendering Numbers - You can leave these at default, or play around with them and see what they change.
	private float vfxTimer = 1.0f;
	private float vfxIntervalMin = 0.1f;
	private float vfxIntervalMax = 0.4f;
	private static final float ORB_WAVY_DIST = 0.04f;
	private static final float PI_4 = 12.566371f;
	
	public AbstractCard delayedCard = null;
	
	public DelayedCard(AbstractCard card, int delayTurns) {
		super();
		logger.info("Creating delayed card");
		if (AbstractDungeon.player instanceof TheCaster) {
			TheCaster caster = (TheCaster) AbstractDungeon.player;
			ID = ORB_ID + card.uuid;
			name = "ORB: " + card.name;
			img = ImageMaster.loadImage("orbs/default_orb.png");
			
			this.delayedCard = card;
			turnsUntilFire = delayTurns;
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
					DelayedCard.logger.info("KEY: " + key);
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
	public void onEvoke() {
//		logger.info("On evoke for delayed card");
		//The dontTriggerOnUseCard is to prevent interactions with
		//relics, powers, and cards that happen when you to play a card
		AbstractMonster monster = AbstractDungeon.getRandomMonster();
		delayedCard.calculateCardDamage(monster);
		delayedCard.use(AbstractDungeon.player, AbstractDungeon.getRandomMonster());
		
		//TODO: Make multi-casting not duplicate card
		AbstractDungeon.player.discardPile.addToBottom(delayedCard);
	}

	@Override
	public void onStartOfTurn() {
		//No passive effect
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
	public void triggerEvokeAnimation() { // The evoke animation of this orb is the dark-orb activation effect.
		AbstractDungeon.effectsQueue.add(new DarkOrbActivateEffect(this.cX, this.cY));
	}

	@Override
	public void playChannelSFX() { // When you channel this orb, the ATTACK_FIRE effect plays ("Fwoom").
		CardCrawlGame.sound.play("ATTACK_FIRE", 0.1f);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new DelayedCard(this.delayedCard, turnsUntilFire);
	}
}
