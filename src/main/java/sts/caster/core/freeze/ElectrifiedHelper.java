package sts.caster.core.freeze;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import sts.caster.delayedCards.CastingSpellCard;
import sts.caster.delayedCards.SpellCardsArea;
import sts.caster.interfaces.ICardWasElectrifiedSubscriber;
import sts.caster.interfaces.OnElectrifyPower;
import sts.caster.patches.delayedCards.CastingQueuePileEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
        getAllPiles().forEach(g -> triggerCardWasElectrifiedForGroup(g));
        triggerCardWasElectrifiedForCastingArea();
    }

    private static void triggerCardWasElectrifiedForGroup(CardGroup cardGroup) {
        for (AbstractCard c : cardGroup.group){
            if (c instanceof ICardWasElectrifiedSubscriber) ((ICardWasElectrifiedSubscriber)c).cardWasElectrified(cardGroup.type);
        }
    }

    private static void triggerCardWasElectrifiedForCastingArea() {
        for (CastingSpellCard c : SpellCardsArea.spellCardsBeingCasted){
            for (AbstractCard cardCopy : c.getAllCardCopies()) {
                if (cardCopy instanceof ICardWasElectrifiedSubscriber) {
                    ((ICardWasElectrifiedSubscriber)cardCopy).cardWasElectrified(CastingQueuePileEnum.CASTER_CASTING_QUEUE);
                }
            }
        }
    }

    public static List<OnElectrifyPower> getCurrentlyAppliedOnElectrifiedPowers(AbstractCreature creature) {
        List<OnElectrifyPower> onElectrifyPowers = creature.powers.stream()
                .filter(OnElectrifyPower.class::isInstance)
                .map(OnElectrifyPower.class::cast)
                .collect(Collectors.toList());
        return onElectrifyPowers;
    }

    private static Collection<CardGroup> getAllPiles() {
        AbstractPlayer p = AbstractDungeon.player;
        List<CardGroup> allPiles = new ArrayList<>();
        allPiles.add(p.drawPile);
        allPiles.add(p.hand);
        allPiles.add(p.discardPile);
        allPiles.add(p.exhaustPile);
        return allPiles;
    }
}
