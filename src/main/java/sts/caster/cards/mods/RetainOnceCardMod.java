package sts.caster.cards.mods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

public class RetainOnceCardMod extends AbstractCardModifier {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("RetainOnceCardMod");

    public RetainOnceCardMod() { }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.retain = true;
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        return true;
    }

    @Override
    public boolean removeAtEndOfTurn(AbstractCard card) {
        return true;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return uiStrings.TEXT[0] + " NL Hi there NL " + rawDescription;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new RetainOnceCardMod();
    }
}
