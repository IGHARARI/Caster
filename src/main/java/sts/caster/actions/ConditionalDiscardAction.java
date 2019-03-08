package sts.caster.actions;

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
    		for (AbstractCard c : AbstractDungeon.player.hand.group) {
    			if (shouldDiscardPredicate.test(c)) {
    				AbstractDungeon.player.hand.moveToDiscardPile(c);
    			}
    		}
    	}
        isDone = true;
    }
}
