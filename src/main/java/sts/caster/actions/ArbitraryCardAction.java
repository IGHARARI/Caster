package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class ArbitraryCardAction extends AbstractGameAction {

	BooleanSupplier shouldCompleteAction;
	Consumer<AbstractCard> cardConsumer;
	AbstractCard cardTarget;

	public ArbitraryCardAction(Consumer<AbstractCard> cardConsumer) {
		this(null, () -> true, cardConsumer);
	}

	public ArbitraryCardAction(AbstractCard c, Consumer<AbstractCard> cardConsumer) {
		this(c, () -> true, cardConsumer);
	}

	public ArbitraryCardAction(AbstractCard c, BooleanSupplier shouldCompleteAction, Consumer<AbstractCard> cardConsumer) {
        actionType = ActionType.SPECIAL;
        cardTarget = c;
        this.shouldCompleteAction = shouldCompleteAction;
        this.cardConsumer = cardConsumer;
	}

	@Override
    public void update() {
    	if (!isDone) {
			if (shouldCompleteAction.getAsBoolean()){
				cardConsumer.accept(cardTarget);
			}
    	}
        isDone = true;
    }
}
