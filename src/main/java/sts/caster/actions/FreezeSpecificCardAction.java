package sts.caster.actions;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import sts.caster.cards.mods.FrozenCardMod;
import sts.caster.cards.skills.WallOfLightning;

import java.util.ArrayList;

public class FreezeSpecificCardAction extends AbstractGameAction {
    AbstractCard card;

    public FreezeSpecificCardAction(AbstractCard card) {
        this.card = card;
    }

    @Override
    public void update() {
       	card.flash();
        CardModifierManager.addModifier(card, new FrozenCardMod());
    	this.isDone = true;
    }
}
