package sts.caster.cards.mods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import sts.caster.actions.FreezeSpecificCardAction;

public class FreezeOnUseCardMod extends AbstractCardModifier {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("FreezeOnUseCardMod");

    static final String ID = "caster:FreezeOnUseCardMod";
    private boolean removeOnTrigger;

    public FreezeOnUseCardMod() { }

    public FreezeOnUseCardMod(boolean removeOnTrigger) {
        this.removeOnTrigger = removeOnTrigger;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        addToBot(new FreezeSpecificCardAction(card));
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        return removeOnTrigger;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return !CardModifierManager.hasModifier(card, ID);
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + uiStrings.TEXT[0];
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new FreezeOnUseCardMod();
    }
}
