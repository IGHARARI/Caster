package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class CryogenesisAction extends AbstractGameAction {
	
	private int drawPerFrozen;
	
	public CryogenesisAction(int drawPerFrozen) {
		this.drawPerFrozen = drawPerFrozen;
        actionType = ActionType.CARD_MANIPULATION;
	}

	@Override
    public void update() {
    	int cardsInHand = AbstractDungeon.player.hand.size();
    	addToBot(new FreezeCardAction(cardsInHand, true));
    	addToBot(new DrawCardAction(AbstractDungeon.player, drawPerFrozen*cardsInHand));
    	
		isDone = true;
    }
}
