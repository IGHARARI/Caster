package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import sts.caster.cards.CasterCard;

@Deprecated
public class CardOnFrozenTriggerAction extends AbstractGameAction {

	CasterCard card;
	
	public CardOnFrozenTriggerAction(CasterCard card) {
        actionType = ActionType.CARD_MANIPULATION;
        this.card = card;
	}

	@Override
    public void update() {
		card.onFrozen();
        isDone = true;
    }
}
