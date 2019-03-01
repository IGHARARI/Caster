package sts.caster.actions;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.cards.CasterCard;
import sts.caster.interfaces.ActionListMakerInterface;

public class RecurringDelayedCardAction extends AbstractGameAction {

	private CasterCard card;
	private int delayTurns;
	private ActionListMakerInterface actionsMaker;
	
	public RecurringDelayedCardAction(CasterCard card, ActionListMakerInterface actionsMaker, int delayTurns, AbstractCreature target) {
        actionType = ActionType.DAMAGE;
        this.card = card;
        this.actionsMaker = actionsMaker;
        this.delayTurns = card.baseDelayTurns;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.target = target;
	}

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_XFAST) {
        	ArrayList<AbstractGameAction> actions = new ArrayList<AbstractGameAction>();
        	actions.addAll(actionsMaker.getActionList());
        	RecurringDelayedCardAction recursiveAction = new RecurringDelayedCardAction(card, actionsMaker, delayTurns, target);
        	actions.add(recursiveAction);
        	AbstractDungeon.actionManager.addToBottom(new QueueDelayedCardAction(card, delayTurns, actions, target));
        }
        this.tickDuration();
    }
}
