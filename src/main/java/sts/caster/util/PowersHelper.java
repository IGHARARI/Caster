package sts.caster.util;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PowersHelper {
	public static int getPlayerPowerAmount(String powerId){
		return getCreaturePowerAmount(powerId, AbstractDungeon.player);
	}

	public static int getCreaturePowerAmount(String powerId, AbstractCreature creature){
		return creature != null && creature.hasPower(powerId) ? creature.getPower(powerId).amount : 0;
	}
}
