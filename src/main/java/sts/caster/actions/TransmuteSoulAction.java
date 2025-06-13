package sts.caster.actions;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.cards.spells.TransmuteSoul;

public class TransmuteSoulAction extends AbstractGameAction {

	private int increaseHpAmount;
	private TransmuteSoul cardInSpell;
	private TransmuteSoul original;
	private AbstractMonster targetMonster;

    public TransmuteSoulAction(AbstractMonster target, int maxHPAmount, TransmuteSoul spellCard, TransmuteSoul originalCard) {
        this.actionType = ActionType.DAMAGE;
		this.cardInSpell = spellCard;
		this.original = originalCard;
		this.targetMonster = target;
    }
    
    @Override
    public void update() {
    	if (!isDone) {
			if (targetMonster != null) {
				cardInSpell.calculateCardDamage(targetMonster);

				DamageInfo info = new DamageInfo(AbstractDungeon.player, cardInSpell.spellDamage);
				targetMonster.damage(info);
				if ((targetMonster.isDying || targetMonster.currentHealth <= 0) && !targetMonster.halfDead && !targetMonster.hasPower("Minion")) {
					AbstractDungeon.player.energy.energyMaster++;
					AbstractDungeon.player.energy.energy++;
					AbstractCard c = StSLib.getMasterDeckEquivalent(this.original);
					if (c != null) {
						AbstractDungeon.player.masterDeck.removeCard(c);
					}
				}
				if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
					AbstractDungeon.actionManager.clearPostCombatActions();
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
