package sts.caster.cards.mods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import sts.caster.core.CasterMod;

public class RetainOnceCardMod extends AbstractCardModifier {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("RetainOnceCardMod");
    public static final String ID = CasterMod.makeID("RetainOnceCardMod");

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    public RetainOnceCardMod() {
    }

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
        return uiStrings.TEXT[0] + " NL " + rawDescription;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new RetainOnceCardMod();
    }
}
