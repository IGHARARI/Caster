package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import sts.caster.delayedCards.DelayedCardEffect;

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
				delayedCard.showEvokeCardOnScreen = false;
			}
    	}
        isDone = true;
    }
}
