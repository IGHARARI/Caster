package sts.caster.actions;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import sts.caster.delayedCards.DelayedCardEffect;
import sts.caster.delayedCards.DelayedCardsArea;

public class QueueDelayedCardAction extends AbstractGameAction {
    private AbstractCard card;
    private int turnsDelay;
    private ArrayList<AbstractGameAction> actions;

    public QueueDelayedCardAction(final AbstractCard card, final int turnsDelay, ArrayList<AbstractGameAction> actions) {
        this.card = card;
        this.turnsDelay = turnsDelay;
        this.actions = actions;
        actionType = ActionType.SPECIAL;
    }

    public QueueDelayedCardAction(final AbstractCard card, final int turnsDelay, AbstractGameAction action) {
        this.card = card;
        this.turnsDelay = turnsDelay;
        this.actions = new ArrayList<AbstractGameAction>();
        this.actions.add(action);
        actionType = ActionType.SPECIAL;
	}

	@Override
    public void update() {
		DelayedCardEffect delayedCard = new DelayedCardEffect(card, turnsDelay, actions);
		DelayedCardsArea.addCardToArea(delayedCard);
		DelayedCardsArea.repositionMiniCards();
        isDone = true;
    }
}
