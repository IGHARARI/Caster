package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import sts.caster.delayedCards.CastingSpellCard;
import sts.caster.delayedCards.SpellCardsArea;

public class ModifyAllCastingSpellCastTimeAction extends AbstractGameAction {

	public ModifyAllCastingSpellCastTimeAction(int amount) {
		this.amount = amount;
	}


	@Override
    public void update() {
    	if (!isDone) {
			if (SpellCardsArea.spellCardsBeingCasted != null) {
				addToTop(new QueueRedrawMiniCardsAction());
				for (CastingSpellCard delayCard : SpellCardsArea.spellCardsBeingCasted) {
					addToTop(new ModifyCastingSpellCastTimeAction(delayCard, amount));
				}
			}
    	}
        isDone = true;
    }
}
