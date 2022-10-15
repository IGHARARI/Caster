package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

import sts.caster.delayedCards.CastingSpellCard;

public class DelayedCardOnStartOfTurnTriggerAction extends AbstractGameAction {

	CastingSpellCard delayedCard;
	
	public DelayedCardOnStartOfTurnTriggerAction(CastingSpellCard delayedCard) {
        actionType = ActionType.SPECIAL;
        this.delayedCard = delayedCard;
        this.duration = Settings.ACTION_DUR_XFAST;
	}

	@Override
    public void update() {
		if (duration == Settings.ACTION_DUR_XFAST) {
			if (delayedCard != null) {
				delayedCard.onStartOfTurn();
			}
		}
        tickDuration();
    }
}
