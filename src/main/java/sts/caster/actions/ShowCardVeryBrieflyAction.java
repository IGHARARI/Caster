package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

public class ShowCardVeryBrieflyAction extends AbstractGameAction {

	private ShowCardBrieflyEffect effect;
	private AbstractCard showCopy;

	private boolean isOnScreen(AbstractCard c) {
		return (c.current_y >= -200.0F * Settings.scale && c.current_y <= Settings.HEIGHT + 200.0F * Settings.scale);
	}

	public ShowCardVeryBrieflyAction(AbstractCard cardToShow, float dur, float scale, boolean showInLocation) {
		if (!isOnScreen(cardToShow)) {
			this.isDone = true;
			return;
		}
		this.showCopy = cardToShow.makeStatEquivalentCopy();
		actionType = ActionType.SPECIAL;
		this.effect = new ShowCardBrieflyEffect(showCopy);
		effect.duration = dur;
		effect.startingDuration = dur;
		showCopy.drawScale = cardToShow.drawScale;
		showCopy.targetDrawScale = Math.max(0.5f, showCopy.drawScale * scale);
//		showCopy.targetTransparency = 0.2f;
		if (showInLocation) {
			showCopy.target_x = cardToShow.current_x;
			showCopy.target_y = cardToShow.current_y;
			showCopy.current_x = cardToShow.current_x;
			showCopy.current_y = cardToShow.current_y;
			showCopy.angle = cardToShow.angle;
			showCopy.targetAngle = cardToShow.angle;
		}
	}

	public ShowCardVeryBrieflyAction(AbstractCard cardToShow) {
		this(cardToShow, 1.0f, 1.2f, true);
	}

	@Override
	public void update() {
		AbstractDungeon.topLevelEffects.add(effect);
		CardCrawlGame.sound.playAV("ORB_LIGHTNING_CHANNEL", 1f, 2f);
		isDone = true;
	}
}
