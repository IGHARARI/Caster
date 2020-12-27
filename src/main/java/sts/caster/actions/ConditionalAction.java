package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public class ConditionalAction extends AbstractGameAction {

	BooleanSupplier shouldCompleteAction;
	List<AbstractGameAction> targetActions;

	public ConditionalAction(BooleanSupplier shouldCompleteAction, List<AbstractGameAction> targetActions) {
        actionType = ActionType.CARD_MANIPULATION;
        this.shouldCompleteAction = shouldCompleteAction;
        this.targetActions = targetActions;
	}

	@Override
    public void update() {
    	if (!isDone) {
			if (shouldCompleteAction.getAsBoolean()){
				for (AbstractGameAction a : targetActions) {
					addToTop(a);
				}
			}
    	}
        isDone = true;
    }
}
