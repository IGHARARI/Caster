package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import sts.caster.cards.CasterCard;
import sts.caster.delayedCards.CastingSpellCard;
import sts.caster.delayedCards.SpellCardsArea;

import java.util.List;

public class ModifyAllCastingSpellsEffectAction extends AbstractGameAction {

	private final int damageModifyAmount;
	private final int blockModifyAmount;

	public ModifyAllCastingSpellsEffectAction(int damageModifyAmount, int blockModifyAmount) {
		this.damageModifyAmount = damageModifyAmount;
		this.blockModifyAmount = blockModifyAmount;
		actionType = ActionType.SPECIAL;
	}

	@Override
    public void update() {
    	if (!isDone) {
			if (SpellCardsArea.spellCardsBeingCasted != null) {
				for (CastingSpellCard delayCard : SpellCardsArea.spellCardsBeingCasted) {
					List<CasterCard> internalCopies = delayCard.getAllCardCopies();
					for (CasterCard cardCopy : internalCopies) {
						if (cardCopy.baseSpellBlock > 0) {
							cardCopy.baseSpellBlock += blockModifyAmount;
						}
						if (cardCopy.baseSpellDamage > 0) {
							cardCopy.baseSpellDamage += damageModifyAmount;
						}
					}
				}
			}
    	}
        isDone = true;
    }
}
