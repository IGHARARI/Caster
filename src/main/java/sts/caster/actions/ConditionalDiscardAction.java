package sts.caster.actions;

import java.util.ArrayList;
import java.util.function.Predicate;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ConditionalDiscardAction extends AbstractGameAction {

	Predicate<AbstractCard> shouldDiscardPredicate;
	
	public ConditionalDiscardAction(Predicate<AbstractCard> shouldDiscardPredicate) {
        actionType = ActionType.CARD_MANIPULATION;
        this.shouldDiscardPredicate = shouldDiscardPredicate;
	}

	@Override
    public void update() {
    	if (!isDone) {
    		ArrayList<AbstractCard> cardsToDiscard = new ArrayList<AbstractCard>();
    		for (AbstractCard c : AbstractDungeon.player.hand.group) {
    			if (shouldDiscardPredicate.test(c)) {
    				cardsToDiscard.add(c);
    			}
    		}
    		for (AbstractCard c : cardsToDiscard) {
    			AbstractDungeon.player.hand.moveToDiscardPile(c);
    		}
    	}
        isDone = true;
    }
}
