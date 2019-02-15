package sts.caster.actions;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.characters.TheCaster;
import sts.caster.delayedCards.DelayedCardEffect;

public class CastDelayedCardAction extends AbstractGameAction {
    private AbstractCard card;
    private int turnsDelay;
    private ArrayList<AbstractGameAction> actions;

    public CastDelayedCardAction(final AbstractCard card, final int turnsDelay, ArrayList<AbstractGameAction> actions)

    {
        this.card = card;
        this.turnsDelay = turnsDelay;
        this.actions = actions;
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
    	if (AbstractDungeon.player instanceof TheCaster) {
    		TheCaster caster = (TheCaster) AbstractDungeon.player;
    		caster.delayedCards.add(new DelayedCardEffect(card, turnsDelay, actions));
    	}
        isDone = true;
    }
}
