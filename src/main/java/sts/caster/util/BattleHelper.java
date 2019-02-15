package sts.caster.util;

import java.util.ArrayList;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BattleHelper {
	public static ArrayList<AbstractMonster> getCurrentBattleMonstersSortedOnX(boolean aliveOnly){
		ArrayList<AbstractMonster> orderedMonsters = new ArrayList<AbstractMonster>();
		if (AbstractDungeon.getMonsters() != null) {
			for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
				if (aliveOnly && m.isDeadOrEscaped()) continue;
				orderedMonsters.add(m);
			}
		}
		//order monsters on the X axis, Y axis pretty much only matters for enemies on the same vertical line.
		orderedMonsters.sort((m1,m2) -> Math.round((m1.drawX - m2.drawX)*1000 + (m1.drawY - m2.drawY)));
		return orderedMonsters;
	}
	
	
	public static ArrayList<AbstractMonster> getCurrentBattleMonstersSortedDistanceTo(AbstractCreature measureFrom, boolean aliveOnly){
		ArrayList<AbstractMonster> orderedMonsters = new ArrayList<AbstractMonster>();
		if (AbstractDungeon.getMonsters() != null) {
			for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
				if (aliveOnly && m.isDeadOrEscaped()) continue;
				orderedMonsters.add(m);
			}
		}
		orderedMonsters.sort((m1,m2) -> (int)Math.round(
				Math.hypot(Math.abs(m1.drawX - measureFrom.drawX), Math.abs(m1.drawY - measureFrom.drawY)) - 
				Math.hypot(Math.abs(m2.drawX - measureFrom.drawX), Math.abs(m2.drawY - measureFrom.drawY))
			));
		return orderedMonsters;
	}


	
}
