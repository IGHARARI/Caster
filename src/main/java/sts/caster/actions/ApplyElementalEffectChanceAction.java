package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.caster.core.MagicElement;
import sts.caster.powers.FrostPower;
import sts.caster.powers.ShockedPower;

public class ApplyElementalEffectChanceAction extends AbstractGameAction {

	private int timesToChance;
	private int stacksPerChance;
	private float chanceToApply;
	private MagicElement element;
	private static float DEFAULT_APPLY_CHANCE = 0.5f;
	private static int DEFAULT_STACKS_PER_CHANCE = 1;
	
	public ApplyElementalEffectChanceAction(AbstractCreature source, AbstractCreature target, MagicElement element,  int timesToChance) {
		this(source, target, element, timesToChance, DEFAULT_STACKS_PER_CHANCE, DEFAULT_APPLY_CHANCE);
	}
	public ApplyElementalEffectChanceAction(AbstractCreature source, AbstractCreature target, MagicElement element,  int timesToChance, int stacksPerChance) {
		this(source, target, element, timesToChance, stacksPerChance, DEFAULT_APPLY_CHANCE);
	}
    
	public ApplyElementalEffectChanceAction(AbstractCreature source, AbstractCreature target, MagicElement element, int timesToChance, int stacksPerChance, float chanceToApply) {
        actionType = ActionType.DEBUFF;
        this.timesToChance = timesToChance;
        this.chanceToApply = chanceToApply;
        this.stacksPerChance = stacksPerChance;
        this.element = element;
        this.source = source;
        this.target = target;
	}

	@Override
    public void update() {
    	if (!isDone && target != null && !target.isDeadOrEscaped()) {
    		int debuffCounter = 0;
    		for (int i = 0; i < timesToChance; i++) {
    			if (AbstractDungeon.cardRandomRng.randomBoolean(chanceToApply)) debuffCounter++;
    		}
    		AbstractPower powerToApply = null;
    		switch (element) {
    			case ICE:
    				powerToApply = new FrostPower(target, source, stacksPerChance);
    				break;
    			case ELECTRIC:
    				powerToApply = new ShockedPower(target, source, stacksPerChance);
    				break;
				default:
					break;
    		}
    		if (powerToApply != null) {
    			for (int i = 0 ; i < debuffCounter; i++) {
    				addToBot(new ApplyPowerAction(target, source, powerToApply, stacksPerChance));
    			}
    		}
    	}
        isDone = true;
    }
}
