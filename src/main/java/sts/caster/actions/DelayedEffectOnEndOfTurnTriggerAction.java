package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import sts.caster.delayedCards.CastingSpellCard;

public class DelayedEffectOnEndOfTurnTriggerAction extends AbstractGameAction {

	CastingSpellCard delayedCard;

	public DelayedEffectOnEndOfTurnTriggerAction(CastingSpellCard delayedCard) {
        actionType = ActionType.SPECIAL;
        this.delayedCard = delayedCard;
	}

	@Override
    public void update() {
    	if (!isDone) {
			if (delayedCard != null) {
				delayedCard.onEndOfTurn();
			}
    	}
        isDone = true;
    }
}
