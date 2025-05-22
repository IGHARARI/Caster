package sts.caster.core.freeze;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import sts.caster.cards.mods.FrozenCardMod;
import sts.caster.interfaces.OnFreezePower;
import sts.caster.interfaces.OnThawPower;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FreezeHelper {
    private static Integer frozenCardCountForCombat = 0;
    public static void resetFrozenThisCombatCount() {
        frozenCardCountForCombat = 0;
    }
    public static Integer getFrozenThisCombatCount() {
        return frozenCardCountForCombat;
    }
    public static void increaseFrozenThisCombatCount(int amount) {
        frozenCardCountForCombat += amount;
    }

    public static List<OnFreezePower> getCurrentlyAppliedOnFreezePowers(AbstractCreature creature) {
        List<OnFreezePower> onFreezePowers = creature.powers.stream()
                .filter(OnFreezePower.class::isInstance)
                .map(OnFreezePower.class::cast)
                .collect(Collectors.toList());
        return onFreezePowers;
    }

    public static List<OnThawPower> getCurrentlyAppliedOnThawPowers(AbstractCreature creature) {
        List<OnThawPower> onThawPowers = creature.powers.stream()
                .filter(OnThawPower.class::isInstance)
                .map(OnThawPower.class::cast)
                .collect(Collectors.toList());
        return onThawPowers;
    }

    public static List<AbstractCard> getFrozenCardsForPile(CardGroup pile) {
        return getFrozenCardsForPile(pile.group);
    }

    public static List<AbstractCard> getFrozenCardsForPile(ArrayList<AbstractCard> pile) {
        return pile.stream().filter( c ->
                CardModifierManager.hasModifier(c, FrozenCardMod.ID)).collect(Collectors.toList()
        );
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

    public static List<AbstractCard> getAllFrozenCards() {
        Collection<CardGroup> allPiles = getAllPiles();
        ArrayList<AbstractCard> frozenCards = new ArrayList<>();
        for (CardGroup pile : allPiles) {
            frozenCards.addAll(getFrozenCardsForPile(pile));
        }
        return frozenCards;
    }

}
