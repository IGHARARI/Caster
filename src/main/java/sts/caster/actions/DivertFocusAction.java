package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.delayedCards.DelayedCardEffect;
import sts.caster.delayedCards.DelayedCardsArea;

public class DivertFocusAction extends AbstractGameAction {
    
    private int modifyAmount;
	
    public DivertFocusAction(int modifyAmount) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
        this.modifyAmount = modifyAmount;
    }
    
    @Override
    public void update() {
    	if (!isDone) {
			if (DelayedCardsArea.delayedCards == null || DelayedCardsArea.delayedCards.isEmpty()) {
				isDone = true;
				return;
			}
			DelayedCardEffect nextCard = DelayedCardsArea.delayedCards.get(0);
			for (DelayedCardEffect card : DelayedCardsArea.delayedCards) {
				if (card.turnsUntilFire < nextCard.turnsUntilFire) nextCard = card;
			}
			nextCard.turnsUntilFire += modifyAmount;
			nextCard.passiveAmount = nextCard.turnsUntilFire;
			AbstractDungeon.actionManager.addToBottom(new QueueRedrawMiniCardsAction());
    	}
    	isDone = true;
    }
}
