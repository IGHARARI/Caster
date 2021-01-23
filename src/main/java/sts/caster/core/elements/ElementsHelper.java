package sts.caster.core.elements;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.ActionOnAllEnemiesAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.MagicElement;
import sts.caster.patches.spellCardType.CasterCardType;
import sts.caster.powers.ElementalStatusPower;
import sts.caster.powers.ManaImbalancePower;

import java.util.HashMap;

import static sts.caster.delayedCards.DelayedCardsArea.getLastSpellForDelay;

public class ElementsHelper {

    private static final int STACKS_PER_REACTION = 2;

    public static void updateElementalAffliction(CasterCard spellCard, AbstractMonster cardTarget) {
        AbstractPlayer player = AbstractDungeon.player;

        // If card targets All, check all monster for elemental reaction and update elemental status
        if (spellCard.target.equals(AbstractCard.CardTarget.ALL) || spellCard.target.equals(AbstractCard.CardTarget.ALL_ENEMY)) {
            addToTop(new ActionOnAllEnemiesAction((mon) -> {
                ElementalStatusPower status = getMonsterElementalStatus(mon);
                if (status == null) {
                    return new ApplyPowerAction(mon, player, new ElementalStatusPower(mon, spellCard.cardElement));
                } else {
                    status.element = spellCard.cardElement;
                    status.updateDescription();
                    return null;
                }
            }));
            addToTop(new ActionOnAllEnemiesAction((mon) -> {
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
        return status != null && causesReaction(status.element, card.cardElement);
    }

    private static void addToTop(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    public static boolean willCauseManaStruck(CasterCard casterCard) {
        if (casterCard.type == CasterCardType.SPELL && isEnemyTarget(casterCard.target)) {
            int delayTurnToCheck = casterCard.delayTurns;
            while (delayTurnToCheck > 0) {
                CasterCard lastCard = getLastSpellForDelay(delayTurnToCheck);
                if (lastCard != null && isEnemyTarget(lastCard.target)) {
                    return causesReaction(lastCard.cardElement, casterCard.cardElement);
                }
                delayTurnToCheck--;
            }
            if (delayTurnToCheck == 0) {
                long reactiveMonsters = AbstractDungeon.getCurrRoom().monsters.monsters.stream().
                        filter(m -> shouldCardCauseReaction(casterCard, m)).count();
                return reactiveMonsters > 0;
            }
        }
        return false;
    }

    private static boolean isEnemyTarget(AbstractCard.CardTarget target) {
        return target.equals(AbstractCard.CardTarget.ENEMY) ||
               target.equals(AbstractCard.CardTarget.ALL_ENEMY) ||
               target.equals(AbstractCard.CardTarget.ALL) ||
               target.equals(AbstractCard.CardTarget.SELF_AND_ENEMY);
    }

    public static boolean causesReaction(MagicElement current, MagicElement newOne) {
        MagicElement reactiveElements = elementalReactions.get(current);
        return reactiveElements != null && reactiveElements.equals(newOne);
    }
    public static HashMap<MagicElement, MagicElement> elementalReactions = new HashMap<>();
    static {
        elementalReactions.put(MagicElement.FIRE, MagicElement.ICE);
        elementalReactions.put(MagicElement.ICE, MagicElement.ELECTRIC);
        elementalReactions.put(MagicElement.ELECTRIC, MagicElement.EARTH);
        elementalReactions.put(MagicElement.EARTH, MagicElement.FIRE);
    }

}
