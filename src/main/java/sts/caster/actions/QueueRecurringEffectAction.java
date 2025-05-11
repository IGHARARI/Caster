package sts.caster.actions;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sts.caster.cards.CasterCard;
import sts.caster.cards.mods.RecurringSpellCardMod;
import sts.caster.core.CasterMod;
import sts.caster.delayedCards.CastingSpellCard;
import sts.caster.delayedCards.SpellCardsArea;

import java.util.ArrayList;

public class QueueRecurringEffectAction extends AbstractGameAction {
    private CasterCard card;
    private int turnsDelay;
	private Integer energyOnUse;
	AbstractMonster target;

    public QueueRecurringEffectAction(final CasterCard card, final int turnsDelay, AbstractMonster target) {
    	this(card, turnsDelay, null, target);
    }

    public QueueRecurringEffectAction(final CasterCard card, final int turnsDelay, Integer energyOnUse, AbstractMonster target) {
    	this.card = card.makeStatIdenticalCopy();
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
		AbstractDungeon.actionManager.addToBottom(new QueueDelayedCardAction(card, turnsDelay, energyOnUse, target));

		this.isDone = true;
    }
}
