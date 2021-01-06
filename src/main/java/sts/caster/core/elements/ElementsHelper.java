package sts.caster.core.elements;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.DelayedActionOnAllEnemiesAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.powers.ElementalStatusPower;
import sts.caster.powers.ManaImbalancePower;

public class ElementsHelper {

    private static final int STACKS_PER_REACTION = 2;

    public static void updateElementalAffliction(CasterCard spellCard, AbstractMonster cardTarget) {
        AbstractPlayer player = AbstractDungeon.player;

        // If card targets All, check all monster for elemental reaction and update elemental status
        if (spellCard.target.equals(AbstractCard.CardTarget.ALL) || spellCard.target.equals(AbstractCard.CardTarget.ALL_ENEMY)) {
            addToTop(new DelayedActionOnAllEnemiesAction((mon) -> {
                ElementalStatusPower status = getMonsterElementalStatus(mon);
                if (status == null) {
                    return new ApplyPowerAction(mon, player, new ElementalStatusPower(mon, spellCard.cardElement));
                } else {
                    status.element = spellCard.cardElement;
                    status.updateDescription();
                    return null;
                }
            }));
            addToTop(new DelayedActionOnAllEnemiesAction((mon) -> {
                if (shouldCardCauseReaction(spellCard, mon)) {
                    return new ApplyPowerAction(mon, player, new ManaImbalancePower(mon, STACKS_PER_REACTION), STACKS_PER_REACTION);
                }
                return null;
            }));
        }
        // Otherwise only check cardTarget for elemental reaction and update status
        if (spellCard.target.equals(AbstractCard.CardTarget.ENEMY) || spellCard.target.equals(AbstractCard.CardTarget.SELF_AND_ENEMY)) {
            if (shouldCardCauseReaction(spellCard, cardTarget)) {
                addToTop(new ApplyPowerAction(cardTarget, player, new ManaImbalancePower(cardTarget, STACKS_PER_REACTION), STACKS_PER_REACTION));
            }
            ElementalStatusPower status = getMonsterElementalStatus(cardTarget);
            if (status == null) {
                addToTop(new ApplyPowerAction(cardTarget, player, new ElementalStatusPower(cardTarget, spellCard.cardElement)));
            } else {
                status.element = spellCard.cardElement;
                status.updateDescription();
            }
        }
    }

    private static ElementalStatusPower getMonsterElementalStatus(AbstractMonster mon) {
        if (mon != null && mon.hasPower(ElementalStatusPower.POWER_ID)) {
            return (ElementalStatusPower) mon.getPower(ElementalStatusPower.POWER_ID);
        }
        return null;
    }

    private static boolean shouldCardCauseReaction(CasterCard card, AbstractMonster mon) {
        ElementalStatusPower status = getMonsterElementalStatus(mon);
        return status != null && CasterMod.causesReaction(status.element, card.cardElement);
    }

    private static void addToTop(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }
}
