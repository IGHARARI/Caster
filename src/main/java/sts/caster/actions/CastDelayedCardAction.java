package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.characters.TheCaster;
import sts.caster.delayedCards.DelayedCard;

public class CastDelayedCardAction extends AbstractGameAction {
    private AbstractCard card;
    private int turnsDelay;

    public CastDelayedCardAction(final AbstractCard card, final int turnsDelay)

    {
        this.card = card;
        this.turnsDelay = turnsDelay;
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
    	if (AbstractDungeon.player instanceof TheCaster) {
    		TheCaster caster = (TheCaster) AbstractDungeon.player;
    		caster.delayedCards.add(new DelayedCard(card, turnsDelay));
    	}
        isDone = true;
    }
}
