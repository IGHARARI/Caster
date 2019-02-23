package sts.caster.actions;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.cards.CasterCard;
import sts.caster.core.TheCaster;

public class QuickCastAction extends AbstractGameAction {
    
    private AbstractPlayer p;
	private int reduceAmount;
	private boolean canSelect;
	
    public QuickCastAction(int reduceAmount, boolean canSelect) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.reduceAmount = reduceAmount;
        this.canSelect = canSelect;
    }
    
    @Override
    public void update() {
    	if (canSelect) {
    		if (this.duration != Settings.ACTION_DUR_FAST) {
    			if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
    				for (final AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
    					if (c instanceof CasterCard) {
    						CasterCard casterCard = (CasterCard) c;
    						AbstractDungeon.actionManager.addToBottom(new ModifyCastTimeAction(casterCard, reduceAmount));
    						p.hand.addToTop(c);
    					}
    				}
    				AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
    				AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
    			}
    			this.tickDuration();
    			return;
    		}
    		AbstractDungeon.handCardSelectScreen.open("", 1, false, true);
    	} else if (this.duration == Settings.ACTION_DUR_FAST) {
			ArrayList<AbstractCard> delayedCards = new ArrayList<AbstractCard>();
			for (AbstractCard c : p.hand.group) {
				if (c.hasTag(TheCaster.Enums.DELAYED_CARD)) delayedCards.add(c);
			}
			if (delayedCards.size() > 0) {
				AbstractCard card = delayedCards.get(AbstractDungeon.cardRandomRng.random(delayedCards.size()-1));
				if (card instanceof CasterCard) AbstractDungeon.actionManager.addToBottom(new ModifyCastTimeAction((CasterCard)card, reduceAmount));
			}    		
    	}
		if (this.p.hand.isEmpty()) {
			this.isDone = true;
			return;
		}
    	this.tickDuration();
	}
}
