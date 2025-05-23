package sts.caster.actions;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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
            for (OnElectrifyPower power : onElectrifyPowers) {
                power.onElectrify(card);
            }

//			card.tags.add(CasterCardTags.ELECTRIFIED);
//
//			CasterMod.cardsElectrifiedThisCombat++;
//			triggerCardWasElectrifiedForGroup(AbstractDungeon.player.hand.group);
//			triggerCardWasElectrifiedForGroup(AbstractDungeon.player.drawPile.group);
//			triggerCardWasElectrifiedForGroup(AbstractDungeon.player.discardPile.group);
//			triggerCardWasElectrifiedForGroup(AbstractDungeon.player.exhaustPile.group);
////			triggerCardWasElectrifiedForGroup(DeprecatedFrozenPileManager.frozenPile.group);
//
//			if (AbstractDungeon.player.hasPower(StaticFieldPower.POWER_ID)) {
//				AbstractDungeon.player.getPower(StaticFieldPower.POWER_ID).onSpecificTrigger();
//			}
//
//			if (AbstractDungeon.player.hasPower(GainPower.POWER_ID)) {
//				AbstractDungeon.player.getPower(GainPower.POWER_ID).onSpecificTrigger();
//			}
		}
    	this.isDone = true;
    }
}
