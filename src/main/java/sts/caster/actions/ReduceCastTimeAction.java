package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

import sts.caster.cards.CasterCard;

public class ReduceCastTimeAction extends AbstractGameAction {
    
    private int reduceAmount;
    private CasterCard card;
	
    public ReduceCastTimeAction(CasterCard card, int reduceAmount) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.reduceAmount = reduceAmount;
        this.card = card;
    }
    
    @Override
    public void update() {
    	if (!isDone) {
    		if (card.delayTurns > 0) {
    			card.delayTurns = Math.max(0, card.delayTurns - reduceAmount);
    			card.isDelayTurnsModified = true;
    			card.flash();
    			card.initializeDescription();
    			card.applyPowers();
    		}
    	}
    	isDone = true;
    }
}
