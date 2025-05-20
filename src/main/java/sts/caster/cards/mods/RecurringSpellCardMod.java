package sts.caster.cards.mods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import sts.caster.core.CasterMod;
import sts.caster.interfaces.OnRecurringSpell;

public class RecurringSpellCardMod extends AbstractCardModifier {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("RecurringSpellCardMod");
    public static final String ID = "caster:RecurringSpellCardMod";
    public int recurAmount;

    public RecurringSpellCardMod(int recurAmount) {
        this.recurAmount = recurAmount;
    }

    public void reduceRecurrence(AbstractCard card) {
        this.recurAmount--;
        if (card instanceof OnRecurringSpell) {
            CasterMod.logger.info("Recurring spell recurred: " + card.name + " " + card.uuid);
            ((OnRecurringSpell) card).onRecurring();
        }
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return uiStrings.TEXT[0] + this.recurAmount + uiStrings.TEXT[1] + " NL " + rawDescription;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new RecurringSpellCardMod(this.recurAmount);
    }
}
