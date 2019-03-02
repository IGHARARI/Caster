package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import sts.caster.delayedCards.DelayedCardEffect;

public class QueueEvokeCardAction extends AbstractGameAction {
	DelayedCardEffect card;
	
	public QueueEvokeCardAction(DelayedCardEffect card) {
        actionType = ActionType.SPECIAL;
        this.card = card;
	}

	@Override
    public void update() {
    	if (!isDone) {
    		card.evokeCardEffect();
    	}
        isDone = true;
    }
}
