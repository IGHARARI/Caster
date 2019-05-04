package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.core.CasterMod;

public class DamageAllEnemiesForBlockLostAction extends AbstractGameAction {
	
	public DamageAllEnemiesForBlockLostAction() {
        actionType = ActionType.DAMAGE;
	}

	@Override
    public void update() {
		System.out.println("Block list on damage all for block is: " + CasterMod.blockLostPerTurn.get(GameActionManager.turn));
		Integer blockLost = CasterMod.blockLostPerTurn.get(GameActionManager.turn -1);
		if (blockLost != null && blockLost != 0) {
			AbstractDungeon.actionManager.addToTop(
					new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(blockLost, true), DamageType.NORMAL, AttackEffect.BLUNT_HEAVY));
		}
		isDone = true;
    }
}
