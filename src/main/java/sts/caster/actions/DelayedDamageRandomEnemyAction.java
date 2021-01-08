package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import sts.caster.cards.CasterCard;
import sts.caster.core.MagicElement;
import sts.caster.interfaces.MonsterToActionInterface;

import java.util.Collections;
import java.util.List;

public class DelayedDamageRandomEnemyAction extends AbstractGameAction {

	MagicElement elem;
	// Used for SFX and VFX
	List<MonsterToActionInterface> additionalEffects;

    public DelayedDamageRandomEnemyAction(AbstractCreature source, int baseDamage, MagicElement elem, AttackEffect effect) {
        this(source, baseDamage, elem, effect, null);
    }

	public DelayedDamageRandomEnemyAction(AbstractCreature source, int baseDamage, MagicElement elem, AttackEffect effect, List<MonsterToActionInterface> additionalEffects) {
        actionType = ActionType.DAMAGE;
        this.source = source;
        this.amount = baseDamage;
        this.attackEffect = effect;
        this.elem = elem;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.additionalEffects = additionalEffects;
        // reverse the order of the effects so they play correctly when adding to TOP
        Collections.reverse(this.additionalEffects);
	}

    @Override
    public void update() {
        if (!this.isDone) {
        	AbstractMonster randomTarget = AbstractDungeon.getMonsters().getRandomMonster(true);
        	int finalAmount = (int) CasterCard.customApplyEnemyPowersToSpellDamage(amount, elem, randomTarget);
        	AbstractDungeon.actionManager.addToTop(new DamageAction(randomTarget, new DamageInfo(source, finalAmount, DamageType.NORMAL), attackEffect));
        	if (additionalEffects != null) {
        	    for (MonsterToActionInterface a: additionalEffects) {
        	        AbstractDungeon.actionManager.addToTop(a.getAction(randomTarget));
                }
            }
        }
        this.isDone = true;
    }
}
