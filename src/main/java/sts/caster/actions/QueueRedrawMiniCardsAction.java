package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.delayedCards.DelayedCardEffect;

public class QueueRedrawMiniCardsAction extends AbstractGameAction {
	private boolean delayed;
	
	public QueueRedrawMiniCardsAction() {
		this(true);
	}

	private QueueRedrawMiniCardsAction(boolean delayed) {
		actionType = ActionType.SPECIAL;
		this.delayed= delayed;
	}

	@Override
    public void update() {
    	if (!isDone) {
    		if(delayed) {
    			AbstractDungeon.actionManager.addToBottom(new QueueRedrawMiniCardsAction(false));
    		} else {
    			DelayedCardEffect.redrawMiniCards();
    		}
    	}
        isDone = true;
    }
}
