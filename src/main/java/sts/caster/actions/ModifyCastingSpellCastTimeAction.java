package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import sts.caster.cards.CasterCard;
import sts.caster.delayedCards.CastingSpellCard;
import sts.caster.delayedCards.SpellCardsArea;

import java.util.List;
import java.util.stream.Collectors;

public class ModifyCastingSpellCastTimeAction extends AbstractGameAction {


	private final CasterCard cardToModify;
	private final CastingSpellCard castingSpellToModify;

	public ModifyCastingSpellCastTimeAction(CasterCard card, int modifyAmount) {
		this.amount = modifyAmount;
		this.cardToModify = card;
		this.castingSpellToModify = null;
		actionType = ActionType.SPECIAL;
	}

	public ModifyCastingSpellCastTimeAction(CastingSpellCard castingSpellToModify, int modifyAmount) {
		this.amount = modifyAmount;
		this.cardToModify = null;
		this.castingSpellToModify = castingSpellToModify;
		actionType = ActionType.SPECIAL;
	}


	@Override
    public void update() {
    	if (!isDone) {
			if (cardToModify != null) {
				if (SpellCardsArea.spellCardsBeingCasted != null) {
					List<CastingSpellCard> matchingUUIDCards = SpellCardsArea.spellCardsBeingCasted.stream().filter(
							c -> c.spellCard.uuid.equals(cardToModify.uuid)).collect(Collectors.toList());
					for (CastingSpellCard card: matchingUUIDCards) {
						// Process them in a new private action to make sure we only ever process one card at a time.
						addToTop(new ModifyCastingSpellCastTimeAction(card, amount));
					}
				}
			}
			if (castingSpellToModify != null) {
				castingSpellToModify.modifyCastingDelay(amount);
				addToBot(new QueueRedrawMiniCardsAction());
			}

    	}
        isDone = true;
    }
}
