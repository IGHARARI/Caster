package sts.caster.cards.mods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.combat.BlockedWordEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import sts.caster.cards.CasterCardTags;
import sts.caster.core.CasterMod;
import sts.caster.core.freeze.StancesHelper;

import java.util.ArrayList;

public class ElectrifiedCardMod extends AbstractCardModifier {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ElectrifiedCardMod");
    public static final String ID = CasterMod.makeID("ElectrifiedCardMod");
    public static final int ON_DRAW_DRAW_AMOUNT = 1;
    public static final int ELECTRIFY_DAMAGE = 2;
    public static final String electrifiedMessage = CardCrawlGame.languagePack.getUIString("ElectrifiedStrings").TEXT[0];
    private int electrifiedAmount = 1;
    private boolean wasCostModified;
    private int originalBaseCost;

    public ElectrifiedCardMod() {
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return uiStrings.TEXT[0] +" x"+ electrifiedAmount + " NL " + rawDescription;
    }

//    @Override
//    public void onDrawn(AbstractCard card) {
//        addToBot(new AbstractGameAction() {
//            @Override
//            public void update() {
//                card.superFlash(Color.GOLD.cpy());
//                isDone = true;
//            }
//        });
//        addToBot(new DrawCardAction(AbstractDungeon.player, ON_DRAW_DRAW_AMOUNT));
//    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (card.hasTag(CasterCardTags.ELECTRIFIED)) {
            int tagsBeforeRemove = card.tags.size();
            card.tags.removeIf((tag) -> tag == CasterCardTags.ELECTRIFIED);
            int amountElectrified = tagsBeforeRemove - card.tags.size();
            addToBot(new VFXAction(new BlockedWordEffect(AbstractDungeon.player, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, electrifiedMessage)));
            addToBot(new VFXAction(new LightningEffect(AbstractDungeon.player.drawX, AbstractDungeon.player.drawY), 0.1f));
            addToBot(new SFXAction("ORB_LIGHTNING_EVOKE"));
            DamageInfo damage = new DamageInfo(AbstractDungeon.player, ELECTRIFY_DAMAGE * amountElectrified, DamageInfo.DamageType.NORMAL);
            addToBot(new DamageAction(AbstractDungeon.player, damage));

            if (this.wasCostModified) {
                updateCost(card, card.cost +1);
                if (card.cost == this.originalBaseCost) card.isCostModified = false;
            }
        }
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        return true;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        increaseElectrify(card);
    }

    private void increaseElectrify(AbstractCard card) {
        card.tags.add(CasterCardTags.ELECTRIFIED);

//        No onElectrified for cards yet
//        if (card instanceof CasterCard) {
//            addToBot(new CardOnElectrifiedTriggerAction((CasterCard)card));
//        }
        card.applyPowers();
    }


    @Override
    public boolean shouldApply(AbstractCard card) {
        if (StancesHelper.shouldTriggerElectroplasma(card, this)) {
            StancesHelper.triggerElectroplasma(card);
            return false;
        }
        if (StancesHelper.shouldTriggerShatter(card, this)) {
            StancesHelper.triggerShatter(card);
            return false;
        }
        ArrayList<AbstractCardModifier> list = CardModifierManager.getModifiers(card, ElectrifiedCardMod.ID);
        for (AbstractCardModifier other : list) {
            ((ElectrifiedCardMod)other).electrifiedAmount += 1;
            increaseElectrify(card);
            card.initializeDescription();
            return false;
        }
        // First stack, reduce cost.
        if (card.cost > 0) {
            this.wasCostModified = true;
            this.originalBaseCost = card.cost;
            updateCost(card, card.cost -1);

        }
        return true;
    }

    private void updateCost(AbstractCard card, int newBaseCost) {
        int diff = card.costForTurn - card.cost;
        card.cost = newBaseCost;
        if (card.costForTurn > 0)
            card.costForTurn = card.cost + diff;
        if (card.costForTurn < 0)
            card.costForTurn = 0;
        card.isCostModified = true;
    }

    @Override
    public void onRemove(AbstractCard card) {
        card.tags.removeIf((tag) -> tag == CasterCardTags.ELECTRIFIED);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ElectrifiedCardMod();
    }
}
