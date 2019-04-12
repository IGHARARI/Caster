package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class VfxTestAction extends AbstractGameAction {
    
    private AbstractGameEffect effect;
	
    public VfxTestAction(AbstractGameEffect eff) {
        this.actionType = ActionType.SPECIAL;
        this.effect = eff;
    }
    
    @Override
    public void update() {
    	if (!isDone) {
    		AbstractDungeon.effectList.add(this.effect);
    	}
    	isDone = true;
    }
}
