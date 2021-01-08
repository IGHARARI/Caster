package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import sts.caster.delayedCards.DelayedCardEffect;
import sts.caster.delayedCards.DelayedCardsArea;

public class DelayedEffectHideEvokedCard extends AbstractGameAction {

	DelayedCardEffect delayedCard;
	
	public DelayedEffectHideEvokedCard(DelayedCardEffect delayedCard) {
        actionType = ActionType.SPECIAL;
        this.delayedCard = delayedCard;
	}

	@Override
    public void update() {
    	if (!isDone) {
			if (delayedCard != null) {
				System.out.println("Removing from EVOKE area: " + delayedCard.ID);
				DelayedCardsArea.evokingCards.remove(delayedCard);
				delayedCard.showEvokeCardOnScreen = false;
			}
    	}
        isDone = true;
    }
}
