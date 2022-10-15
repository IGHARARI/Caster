package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import sts.caster.delayedCards.CastingSpellCard;
import sts.caster.delayedCards.SpellCardsArea;

public class RemoveEvokingSpellCard extends AbstractGameAction {

	CastingSpellCard delayedCard;
	
	public RemoveEvokingSpellCard(CastingSpellCard delayedCard) {
        actionType = ActionType.SPECIAL;
        this.delayedCard = delayedCard;
	}

	@Override
    public void update() {
    	if (!isDone) {
			if (delayedCard != null) {
				SpellCardsArea.cardsBeingEvoked.remove(delayedCard);
//				delayedCard.showEvokeCardOnScreen = false;
			}
    	}
        isDone = true;
    }
}
