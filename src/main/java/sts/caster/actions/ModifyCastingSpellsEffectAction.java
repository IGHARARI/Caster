package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import sts.caster.cards.CasterCard;
import sts.caster.delayedCards.CastingSpellCard;
import sts.caster.delayedCards.SpellCardsArea;

import java.util.List;
import java.util.function.Predicate;

public class ModifyCastingSpellsEffectAction extends AbstractGameAction {

	private final int damageModifyAmount;
	private final int blockModifyAmount;
	private AbstractCard targetCard;

	public ModifyCastingSpellsEffectAction(int damageModifyAmount, int blockModifyAmount) {
		this.damageModifyAmount = damageModifyAmount;
		this.blockModifyAmount = blockModifyAmount;
		actionType = ActionType.SPECIAL;
	}

	public ModifyCastingSpellsEffectAction(AbstractCard card, int damageModifyAmount, int blockModifyAmount) {
		this(damageModifyAmount, blockModifyAmount);
		this.targetCard = card;
	}

	@Override
    public void update() {
    	if (!isDone) {
			if (SpellCardsArea.spellCardsBeingCasted != null) {
				Predicate<CastingSpellCard> maybeFilter = c -> true;
				if (this.targetCard != null) {
					maybeFilter = castingSpell -> castingSpell.spellCard == this.targetCard;
				}
				SpellCardsArea.spellCardsBeingCasted.stream()
					.filter(maybeFilter)
					.forEach(castingSpell -> increaseAllCopies(castingSpell));
			}
    	}
        isDone = true;
    }

	private void increaseAllCopies(CastingSpellCard delayedCard) {
		List<CasterCard> internalCopies = delayedCard.getAllCardCopies();
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
