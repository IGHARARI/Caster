package sts.caster.interfaces;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public interface MonsterToActionInterface {
	public AbstractGameAction getAction(AbstractMonster monster);
}
