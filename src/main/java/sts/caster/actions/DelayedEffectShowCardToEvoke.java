package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

import sts.caster.delayedCards.DelayedCardEffect;
import sts.caster.delayedCards.DelayedCardsArea;

public class DelayedEffectShowCardToEvoke extends AbstractGameAction {

	DelayedCardEffect delayedCard;
	
	public DelayedEffectShowCardToEvoke(DelayedCardEffect delayedCard) {
        actionType = ActionType.SPECIAL;
        this.delayedCard = delayedCard;
        this.duration = Settings.ACTION_DUR_FAST;
	}

	@Override
    public void update() {
		if (delayedCard != null) {
			if (duration == Settings.ACTION_DUR_FAST) {
//				delayedCard.cardEvokeCopy.calculateCardDamage(delayedCard.target);
//				delayedCard.showEvokeCardOnScreen = true;
			}
		}
        tickDuration();
    }
}
