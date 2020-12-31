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
		AbstractDungeon.actionManager.addToTop(new DamageAction(target, damageInfo, AttackEffect.BLUNT_HEAVY));
		
		isDone = true;
    }
}
