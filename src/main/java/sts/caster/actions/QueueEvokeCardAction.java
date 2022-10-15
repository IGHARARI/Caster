package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import sts.caster.delayedCards.CastingSpellCard;

public class QueueEvokeCardAction extends AbstractGameAction {
	CastingSpellCard card;
	
	public QueueEvokeCardAction(CastingSpellCard card) {
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
