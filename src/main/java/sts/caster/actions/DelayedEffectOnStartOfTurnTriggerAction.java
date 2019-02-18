package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import sts.caster.delayedCards.DelayedCardEffect;

public class DelayedEffectOnStartOfTurnTriggerAction extends AbstractGameAction {

	DelayedCardEffect delayedCard;
	
	public DelayedEffectOnStartOfTurnTriggerAction(DelayedCardEffect delayedCard) {
        actionType = ActionType.SPECIAL;
        this.delayedCard = delayedCard;
	}

	@Override
    public void update() {
    	if (!isDone) {
			if (delayedCard != null) {
				delayedCard.onStartOfTurn();
			}
    	}
        isDone = true;
    }
}
