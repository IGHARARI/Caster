package sts.caster.actions;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import sts.caster.cards.CasterCard;
import sts.caster.delayedCards.DelayedCardEffect;
import sts.caster.delayedCards.DelayedCardsArea;

public class QueueDelayedCardAction extends AbstractGameAction {
    private CasterCard card;
    private int turnsDelay;
    private ArrayList<AbstractGameAction> actions;
	private int energyOnUse;
	AbstractCreature target;
	
	public QueueDelayedCardAction(final CasterCard card, final int turnsDelay, AbstractGameAction action, AbstractCreature target) {
		this(card, turnsDelay, action, 0, target);
	}
	
	public QueueDelayedCardAction(final CasterCard card, final int turnsDelay, AbstractGameAction action, int energyOnUse, AbstractCreature target) {
		this(card, turnsDelay, new ArrayList<AbstractGameAction>(), energyOnUse, target);
		this.actions.add(action);
	}

    public QueueDelayedCardAction(final CasterCard card, final int turnsDelay, ArrayList<AbstractGameAction> actions, AbstractCreature target) {
    	this(card, turnsDelay, actions, 0, target);
    }
    
    public QueueDelayedCardAction(final CasterCard card, final int turnsDelay, ArrayList<AbstractGameAction> actions, int energyOnUse, AbstractCreature target) {
    	this.card = card;
    	this.turnsDelay = turnsDelay;
    	this.actions = actions;
    	actionType = ActionType.SPECIAL;
    	this.energyOnUse = energyOnUse;
    	this.target = target;
    }

	@Override
    public void update() {
		DelayedCardEffect delayedCard = new DelayedCardEffect(card, turnsDelay, actions, energyOnUse, target);
		DelayedCardsArea.addCardToArea(delayedCard);
		DelayedCardsArea.repositionMiniCards();
        isDone = true;
    }
}
