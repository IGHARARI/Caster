package sts.caster.actions;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.interfaces.ActionListMakerInterface;

public class RecurringDelayedCardAction extends AbstractGameAction {

	private AbstractCard card;
	private int delayTurns;
	private ActionListMakerInterface actionsMaker;
	
	public RecurringDelayedCardAction(AbstractCard card, ActionListMakerInterface actionsMaker, int delayTurns) {
        actionType = ActionType.DAMAGE;
        this.card = card;
        this.actionsMaker = actionsMaker;
        this.delayTurns = delayTurns;
        this.duration = Settings.ACTION_DUR_XFAST;
	}

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_XFAST) {
        	ArrayList<AbstractGameAction> actions = new ArrayList<AbstractGameAction>();
        	actions.addAll(actionsMaker.getActionList());
        	RecurringDelayedCardAction recursiveAction = new RecurringDelayedCardAction(card, actionsMaker, delayTurns);
        	actions.add(recursiveAction);
        	AbstractDungeon.actionManager.addToBottom(new QueueDelayedCardAction(card, delayTurns, actions));
        }
        this.tickDuration();
    }
}
