package sts.caster.cards.mods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import sts.caster.actions.CardOnFrozenTriggerAction;
import sts.caster.actions.CardOnThawTriggerAction;
import sts.caster.cards.CasterCard;
import sts.caster.cards.CasterCardTags;
import sts.caster.core.CasterMod;
import sts.caster.core.freeze.FreezeHelper;
import sts.caster.interfaces.IPlayableWhileFrozen;
import sts.caster.interfaces.OnFreezePower;
import sts.caster.interfaces.OnThawPower;

import java.util.List;

import static sts.caster.core.freeze.FreezeHelper.getCurrentlyAppliedOnFreezePowers;
import static sts.caster.core.freeze.FreezeHelper.getCurrentlyAppliedOnThawPowers;

public class FrozenCardMod extends AbstractCardModifier {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("FrozenCardMod");
    public static final String ID = CasterMod.makeID("FrozenCardMod");
    public static final int ON_DRAW_BLOCK_AMOUNT = 2;

    public FrozenCardMod() {
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return uiStrings.TEXT[0] + " NL " + rawDescription;
    }

    @Override
    public boolean canPlayCard(AbstractCard card) {
        if (card instanceof IPlayableWhileFrozen) {
            return true;
        }
        card.cantUseMessage = "Can't play a #rFrozen Card";
        return false;
    }

    @Override
    public void onDrawn(AbstractCard card) {
        addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, ON_DRAW_BLOCK_AMOUNT));
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.tags.add(CasterCardTags.FROZEN);
        FreezeHelper.increaseFrozenThisCombatCount(1);

        if (card instanceof CasterCard) {
            CasterMod.logger.info("On freeze for " + card.name + " " + card.uuid);
            addToBot(new CardOnFrozenTriggerAction((CasterCard)card));
        }

        List<OnFreezePower> onFreezePowers = getCurrentlyAppliedOnFreezePowers(AbstractDungeon.player);
        for (OnFreezePower power : onFreezePowers) {
            power.onFreeze(card);
        }

        card.applyPowers();
    }

    @Override
    public void atEndOfTurn(AbstractCard card, CardGroup group) {
        card.retain = true;
    }

    @Override
    public void onRemove(AbstractCard card) {
        card.tags.remove(CasterCardTags.FROZEN);
        List<OnThawPower> onThawPowers = getCurrentlyAppliedOnThawPowers(AbstractDungeon.player);
        for (OnThawPower power : onThawPowers) {
            power.onThaw(card);
        }
        if (card instanceof CasterCard) {
            CasterMod.logger.info("Thawing " + card.name + " " + card.uuid);
            addToBot(new CardOnThawTriggerAction((CasterCard)card));
        }
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
