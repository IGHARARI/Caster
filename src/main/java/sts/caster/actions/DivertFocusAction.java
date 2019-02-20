package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.characters.TheCaster;
import sts.caster.delayedCards.DelayedCardEffect;

public class DivertFocusAction extends AbstractGameAction {
    
    private int modifyAmount;
	
    public DivertFocusAction(int modifyAmount) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.modifyAmount = modifyAmount;
    }
    
    @Override
    public void update() {
    	if (!isDone) {
    		if (AbstractDungeon.player instanceof TheCaster) {
    			TheCaster p = (TheCaster) AbstractDungeon.player;
    			if (p.delayedCards.isEmpty()) {
    				isDone = true;
    				return;
    			}
    			DelayedCardEffect nextCard = p.delayedCards.get(0);
    			for (DelayedCardEffect card : p.delayedCards) {
    				if (card.turnsUntilFire <= nextCard.turnsUntilFire) nextCard = card;
    			}
    			nextCard.turnsUntilFire += modifyAmount;
    		}
    	}
    	isDone = true;
    }
}
