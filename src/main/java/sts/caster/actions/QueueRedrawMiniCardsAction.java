package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.delayedCards.DelayedCardsArea;

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
    			DelayedCardsArea.repositionMiniCards();
				DelayedCardsArea.redrawEvokeCards();
    		}
    	}
        isDone = true;
    }
}
