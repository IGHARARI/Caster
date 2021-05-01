package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import sts.caster.delayedCards.SpellCardsArea;

public class QueueRedrawMiniCardsAction extends AbstractGameAction {
	private boolean delayed;
	
	public QueueRedrawMiniCardsAction() {
		this(true);
	}

	public QueueRedrawMiniCardsAction(boolean delayed) {
		actionType = ActionType.SPECIAL;
		this.delayed= delayed;
	}

	@Override
    public void update() {
    	if (!isDone) {
    		if(delayed) {
    			addToBot(new QueueRedrawMiniCardsAction(false));
    		} else {
    			SpellCardsArea.repositionMiniCards();
				SpellCardsArea.redrawEvokeCards();
    		}
    	}
        isDone = true;
    }
}
