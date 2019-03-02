package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.caster.cards.CasterCard;

public class RequeueDelayedCardAction extends AbstractGameAction {

	private CasterCard card;
	private int delayTurns;
	private AbstractMonster monsterTarget;
	
	public RequeueDelayedCardAction(CasterCard card, AbstractMonster monsterTarget) {
        actionType = ActionType.DAMAGE;
        this.card = card.makeStatIdenticalCopy();
        this.card.delayTurns = card.baseDelayTurns;
        this.delayTurns = card.baseDelayTurns;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.monsterTarget = monsterTarget;
	}

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_XFAST) {
        	AbstractDungeon.actionManager.addToBottom(new QueueDelayedCardAction(card, delayTurns, monsterTarget));
        }
        this.tickDuration();
    }
}
