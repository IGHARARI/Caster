package sts.caster.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import sts.caster.core.MagicElement;
import sts.caster.util.BattleHelper;

public class DelayedDamageAllEnemiesAction extends AbstractGameAction {

	MagicElement elem;
	
	public DelayedDamageAllEnemiesAction(AbstractCreature source, int damage, MagicElement elem, AttackEffect effect) {
        actionType = ActionType.DAMAGE;
        this.source = source;
        this.amount = damage;
        this.attackEffect = effect;
        this.elem = elem;
        this.duration = Settings.ACTION_DUR_FAST;
	}

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.source.isDying || this.source.halfDead) {
                this.isDone = true;
                return;
            }
            for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            	if (!mon.isDeadOrEscaped() && mon.currentHealth > 0) {
            		mon.damageFlash = true;
            		mon.damageFlashFrames = 4;
            		AbstractDungeon.effectList.add(new FlashAtkImgEffect(mon.hb.cX, mon.hb.cY, this.attackEffect, false));
            	}
            }
        }
        this.tickDuration();
        if (this.isDone) {
        	int[] damageMatrix = BattleHelper.createSpellDamageMatrix(amount, elem, false);
        	
            for (final AbstractPower p : AbstractDungeon.player.powers) {
                p.onDamageAllEnemies(damageMatrix);
            }
            for (int i = 0; i < AbstractDungeon.getMonsters().monsters.size(); i++) {
            	AbstractMonster mon = AbstractDungeon.getMonsters().monsters.get(i);
            	if (!mon.isDeadOrEscaped() && mon.currentHealth > 0) {
                    if (this.attackEffect == AttackEffect.POISON) {
                        mon.tint.color = Color.CHARTREUSE.cpy();
                        mon.tint.changeColor(Color.WHITE.cpy());
                    }
                    else if (this.attackEffect == AttackEffect.FIRE) {
                        mon.tint.color = Color.RED.cpy();
                        mon.tint.changeColor(Color.WHITE.cpy());
                    }
            	}
            	mon.damage(new DamageInfo(source, damageMatrix[i], DamageType.NORMAL));
            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
            if (!Settings.FAST_MODE) {
                AbstractDungeon.actionManager.addToTop(new WaitAction(0.1f));
            }
        }
    }
}
