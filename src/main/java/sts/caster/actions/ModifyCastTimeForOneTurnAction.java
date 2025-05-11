package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

import sts.caster.cards.CasterCard;

public class ModifyCastTimeForOneTurnAction extends AbstractGameAction {
    
    private int modifyAmount;
    private CasterCard card;
	
    public ModifyCastTimeForOneTurnAction(CasterCard card, int modifyAmount) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.modifyAmount = modifyAmount;
        this.card = card;
    }
    
    @Override
    public void update() {
    	if (!isDone) {
    		if (card.delayTurns > 0) {
    			card.delayTurnsModificationForTurn += modifyAmount;
    			card.isDelayTurnsModified = true;
    			card.flash();
    			card.initializeDescription();
    			card.applyPowers();
    		}
    	}
    	isDone = true;
    }
}
