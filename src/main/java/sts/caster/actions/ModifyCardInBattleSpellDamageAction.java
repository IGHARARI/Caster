package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;

import sts.caster.cards.CasterCard;

public class ModifyCardInBattleSpellDamageAction extends AbstractGameAction {

	CasterCard card;
	
	public ModifyCardInBattleSpellDamageAction(CasterCard card, int modifyAmount) {
        actionType = ActionType.CARD_MANIPULATION;
        this.card = card;
        this.amount = modifyAmount;
	}

	@Override
    public void update() {
        for (final AbstractCard abstractCard : GetAllInBattleInstances.get(card.uuid)) {
        	increaseSpellDamage(abstractCard);
        }
        this.isDone = true;
    }

	private void increaseSpellDamage(AbstractCard c) {
    	if (!(c instanceof CasterCard)) return;
    	CasterCard castrcard = (CasterCard) c;
    	castrcard.baseSpellDamage = Math.max(0, castrcard.baseSpellDamage + amount);
	}
}
