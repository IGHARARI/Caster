package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import sts.caster.delayedCards.CastingSpellCard;

public class DelayedEffectOnStartOfTurnTriggerAction extends AbstractGameAction {

	CastingSpellCard delayedCard;
	
	public DelayedEffectOnStartOfTurnTriggerAction(CastingSpellCard delayedCard) {
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
