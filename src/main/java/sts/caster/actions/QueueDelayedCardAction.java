package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.caster.cards.CasterCard;
import sts.caster.delayedCards.DelayedCardEffect;
import sts.caster.delayedCards.DelayedCardsArea;

public class QueueDelayedCardAction extends AbstractGameAction {
    private CasterCard card;
    private int turnsDelay;
	private Integer energyOnUse;
	AbstractMonster target;
	
    public QueueDelayedCardAction(final CasterCard card, final int turnsDelay, AbstractMonster target) {
    	this(card, turnsDelay, null, target);
    }
    
    public QueueDelayedCardAction(final CasterCard card, final int turnsDelay, Integer energyOnUse, AbstractMonster target) {
    	this.card = card.makeStatIdenticalCopy();
    	this.turnsDelay = turnsDelay;
    	actionType = ActionType.SPECIAL;
    	this.energyOnUse = energyOnUse;
    	this.target = target;
    }

	@Override
    public void update() {
		DelayedCardEffect delayedCard = new DelayedCardEffect(card, turnsDelay, energyOnUse, target);
		DelayedCardsArea.addCardToArea(delayedCard);
		DelayedCardsArea.repositionMiniCards();
        isDone = true;
    }
}
