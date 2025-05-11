package sts.caster.actions;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.powers.IllusionPower;

public class IllusionAction extends AbstractGameAction {
    
    public IllusionAction(AbstractMonster target, AbstractCreature source)
    {
        this.target = target;
        this.source = source;
        actionType = AbstractGameAction.ActionType.DEBUFF;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_FAST) {
        	if (!target.hasPower(StunMonsterPower.POWER_ID)) {
        		AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, source, new IllusionPower((AbstractMonster) target), -1));
        	}
        }
        tickDuration();
    }
}
