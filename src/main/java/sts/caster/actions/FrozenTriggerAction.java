package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class FrozenTriggerAction extends AbstractGameAction {

	public FrozenTriggerAction(AbstractCreature target, int frozenStacks) {
        actionType = ActionType.DAMAGE;
        this.target = target;
        this.amount = frozenStacks;
	}

	@Override
    public void update() {
    	if (!isDone) {
			if (target.isPlayer) {
				AbstractDungeon.actionManager.addToTop(new DamageAction(target, new DamageInfo(target, amount, DamageType.THORNS), AttackEffect.BLUNT_LIGHT, true));
			} else {
				int[] damageMatrix = DamageInfo.createDamageMatrix(amount, true);
				AbstractDungeon.actionManager.addToTop(new DamageAllEnemiesAction(target, damageMatrix, DamageType.THORNS, AttackEffect.BLUNT_LIGHT, true));
			}
			AbstractDungeon.actionManager.addToTop(new SFXAction("POWER_SHACKLE", 0.5f));
    	}
        isDone = true;
    }
}
