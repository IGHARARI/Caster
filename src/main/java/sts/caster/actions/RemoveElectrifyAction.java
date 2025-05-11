package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import sts.caster.cards.CasterCardTags;

public class RemoveElectrifyAction extends AbstractGameAction {
	AbstractCard card;
	Integer amount;

	public RemoveElectrifyAction(AbstractCard card) {
		actionType = ActionType.SPECIAL;
		this.card = card;
	}

	public RemoveElectrifyAction(AbstractCard card, int stacksToRemove) {
        actionType = ActionType.SPECIAL;
        this.card = card;
        this.amount = stacksToRemove;
	}

	@Override
    public void update() {
		card.flash();
		if (amount != null) {
			for (int i = 0; i < amount; i++) {
				if (card.tags.contains(CasterCardTags.ELECTRIFIED))
					card.tags.remove(CasterCardTags.ELECTRIFIED);
			}
		} else {
			card.tags.removeIf((tag) -> tag == CasterCardTags.ELECTRIFIED);
		}
		isDone = true;
    }
}
