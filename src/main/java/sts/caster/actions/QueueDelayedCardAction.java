package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.caster.cards.CasterCard;
import sts.caster.delayedCards.CastingSpellCard;
import sts.caster.delayedCards.SpellCardsArea;

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
		CastingSpellCard delayedCard = new CastingSpellCard(card, turnsDelay, energyOnUse, target);
		SpellCardsArea.addCardToArea(delayedCard);
		SpellCardsArea.repositionMiniCards();
        isDone = true;
    }
}
