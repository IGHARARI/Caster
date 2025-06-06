package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import sts.caster.cards.CasterCard;

import java.util.function.Predicate;

public class ModifyCardInBattleSpellBlockAction extends AbstractGameAction {

	Predicate<AbstractCard> pred;

	public ModifyCardInBattleSpellBlockAction(CasterCard card, int modifyAmount) {
        actionType = ActionType.CARD_MANIPULATION;
        this.pred = (c) -> (c != null && card.uuid.equals(c.uuid));
        this.amount = modifyAmount;
	}

	public ModifyCardInBattleSpellBlockAction(Predicate<AbstractCard> pred, int modifyAmount) {
		actionType = ActionType.CARD_MANIPULATION;
		this.pred = pred;
		this.amount = modifyAmount;
	}

	@Override
    public void update() {
        if (pred.test(AbstractDungeon.player.cardInUse)) {
        	increaseSpellBlock(AbstractDungeon.player.cardInUse);
        }
        for (final AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (pred.test(c)) {
                increaseSpellBlock(c);
            }
        }
        for (final AbstractCard c : AbstractDungeon.player.discardPile.group) {
        	if (pred.test(c)) {
                increaseSpellBlock(c);
            }
        }
        for (final AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
        	if (pred.test(c)) {
                increaseSpellBlock(c);
            }
        }
        for (final AbstractCard c : AbstractDungeon.player.limbo.group) {
        	if (pred.test(c)) {
                increaseSpellBlock(c);
            }
        }
        for (final AbstractCard c : AbstractDungeon.player.hand.group) {
        	if (pred.test(c)) {
                increaseSpellBlock(c);
            }
        }
        
        this.isDone = true;
    }

	private void increaseSpellBlock(AbstractCard c) {
    	if (!(c instanceof CasterCard)) return;
    	CasterCard castrcard = (CasterCard) c;
    	castrcard.baseSpellBlock = Math.max(0, castrcard.baseSpellBlock + amount);
    	castrcard.applyPowers();
	}
}
