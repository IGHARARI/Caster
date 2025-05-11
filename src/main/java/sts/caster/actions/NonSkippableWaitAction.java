package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import sts.caster.delayedCards.CastingSpellCard;

public class NonSkippableWaitAction extends AbstractGameAction {

	CastingSpellCard delayedCard;
	
    public NonSkippableWaitAction(final float setDur) {
        this.setValues(null, null, 0);
        this.duration = setDur;
        if (Settings.FAST_MODE) duration *= 0.66f;
        this.actionType = ActionType.WAIT;
    }
    
    @Override
    public void update() {
        this.tickDuration();
    }
}
