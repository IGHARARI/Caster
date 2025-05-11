package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.FlashPowerEffect;
import sts.caster.powers.FrostPower;
import sts.caster.util.PowersHelper;

public class FrostTriggerAction extends AbstractGameAction {

	public FrostTriggerAction(AbstractCreature target) {
        actionType = ActionType.DAMAGE;
        this.target = target;
	}

	@Override
    public void update() {
    	if (!isDone) {
			addToTop(new ReducePowerAction(target, target, FrostPower.POWER_ID, 1));
			addToTop(new NonSkippableWaitAction(0.1f));
    		this.amount = PowersHelper.getCreaturePowerAmount(FrostPower.POWER_ID, target);
    		if (amount > 0) {
    			if (target.isPlayer) {
    				addToTop(new DamageAction(target, new DamageInfo(null, amount, DamageType.THORNS), AttackEffect.BLUNT_LIGHT, true));
    			} else {
    				int[] damageMatrix = DamageInfo.createDamageMatrix(amount, true);
    				addToTop(new DamageAllEnemiesAction(null, damageMatrix, DamageType.THORNS, AttackEffect.BLUNT_LIGHT, true));
    			}
    			addToTop(new SFXAction("POWER_SHACKLE", 0.5f));
				addToTop(new VFXAction(new FlashPowerEffect(target.getPower(FrostPower.POWER_ID))));

			}
			addToTop(new NonSkippableWaitAction(0.1f));
    	}
        isDone = true;
    }
}
