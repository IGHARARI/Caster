package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

import sts.caster.delayedCards.CastingSpellCard;
import sts.caster.delayedCards.SpellCardsArea;

public class BloodRitualAction extends AbstractGameAction {

	public BloodRitualAction(int reduceAmount) {
        actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = reduceAmount;
	}

	@Override
    public void update() {
		if (duration == Settings.ACTION_DUR_FAST) {
			for(CastingSpellCard card : SpellCardsArea.spellCardsBeingCasted) {
				addToBot(new DelayedCardOnStartOfTurnTriggerAction(card));
			}
		}
        tickDuration();
    }
}
