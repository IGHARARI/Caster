package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.caster.cards.CasterCard;
import sts.caster.core.MagicElement;

public class DelayedDamageRandomEnemyAction extends AbstractGameAction {

	MagicElement elem;
	
	public DelayedDamageRandomEnemyAction(AbstractCreature source, int baseDamage, MagicElement elem, AttackEffect effect) {
        actionType = ActionType.DAMAGE;
        this.source = source;
        this.amount = baseDamage;
        this.attackEffect = effect;
        this.elem = elem;
        this.duration = Settings.ACTION_DUR_XFAST;
	}

    @Override
    public void update() {
        if (!this.isDone) {
        	AbstractMonster randomTarget = AbstractDungeon.getMonsters().getRandomMonster(true);
        	int finamt = (int) CasterCard.customApplyEnemyPowersToSpellDamage(amount, elem, randomTarget);
        	AbstractDungeon.actionManager.addToTop(new DamageAction(randomTarget, new DamageInfo(source, finamt, DamageType.NORMAL), attackEffect));
        }
        this.isDone = true;
    }
}
