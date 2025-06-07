package sts.caster.actions;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import sts.caster.cards.mods.IgnitedCardMod;

import static sts.caster.core.freeze.IgnitedHelper.triggerCardWasIgnitedForAllGroups;

public class IgniteSpecificCardAction extends AbstractGameAction {
    AbstractCard card;
    int igniteAmount;

    public IgniteSpecificCardAction(AbstractCard card) {
        this.card = card;
        this.igniteAmount = 1;
    }

    public IgniteSpecificCardAction(AbstractCard card, int igniteAmount) {
        this.card = card;
        this.igniteAmount = igniteAmount;
    }

    @Override
    public void update() {
       	card.flash();
        for (int i = 0; i < igniteAmount; i++) {
            CardModifierManager.addModifier(card, new IgnitedCardMod());
        }
        triggerCardWasIgnitedForAllGroups();
    	this.isDone = true;
    }
}
