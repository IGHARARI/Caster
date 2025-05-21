package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import sts.caster.cards.CasterCard;

public class CardOnThawTriggerAction extends AbstractGameAction {

	CasterCard card;

	public CardOnThawTriggerAction(CasterCard card) {
        actionType = ActionType.CARD_MANIPULATION;
        this.card = card;
	}

	@Override
    public void update() {
		card.onThaw();
        isDone = true;
    }
}
