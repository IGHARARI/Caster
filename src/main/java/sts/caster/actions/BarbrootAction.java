package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BarbrootAction extends AbstractGameAction {
	DamageInfo damageInfo;
	AbstractMonster target;
	
	public BarbrootAction(DamageInfo info, AbstractMonster target) {
        actionType = ActionType.DAMAGE;
        this.damageInfo = info;
        this.target = target;
	}

	@Override
    public void update() {
		int actualDamage = Math.min(damageInfo.output - target.currentBlock, target.currentHealth);
		int healAmount  = Math.max(actualDamage, 0) / 3;
		if (healAmount > 0) {
			AbstractDungeon.actionManager.addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, healAmount));
		}
		AbstractDungeon.actionManager.addToTop(new DamageAction(target, damageInfo, AttackEffect.BLUNT_HEAVY));
		
		isDone = true;
    }
}
