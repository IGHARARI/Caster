package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import sts.caster.cards.skills.Explosion;

public class ReduceExplosionDamageAction extends AbstractGameAction {

	Explosion card;
	
	public ReduceExplosionDamageAction(Explosion explosionCard, int reduceAmount) {
        actionType = ActionType.DAMAGE;
        this.card = explosionCard;
        this.amount = reduceAmount;
	}

	@Override
    public void update() {
    	if (!isDone) {
    		card.baseDamage = Math.max(0, card.baseDamage - amount);
    		card.applyPowers();
    	}
        isDone = true;
    }
}
