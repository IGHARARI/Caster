package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DelayedDamageRandomEnemyAction extends AbstractGameAction {

	public DelayedDamageRandomEnemyAction(AbstractCreature source, int baseDamage, AttackEffect effect) {
        actionType = ActionType.DAMAGE;
        this.source = source;
        this.amount = baseDamage;
        this.attackEffect = effect;
        this.duration = Settings.ACTION_DUR_XFAST;
	}

    @Override
    public void update() {
        if (!this.isDone) {
        	AbstractMonster randomTarget = AbstractDungeon.getMonsters().getRandomMonster(true);
        	AbstractDungeon.actionManager.addToTop(new DamageAction(randomTarget, new DamageInfo(source, amount, DamageType.NORMAL), attackEffect));
        }
        this.isDone = true;
    }
}
