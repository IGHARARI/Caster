package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import sts.caster.core.freeze.FreezeHelper;

public class CryogenesisAction extends AbstractGameAction {
	
	private int drawPerFrozen;
	
	public CryogenesisAction(int drawPerFrozen) {
		this.drawPerFrozen = drawPerFrozen;
        actionType = ActionType.CARD_MANIPULATION;
	}

	@Override
    public void update() {
    	int cardsInHandSize = AbstractDungeon.player.hand.size();
		int alreadyFrozenCards = FreezeHelper.getFrozenCardsForPile(AbstractDungeon.player.hand).size();
		int toFreeze = cardsInHandSize - alreadyFrozenCards;
    	addToBot(new FreezeRandomCardsAction(toFreeze));
    	addToBot(new DrawCardAction(AbstractDungeon.player, drawPerFrozen*toFreeze));
    	
		isDone = true;
    }
}
