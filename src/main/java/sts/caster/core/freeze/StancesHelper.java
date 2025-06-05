package sts.caster.core.freeze;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import sts.caster.cards.mods.ElectrifiedCardMod;
import sts.caster.cards.mods.FrozenCardMod;
import sts.caster.cards.mods.IgnitedCardMod;
import sts.caster.powers.AetherflameCatalystPower;
import sts.caster.powers.ShivasWrathPower;

public class StancesHelper {
    public static boolean shouldTriggerElectroplasma(AbstractCard card, AbstractCardModifier modBeingApplied) {
        if (!AbstractDungeon.player.hasPower(AetherflameCatalystPower.POWER_ID)) return false;
        if (modBeingApplied instanceof ElectrifiedCardMod) return shouldTriggerElectroplasma(card, (ElectrifiedCardMod) modBeingApplied);
        if (modBeingApplied instanceof IgnitedCardMod) return shouldTriggerElectroplasma(card, (IgnitedCardMod) modBeingApplied);
        return false;
    }
    private static boolean shouldTriggerElectroplasma(AbstractCard card, IgnitedCardMod mod) {
        return CardModifierManager.hasModifier(card, ElectrifiedCardMod.ID);
    }
    private static boolean shouldTriggerElectroplasma(AbstractCard card, ElectrifiedCardMod mod) {
        return CardModifierManager.hasModifier(card, IgnitedCardMod.ID);
    }

    public static void triggerElectroplasma(AbstractCard card) {
        AetherflameCatalystPower power = (AetherflameCatalystPower) AbstractDungeon.player.getPower(AetherflameCatalystPower.POWER_ID);
        if (power != null) power.triggerElectroPlasma(card);
    }

    public static boolean shouldTriggerShatter(AbstractCard card, AbstractCardModifier modBeingApplied) {
        if (modBeingApplied instanceof ElectrifiedCardMod) return shouldTriggerShatter(card, (ElectrifiedCardMod) modBeingApplied);
        if (modBeingApplied instanceof FrozenCardMod) return shouldTriggerShatter(card, (FrozenCardMod) modBeingApplied);
        return false;
    }
    private static boolean shouldTriggerShatter(AbstractCard card, FrozenCardMod mod) {
        return CardModifierManager.hasModifier(card, ElectrifiedCardMod.ID);
    }
    private static boolean shouldTriggerShatter(AbstractCard card, ElectrifiedCardMod mod) {
        return CardModifierManager.hasModifier(card, FrozenCardMod.ID);
    }

    public static void triggerShatter(AbstractCard card) {
        ShivasWrathPower power = (ShivasWrathPower) AbstractDungeon.player.getPower(ShivasWrathPower.POWER_ID);
        if (power != null) power.triggerShattered(card);
    }
}
