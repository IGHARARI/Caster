package sts.caster.delayedCards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class SpellPredictionIntent extends AbstractOrb {

	public AbstractMonster target;
	public int intentAmount;
	public AbstractCreature hoverTarget;
	public SpellIntentType spellIntentType;
	private Texture intentIcon;
	public enum SpellIntentType {
		ATTACK,
		ATTACK_ALL,
		BLOCK
	}

	// Default to player intent, besides the spells table
	public SpellPredictionIntent(int intentAmount, SpellIntentType spellIntentType) {
		this(intentAmount, AbstractDungeon.player, spellIntentType);
	}

	public SpellPredictionIntent(int intentAmount, AbstractCreature hoverTarget, SpellIntentType spellIntentType) {
        super();
		this.intentAmount = intentAmount;
		this.hoverTarget = hoverTarget;
		this.spellIntentType = spellIntentType;

		float offsetX = spellIntentType == SpellIntentType.ATTACK ?  0 : 300.0f * Settings.scale;
		float offsetY = 100.0f * Settings.scale;
		if (spellIntentType == SpellIntentType.BLOCK) offsetY = 200.0f * Settings.scale;

//		AbstractPlayer p = AbstractDungeon.player;
        cX = hoverTarget.drawX + hoverTarget.hb_x + offsetX;
		cY = hoverTarget.drawY + offsetY + hoverTarget.hb_y + hoverTarget.hb_h / 2.0f;
		tX = cX;
		tY = cY;
		hb = new Hitbox(65.0f * Settings.scale, 65.0f * Settings.scale);
		hb.move(cX, cY);
		if (spellIntentType == SpellIntentType.ATTACK) {
			this.intentIcon = getAttackIntent(intentAmount);
		} else if (spellIntentType == SpellIntentType.BLOCK) {
			this.intentIcon = ImageMaster.INTENT_DEFEND_L;
		} else if (spellIntentType == SpellIntentType.ATTACK_ALL) {
			this.intentIcon = ImageMaster.INTENT_MAGIC_L;
		}
	}

	@Override
	public void updateDescription() {}

	@Override
	public void applyFocus() {}

	@Override
	public void update() {
        this.hb.update();
		TipHelper.renderGenericTip(this.tX + 96.0f * Settings.scale, this.tY + 64.0f * Settings.scale, String.valueOf(this.spellIntentType),
				"Will deal damage: " + intentAmount);
        this.fontScale = MathHelper.scaleLerpSnap(this.fontScale, 0.7f);
	}
	
	@Override
	public void onStartOfTurn() {

	}


	@Override
	public void updateAnimation() {
//		super.updateAnimation();
//		cX = MathHelper.orbLerpSnap(cX, tX);
//		cY = MathHelper.orbLerpSnap(cY, this.tY + (bobEffect.y / 2.0F));
		bobEffect.update();
		hb.update();
		if (channelAnimTimer != 0.0F) {
			channelAnimTimer -= Gdx.graphics.getDeltaTime();
			if (channelAnimTimer < 0.0F) {
				channelAnimTimer = 0.0F;
			}
		}

		c.a = Interpolation.pow2In.apply(1.0F, 0.01F, channelAnimTimer / 0.5F);
	}

	// Render the card.
	@Override
	public void render(SpriteBatch sb) {
		hb.render(sb);
		drawIcon(sb, intentIcon);
		FontHelper.renderFontCentered(sb,
				FontHelper.cardEnergyFont_L,
				String.valueOf(intentAmount),
				cX + NUM_X_OFFSET,
				cY + NUM_Y_OFFSET + bobEffect.y,
				c,
				fontScale
		);
	}

	private void drawIcon(SpriteBatch sb, Texture icon) {
		sb.setColor(c);
		sb.draw(icon,
				cX - 64.0F,
				cY - 64.0F,
				64.0F,
				64.0F,
				128.0F,
				128.0F,
				Settings.scale,
				Settings.scale,
				0,
				0,
				0,
				128,
				128,
				false,
				false);
	}

	private Texture getAttackIntent(int spellDamage) {
		if (spellDamage < 5) {
			return ImageMaster.INTENT_ATK_1;
		} else if (spellDamage < 10) {
			return ImageMaster.INTENT_ATK_2;
		} else if (spellDamage < 15) {
			return ImageMaster.INTENT_ATK_3;
		} else if (spellDamage < 20) {
			return ImageMaster.INTENT_ATK_4;
		} else if (spellDamage < 25) {
			return ImageMaster.INTENT_ATK_5;
		} else {
			return spellDamage < 30 ? ImageMaster.INTENT_ATK_6 : ImageMaster.INTENT_ATK_7;
		}
	}

	public boolean renderReticleIfHovered(SpriteBatch sb) {
		if (hb.hovered) {
			switch (spellIntentType) {
				case ATTACK: {
					if (hoverTarget != null && !hoverTarget.isDeadOrEscaped()) {
						hoverTarget.renderReticle(sb);
					}
					break;
				}
				case ATTACK_ALL: {
					AbstractDungeon.getCurrRoom().monsters.renderReticle(sb);
					break;
				}
				case BLOCK: {
					AbstractDungeon.player.renderReticle(sb);
					break;
				}
				default:
					break;
			}
		}
		return hb.hovered;
	}
	@Override
	public void triggerEvokeAnimation() {}

	@Override
	public void playChannelSFX() {
		CardCrawlGame.sound.play("ATTACK_FIRE", 0.1f);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new SpellPredictionIntent(this.intentAmount, this.hoverTarget, this.spellIntentType);
	}

	@Override
	public void onEvoke() {}
}
