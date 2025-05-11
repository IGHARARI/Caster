package sts.caster.interfaces;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.cards.CasterCard;

import java.util.ArrayList;

public interface ActionListSupplier {
	public ArrayList<AbstractGameAction> getActionList(CasterCard card, AbstractMonster target);
}
