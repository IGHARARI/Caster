package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.delayedCards.DelayedCardEffect;
import sts.caster.delayedCards.DelayedCardsArea;

public class BloodRitualAction extends AbstractGameAction {

	public BloodRitualAction(int reduceAmount) {
        actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = reduceAmount;
	}

	@Override
    public void update() {
		if (duration == Settings.ACTION_DUR_FAST) {
			for(DelayedCardEffect card : DelayedCardsArea.delayedCards) {
				addToBot(new DelayedCardOnStartOfTurnTriggerAction(card));
			}
		}
        tickDuration();
    }
}
