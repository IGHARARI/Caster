package sts.caster.cards.mods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import sts.caster.actions.CardOnAfterRecurringTriggerAction;
import sts.caster.interfaces.OnRecurringSpell;

import java.util.ArrayList;

public class RecurringSpellCardMod extends AbstractCardModifier {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("RecurringSpellCardMod");
    public static final String ID = "caster:RecurringSpellCardMod";
    public int recurAmount;

    public RecurringSpellCardMod(int recurAmount) {
        this.recurAmount = recurAmount;
    }

    public void reduceRecurrence(AbstractCard card) {
        this.recurAmount--;
    }

    public void onAfterRecurringAction(AbstractCard card) {
        if (card instanceof OnRecurringSpell) {
            addToBot(new CardOnAfterRecurringTriggerAction(((OnRecurringSpell) card)));
        }
    }

    public void modifyRecurrence(int amount) {
        this.recurAmount += amount;
    }

    public void setRecurrence(int amount) {
        this.recurAmount = amount;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + uiStrings.TEXT[0] + this.recurAmount + uiStrings.TEXT[1];
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new RecurringSpellCardMod(this.recurAmount);
    }

    public static void addRecurrence(AbstractCard card, int amount) {
        if (CardModifierManager.hasModifier(card, RecurringSpellCardMod.ID)) {
            ArrayList<AbstractCardModifier> mods = CardModifierManager.getModifiers(card, RecurringSpellCardMod.ID);
            for (AbstractCardModifier mod : mods) {
                RecurringSpellCardMod recurMod = (RecurringSpellCardMod) mod;
                recurMod.modifyRecurrence(amount);
                card.initializeDescription();
            }
        } else {
            CardModifierManager.addModifier(card, new RecurringSpellCardMod(amount));
        }

    }
}
