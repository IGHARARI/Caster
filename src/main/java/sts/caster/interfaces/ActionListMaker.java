package sts.caster.interfaces;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.caster.cards.CasterCard;

public interface ActionListMaker {
	public ArrayList<AbstractGameAction> getActionList(CasterCard card, AbstractMonster target);
}
