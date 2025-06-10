package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.caster.cards.CasterCard;
import sts.caster.cards.mods.RecurringSpellCardMod;
import sts.caster.delayedCards.CastingSpellCard;
import sts.caster.delayedCards.SpellCardsArea;
import sts.caster.powers.EchoingVoicePower;

public class QueueDelayedCardAction extends AbstractGameAction {
    private CasterCard card;
    private int turnsDelay;
	private Integer energyOnUse;
	AbstractMonster target;
	private boolean isRecurrence;
	
    public QueueDelayedCardAction(final CasterCard card, final int turnsDelay, AbstractMonster target) {
    	this(card, turnsDelay, null, target);
    }

	public QueueDelayedCardAction(final CasterCard card, final int turnsDelay, Integer energyOnUse, AbstractMonster target, boolean isRecurrence) {
		this.card = card.makeStatIdenticalCopy();
		this.turnsDelay = turnsDelay;
		actionType = ActionType.SPECIAL;
		this.energyOnUse = energyOnUse;
		this.target = target;
		this.isRecurrence = isRecurrence;
	}
    
    public QueueDelayedCardAction(final CasterCard card, final int turnsDelay, Integer energyOnUse, AbstractMonster target) {
		this(card, turnsDelay, energyOnUse, target, false);
    }

	@Override
    public void update() {
		if (!this.isRecurrence) {
			AbstractPower echoPower = AbstractDungeon.player.getPower(EchoingVoicePower.POWER_ID);
			if (echoPower != null && echoPower.amount > 0) {
				RecurringSpellCardMod.addRecurrence(card, echoPower.amount);
			}
		}
		CastingSpellCard delayedCard = new CastingSpellCard(card, turnsDelay, energyOnUse, target);
		SpellCardsArea.addCardToArea(delayedCard);
		SpellCardsArea.repositionMiniCards();
        isDone = true;
    }
}
