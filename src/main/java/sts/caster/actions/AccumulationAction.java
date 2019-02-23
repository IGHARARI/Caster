package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.cards.skills.Accumulation;

public class AccumulationAction extends AbstractGameAction {
    
    private int extraAmount = 0;
	
    public AccumulationAction(int extraAmount) {
        this.actionType = ActionType.BLOCK;
        this.duration = Settings.ACTION_DUR_FAST;
        this.extraAmount = extraAmount;
    }
    
    @Override
    public void update() {
    	if (!isDone) {
    		int totalBlock = extraAmount + Accumulation.countAggregateCastTime();
			AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, totalBlock));
    	}
    	isDone = true;
    }
}
