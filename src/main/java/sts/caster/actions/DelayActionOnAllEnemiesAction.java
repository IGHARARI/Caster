package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.caster.interfaces.MonsterToActionInterface;

public class DelayActionOnAllEnemiesAction extends AbstractGameAction {

	MonsterToActionInterface monToAction;
	
	public DelayActionOnAllEnemiesAction(MonsterToActionInterface monToAction) {
        actionType = ActionType.SPECIAL;
        this.monToAction = monToAction;
	}

	@Override
    public void update() {
    	if (!isDone) {
            for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            	if (!mon.isDeadOrEscaped() && mon.currentHealth > 0) {
            		AbstractGameAction action = monToAction.getAction(mon);
            		AbstractDungeon.actionManager.addToTop(action);
            	}
            }
    	}
        isDone = true;
    }
}
