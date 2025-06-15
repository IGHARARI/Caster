package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

public class ShowCardVeryBrieflyAction extends AbstractGameAction {

	private final boolean showInLocation;
	private final ShowCardBrieflyEffect effect;
	private AbstractCard showCopy;
	private float scale;

	public ShowCardVeryBrieflyAction(AbstractCard cardToShow, float dur, float scale, boolean showInLocation) {
		this.showCopy = cardToShow.makeStatEquivalentCopy();
		actionType = ActionType.SPECIAL;
		this.duration = dur;
		this.scale = scale;
		this.showInLocation = showInLocation;
		this.effect = new ShowCardBrieflyEffect(showCopy);
		effect.duration = this.duration;
		effect.startingDuration = this.duration;
		showCopy.targetDrawScale = scale;
		if (showInLocation) {
			showCopy.target_x = cardToShow.current_x;
			showCopy.target_y = cardToShow.current_y;
		}
	}

	public ShowCardVeryBrieflyAction(AbstractCard cardToShow) {
		this(cardToShow, 0.8f, 0.75f, false);
	}

	@Override
	public void update() {
		AbstractDungeon.topLevelEffects.add(effect);
		isDone = true;
	}
}
