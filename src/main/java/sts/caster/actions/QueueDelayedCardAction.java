package sts.caster.actions;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.characters.TheCaster;
import sts.caster.delayedCards.DelayedCardEffect;

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
    	if (AbstractDungeon.player instanceof TheCaster) {
    		TheCaster caster = (TheCaster) AbstractDungeon.player;
    		caster.delayedCards.add(new DelayedCardEffect(card, turnsDelay, actions));
    		DelayedCardEffect.redrawMiniCards();
    	}
        isDone = true;
    }
}
