package sts.caster.core.freeze;

import com.megacrit.cardcrawl.core.AbstractCreature;
import sts.caster.interfaces.ICardWasElectrifiedSubscriber;
import sts.caster.interfaces.OnElectrifyPower;

import java.util.List;
import java.util.stream.Collectors;

import static sts.caster.core.freeze.ElementalModsHelper.triggerOnElementalModAppliedForAllGroups;

public class ElectrifiedHelper {
    private static Integer cardsElectrifiedThisCombat = 0;
    public static Integer getCardsElectrifiedThisCombat() {
        return cardsElectrifiedThisCombat;
    }
    public static void resetElectrifiedThisCombatCount() {
        cardsElectrifiedThisCombat = 0;
    }
    public static void increaseElectrifiedThisCombatCount(int amount) {
        cardsElectrifiedThisCombat += amount;
    }
    public static void triggerCardWasElectrifiedForAllGroups() {
        triggerOnElementalModAppliedForAllGroups(
                ICardWasElectrifiedSubscriber.class,
                (card, fromPile) -> card.cardWasElectrified(fromPile)
        );
    }

    public static List<OnElectrifyPower> getCurrentlyAppliedOnElectrifiedPowers(AbstractCreature creature) {
        List<OnElectrifyPower> onElectrifyPowers = creature.powers.stream()
                .filter(OnElectrifyPower.class::isInstance)
                .map(OnElectrifyPower.class::cast)
                .collect(Collectors.toList());
        return onElectrifyPowers;
    }
}
