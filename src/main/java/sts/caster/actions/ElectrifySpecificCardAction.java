package sts.caster.actions;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.cards.mods.ElectrifiedCardMod;
import sts.caster.core.freeze.ElectrifiedHelper;
import sts.caster.interfaces.OnElectrifyPower;

import java.util.List;

public class ElectrifySpecificCardAction extends AbstractGameAction {
    AbstractCard card;
    int amount;
    
    public ElectrifySpecificCardAction(AbstractCard card) {
        this(card, 1);
    }

    public ElectrifySpecificCardAction(AbstractCard card, int amount) {
        this.card = card;
        this.amount = amount;
    }

    @Override
    public void update() {
       	card.flash();
       	for (int i = 0; i < amount; i++) {
            CardModifierManager.addModifier(card, new ElectrifiedCardMod());
            ElectrifiedHelper.increaseElectrifiedThisCombatCount(1);
            ElectrifiedHelper.triggerCardWasElectrifiedForAllGroups();

            List<OnElectrifyPower> onElectrifyPowers = ElectrifiedHelper.getCurrentlyAppliedOnElectrifiedPowers(AbstractDungeon.player);
            onElectrifyPowers.forEach(power -> power.onElectrify(card));

            for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
                if (!mon.isDeadOrEscaped() && mon.currentHealth > 0) {
                    List<OnElectrifyPower> monsterOnElectrifyPowers = ElectrifiedHelper.getCurrentlyAppliedOnElectrifiedPowers(mon);
                    monsterOnElectrifyPowers.forEach(power -> power.onElectrify(card));
                }
            }
		}
    	this.isDone = true;
    }
}
