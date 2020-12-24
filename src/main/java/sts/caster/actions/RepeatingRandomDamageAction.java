package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class RepeatingRandomDamageAction extends AbstractGameAction {
    private DamageInfo info;
    private static final float DURATION = 0.01f;
    private float postAttackWait = 0.2f;
    private int numTimes;

    public RepeatingRandomDamageAction(final AbstractCreature target, final DamageInfo info, final int numTimes) {
    	this(target, info, numTimes, AttackEffect.BLUNT_LIGHT, true);
    }

    public RepeatingRandomDamageAction(final AbstractCreature target, final DamageInfo info, final int numTimes, boolean isFast) {
    	this(target, info, numTimes, AttackEffect.BLUNT_LIGHT, isFast);
    }
    
    public RepeatingRandomDamageAction(final AbstractCreature target, final DamageInfo info, final int numTimes, AttackEffect attackEffect) {
    	this(target, info, numTimes, attackEffect, true);
    }
    
    public RepeatingRandomDamageAction(final AbstractCreature target, final DamageInfo info, final int numTimes, final AttackEffect attackEffect, final boolean isFast) {
        this.info = info;
        this.target = target;
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = attackEffect;
        this.duration = DURATION;
        if (isFast) postAttackWait = 0.1f;
        this.numTimes = numTimes;
    }
    
    @Override
    public void update() {
        if (this.target == null) {
            this.isDone = true;
            return;
        }
        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
            this.isDone = true;
            return;
        }
        if (this.target.currentHealth > 0) {
//            this.target.damageFlash = true;
//            this.target.damageFlashFrames = 4;
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
            this.info.applyPowers(this.info.owner, this.target);
            this.target.damage(this.info);
            if (this.numTimes > 1 && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                --this.numTimes;
                AbstractDungeon.actionManager.addToTop(new RepeatingRandomDamageAction(AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng), this.info, this.numTimes));
            }
            AbstractDungeon.actionManager.addToTop(new WaitAction(postAttackWait));
        }
        this.isDone = true;
    }
}
