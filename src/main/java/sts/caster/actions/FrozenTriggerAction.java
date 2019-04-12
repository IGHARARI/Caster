package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.powers.FrostPower;
import sts.caster.util.PowersHelper;

public class FrozenTriggerAction extends AbstractGameAction {

	public FrozenTriggerAction(AbstractCreature target) {
        actionType = ActionType.DAMAGE;
        this.target = target;
	}

	@Override
    public void update() {
    	if (!isDone) {
    		AbstractDungeon.actionManager.addToTop(new NonSkippableWaitAction(0.2f));
    		AbstractDungeon.actionManager.addToTop(new ReducePowerAction(target, target, FrostPower.POWER_ID, 1));
    		this.amount = PowersHelper.getCreaturePowerAmount(FrostPower.POWER_ID, target);
    		if (amount > 0) {
    			target.getPower(FrostPower.POWER_ID).flash();
    			if (target.isPlayer) {
    				AbstractDungeon.actionManager.addToTop(new DamageAction(target, new DamageInfo(target, amount, DamageType.THORNS), AttackEffect.BLUNT_LIGHT, true));
    			} else {
    				int[] damageMatrix = DamageInfo.createDamageMatrix(amount, true);
    				AbstractDungeon.actionManager.addToTop(new DamageAllEnemiesAction(target, damageMatrix, DamageType.THORNS, AttackEffect.BLUNT_LIGHT, true));
    			}
    			AbstractDungeon.actionManager.addToTop(new SFXAction("POWER_SHACKLE", 0.5f));
    		}
    	}
        isDone = true;
    }
}
