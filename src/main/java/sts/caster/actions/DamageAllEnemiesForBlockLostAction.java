package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
		System.out.println("Block list on damage all for block is: " + CasterMod.blockLostLastRound);
		AbstractDungeon.actionManager.addToBottom(
				new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(CasterMod.blockLostLastRound, true), DamageType.NORMAL, AttackEffect.BLUNT_HEAVY));
		isDone = true;
    }
}
