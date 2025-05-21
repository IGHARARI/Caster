package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;

public class QueueRecurringEffectAction extends AbstractGameAction {
    private CasterCard card;
    private int turnsDelay;
	private Integer energyOnUse;
	AbstractMonster target;

    public QueueRecurringEffectAction(final CasterCard card, final int turnsDelay, AbstractMonster target) {
    	this(card, turnsDelay, null, target);
    }

    public QueueRecurringEffectAction(final CasterCard card, final int turnsDelay, Integer energyOnUse, AbstractMonster target) {
    	this.card = card;
    	this.turnsDelay = turnsDelay;
    	actionType = ActionType.SPECIAL;
    	this.energyOnUse = energyOnUse;
    	this.target = target;
    }

	public static final Logger logger = LogManager.getLogger(CasterMod.class.getName());
	@Override
    public void update() {
		if (target == null || target.isDeadOrEscaped()) {
			target = AbstractDungeon.getRandomMonster();
		}
		CasterCard cardCopy = card.makeStatIdenticalCopy();
		AbstractDungeon.actionManager.addToBottom(new QueueDelayedCardAction(cardCopy, turnsDelay, energyOnUse, target));

		this.isDone = true;
    }
}
