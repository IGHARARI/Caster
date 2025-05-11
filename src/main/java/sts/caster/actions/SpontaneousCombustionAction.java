package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.cards.spells.SpontaneousCombustion;

public class SpontaneousCombustionAction extends AbstractGameAction {
    
    private SpontaneousCombustion card;
    private int timesRepeat;
	
    public SpontaneousCombustionAction(SpontaneousCombustion card, int timesRepeat) {
        this.actionType = ActionType.DAMAGE;
        this.card = card;
        this.timesRepeat = timesRepeat;
    }
    
    @Override
    public void update() {
    	if (!isDone) {
    		if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
    			if (timesRepeat > 1) {
    				addToTop(new SpontaneousCombustionAction(card, timesRepeat-1));
				}
				AbstractMonster lowestMon = findLowestHPEnemy();
    			if (lowestMon != null) {
    				card.calculateCardDamage(lowestMon);
    				AbstractDungeon.actionManager.addToTop(new DamageAction(lowestMon, new DamageInfo(AbstractDungeon.player, card.spellDamage), AttackEffect.FIRE));
    			}
    		}
    	}
    	isDone = true;
    }

	private AbstractMonster findLowestHPEnemy() {
		AbstractMonster lowestMon = null;
		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
			if (!m.isDeadOrEscaped()) {
				if (lowestMon == null || lowestMon.currentHealth > m.currentHealth) lowestMon = m;
			}
		}
		return lowestMon;
	}
}
