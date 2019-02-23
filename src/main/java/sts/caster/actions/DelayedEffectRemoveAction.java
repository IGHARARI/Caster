package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import sts.caster.delayedCards.DelayedCardEffect;
import sts.caster.delayedCards.DelayedCardsArea;

public class DelayedEffectRemoveAction extends AbstractGameAction {

	DelayedCardEffect delayedCard;
	
	public DelayedEffectRemoveAction(DelayedCardEffect delayedCard) {
        actionType = ActionType.SPECIAL;
        this.delayedCard = delayedCard;
	}

	@Override
    public void update() {
    	if (!isDone) {
			if (delayedCard != null && delayedCard.turnsUntilFire <= 0) {
				DelayedCardsArea.removeCardFromArea(delayedCard);
			}
    	}
        isDone = true;
    }
}
