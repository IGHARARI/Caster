package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import sts.caster.interfaces.OnRecurringSpell;

public class CardOnAfterRecurringTriggerAction extends AbstractGameAction {

    OnRecurringSpell card;

	public CardOnAfterRecurringTriggerAction(OnRecurringSpell card) {
        actionType = ActionType.CARD_MANIPULATION;
        this.card = card;
	}

	@Override
    public void update() {
		card.onAfterRecurring();
        isDone = true;
    }
}
