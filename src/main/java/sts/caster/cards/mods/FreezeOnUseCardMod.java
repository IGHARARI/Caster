package sts.caster.cards.mods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import sts.caster.patches.relics.FreezeOnUseCardField;

public class FreezeOnUseCardMod extends AbstractCardModifier {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("FreezeOnUseCardMod");

    static final String ID = "caster:FreezeOnUseCardMod";

    public FreezeOnUseCardMod() { }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        // Should be true, but remove on played is a little wonky for cardmods
        return false;
    }

    @Override
    public boolean removeAtEndOfTurn(AbstractCard card) {
        return false;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                FreezeOnUseCardField.freezeOnuse.set(card, true);
                isDone = true;
            }
        });
    }

    @Override
    public void onRemove(AbstractCard card) {
        // In order for this to work it would need to be queued after the UseCardAction
        // Could use a wrapper action to push it down the queue but that's yucky
//        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
//            @Override
//            public void update() {
//                FreezeOnUseCardField.freezeOnuse.set(card, false);
//                isDone = true;
//            }
//        });
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return !FreezeOnUseCardField.freezeOnuse.get(card) && !CardModifierManager.hasModifier(card, ID);
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
