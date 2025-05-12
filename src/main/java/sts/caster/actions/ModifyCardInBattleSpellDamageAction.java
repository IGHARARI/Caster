package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import sts.caster.cards.CasterCard;

import java.util.function.Predicate;

public class ModifyCardInBattleSpellDamageAction extends AbstractGameAction {

	Predicate<AbstractCard> pred;
	
	public ModifyCardInBattleSpellDamageAction(CasterCard card, int modifyAmount) {
        actionType = ActionType.CARD_MANIPULATION;
        this.pred = (c) -> (c != null && card.uuid.equals(c.uuid));
        this.amount = modifyAmount;
	}

	public ModifyCardInBattleSpellDamageAction(Predicate<AbstractCard> pred, int modifyAmount) {
		actionType = ActionType.CARD_MANIPULATION;
		this.pred = pred;
		this.amount = modifyAmount;
	}

	@Override
    public void update() {
        if (pred.test(AbstractDungeon.player.cardInUse)) {
        	increaseSpellDamage(AbstractDungeon.player.cardInUse);
        }
        for (final AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (pred.test(c)) {
                increaseSpellDamage(c);
            }
        }
        for (final AbstractCard c : AbstractDungeon.player.discardPile.group) {
        	if (pred.test(c)) {
                increaseSpellDamage(c);
            }
        }
        for (final AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
        	if (pred.test(c)) {
                increaseSpellDamage(c);
            }
        }
        for (final AbstractCard c : AbstractDungeon.player.limbo.group) {
        	if (pred.test(c)) {
                increaseSpellDamage(c);
            }
        }
        for (final AbstractCard c : AbstractDungeon.player.hand.group) {
        	if (pred.test(c)) {
                increaseSpellDamage(c);
            }
        }
//        for (final AbstractCard c : DeprecatedFrozenPileManager.frozenPile.group) {
//        	if (pred.test(c)) {
//        		increaseSpellDamage(c);
//        	}
//        }
        
        
        this.isDone = true;
    }

	private void increaseSpellDamage(AbstractCard c) {
    	if (!(c instanceof CasterCard)) return;
    	CasterCard castrcard = (CasterCard) c;
    	castrcard.baseSpellDamage = Math.max(0, castrcard.baseSpellDamage + amount);
    	castrcard.applyPowers();
	}
}
