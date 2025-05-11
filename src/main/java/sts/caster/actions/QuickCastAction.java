package sts.caster.actions;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.cards.CasterCard;
import sts.caster.patches.spellCardType.CasterCardType;

public class QuickCastAction extends AbstractGameAction {
    
    private AbstractPlayer p;
	private int reduceAmount;
	private boolean canSelect;
	private ArrayList<AbstractCard> notDelayed;
	
    public QuickCastAction(int reduceAmount, boolean canSelect) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        notDelayed = new ArrayList<AbstractCard>();
        this.duration = Settings.ACTION_DUR_FAST;
        this.reduceAmount = reduceAmount;
        this.canSelect = canSelect;
    }
    
    @Override
    public void update() {
    	if (canSelect) {
    		if (this.duration == Settings.ACTION_DUR_FAST) {
    			
                for (final AbstractCard c : this.p.hand.group) {
                    if (!hasDelay(c)) {
                        notDelayed.add(c);
                    }
                }
                if (notDelayed.size() == p.hand.group.size()) {
                	isDone = true;
                	return;
                }
                if (p.hand.group.size() - notDelayed.size() == 1) {
                	for (final AbstractCard c : this.p.hand.group) {
                		if (hasDelay(c)) {
                			addToBot(new ModifyCastTimeForOneTurnAction((CasterCard)c, -reduceAmount));
                			isDone = true;
                			return;
                		}
                	}
                }
                p.hand.group.removeAll(notDelayed);
                AbstractDungeon.handCardSelectScreen.open("", 1, false, true);
                tickDuration();
                return;
    		} else {
    			if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
    				for (final AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
    					if (c instanceof CasterCard) {
    						CasterCard casterCard = (CasterCard) c;
    						addToBot(new ModifyCastTimeForOneTurnAction(casterCard, -reduceAmount));
    						p.hand.addToTop(c);
    					}
    				}
    		        for (final AbstractCard c : notDelayed) {
    		            p.hand.addToTop(c);
    		        }
    		        this.p.hand.refreshHandLayout();
    				AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
    				AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
    			}
    			this.tickDuration();
    			return;
    		}
    	} else if (this.duration == Settings.ACTION_DUR_FAST) {
			ArrayList<AbstractCard> delayedCards = new ArrayList<AbstractCard>();
			for (AbstractCard c : p.hand.group) {
				if (c.type == CasterCardType.SPELL && hasDelay(c)) delayedCards.add(c);
			}
			if (delayedCards.size() > 0) {
				AbstractCard card = delayedCards.get(AbstractDungeon.cardRandomRng.random(delayedCards.size()-1));
				if (card instanceof CasterCard) addToBot(new ModifyCastTimeForOneTurnAction((CasterCard)card, -reduceAmount));
			}    		
    	}
		if (this.p.hand.isEmpty()) {
			this.isDone = true;
			return;
		}
    	this.tickDuration();
	}

	private boolean hasDelay(AbstractCard c) {
		return (c instanceof CasterCard) && ((CasterCard)c).delayTurns > 0;
	}
}
