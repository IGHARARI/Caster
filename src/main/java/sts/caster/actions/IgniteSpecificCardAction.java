package sts.caster.actions;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import sts.caster.cards.mods.IgnitedCardMod;

public class IgniteSpecificCardAction extends AbstractGameAction {
    AbstractCard card;

    public IgniteSpecificCardAction(AbstractCard card) {
        this.card = card;
    }

    @Override
    public void update() {
       	card.flash();
        CardModifierManager.addModifier(card, new IgnitedCardMod());
    	this.isDone = true;
    }
}
