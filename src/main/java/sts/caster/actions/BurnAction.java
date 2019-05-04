package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.cards.special.Ashes;

public class BurnAction extends AbstractGameAction {
	
	private AbstractCard cardToBurn;
	private CardGroup fromGroup;
	
	public BurnAction(AbstractCard cardToBurn, CardGroup fromGroup) {
		this.cardToBurn = cardToBurn;
		this.fromGroup = fromGroup;
        actionType = ActionType.CARD_MANIPULATION;
	}

	@Override
    public void update() {
		AbstractDungeon.actionManager.addToTop(new MakeTempCardInDiscardAction(new Ashes(), 1));
    	AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(cardToBurn, fromGroup));
		isDone = true;
    }
}
