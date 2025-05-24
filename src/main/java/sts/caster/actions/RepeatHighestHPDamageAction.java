package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.cards.CasterCard;

public class RepeatHighestHPDamageAction extends AbstractGameAction {
    
    private CasterCard card;
    private int timesRepeat;
	
    public RepeatHighestHPDamageAction(CasterCard card, int timesRepeat) {
        this.actionType = ActionType.DAMAGE;
        this.card = card;
        this.timesRepeat = timesRepeat;
    }
    
    @Override
    public void update() {
    	if (!isDone) {
    		if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
    			if (timesRepeat > 1) {
    				addToTop(new RepeatHighestHPDamageAction(card, timesRepeat-1));
				}
				AbstractMonster highestHPEnemy = findHighestHPEnemy();
    			if (highestHPEnemy != null) {
    				card.calculateCardDamage(highestHPEnemy);
    				AbstractDungeon.actionManager.addToTop(new DamageAction(highestHPEnemy, new DamageInfo(AbstractDungeon.player, card.spellDamage), AttackEffect.FIRE));
    			}
    		}
    	}
    	isDone = true;
    }

	private AbstractMonster findHighestHPEnemy() {
		AbstractMonster highestHPMon = null;
		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
			if (!m.isDeadOrEscaped()) {
				if (highestHPMon == null || highestHPMon.currentHealth < m.currentHealth) highestHPMon = m;
			}
		}
		return highestHPMon;
	}
}
