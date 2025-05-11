package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import sts.caster.delayedCards.CastingSpellCard;
import sts.caster.delayedCards.SpellCardsArea;

public class DelayedEffectRemoveAction extends AbstractGameAction {

	CastingSpellCard delayedCard;
	
	public DelayedEffectRemoveAction(CastingSpellCard delayedCard) {
        actionType = ActionType.SPECIAL;
        this.delayedCard = delayedCard;
	}

	@Override
    public void update() {
    	if (!isDone) {
			if (delayedCard != null && delayedCard.turnsUntilFire <= 0) {
				SpellCardsArea.removeCardFromArea(delayedCard);
			}
    	}
        isDone = true;
    }
}
