package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import sts.caster.cards.spells.TransmuteSoul;

import java.util.UUID;

public class TransmuteSoulAction extends AbstractGameAction {

	private final UUID originalUUID;
	private int increaseHpAmount;
	private TransmuteSoul cardInSpell;
	private AbstractMonster targetMonster;

    public TransmuteSoulAction(AbstractMonster target, int maxHPAmount, TransmuteSoul spellCard, UUID originalUUID) {
        this.actionType = ActionType.DAMAGE;
		this.cardInSpell = spellCard;
		this.targetMonster = target;
		this.originalUUID = originalUUID;
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
					AbstractCard c = AbstractDungeon.player.masterDeck.group.stream()
							.filter(abstractCard -> abstractCard.uuid == this.originalUUID)
							.findFirst()
							.orElse(null);

					if (c != null) {
						AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
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
