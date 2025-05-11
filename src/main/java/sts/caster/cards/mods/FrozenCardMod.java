package sts.caster.cards.mods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import sts.caster.cards.CasterCard;
import sts.caster.cards.CasterCardTags;
import sts.caster.core.CasterMod;

public class FrozenCardMod extends AbstractCardModifier {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("FrozenCardMod");
    public static final String ID = CasterMod.makeID("FrozenCardMod");
    private static final int ON_DRAW_BLOCK_AMOUNT = 5;

    public FrozenCardMod() {
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return uiStrings.TEXT[0] + " NL " + rawDescription;
    }

    @Override
    public boolean canPlayCard(AbstractCard card) {
        return false;
    }

    @Override
    public void onDrawn(AbstractCard card) {
        addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, ON_DRAW_BLOCK_AMOUNT));
        card.retain = true;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.retain = true;
        card.tags.add(CasterCardTags.FROZEN);
        if (card instanceof CasterCard) {
            ((CasterCard) card).onFrozen();
        }
    }

    @Override
    public void onRetained(AbstractCard card) {
        card.retain = true;
    }

    @Override
    public void onRemove(AbstractCard card) {
        card.retain = false;
        card.tags.remove(CasterCardTags.FROZEN);
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return !CardModifierManager.hasModifier(card, ID);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new FrozenCardMod();
    }
}
