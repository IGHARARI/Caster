package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class ModifyCardDamageAction extends AbstractGameAction {

	AbstractCard card;
	
	public ModifyCardDamageAction(AbstractCard card, int reduceAmount) {
        actionType = ActionType.DAMAGE;
        this.card = card;
        this.amount = reduceAmount;
	}

	@Override
    public void update() {
    	if (!isDone) {
    		card.baseDamage = Math.max(0, card.baseDamage - amount);
    		card.applyPowers();
    	}
        isDone = true;
    }
}
