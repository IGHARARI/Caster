package sts.caster.core.freeze;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import sts.caster.cards.skills.WallOfLightning;
import sts.caster.interfaces.OnElectrifyPower;

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
        getAllPiles().forEach(g -> triggerCardWasElectrifiedForGroup(g.group));
    }

    private static void triggerCardWasElectrifiedForGroup(ArrayList<AbstractCard> cardGroup) {
        for (AbstractCard c : cardGroup){
            if (c.cardID == WallOfLightning.ID) ((WallOfLightning)c).cardWasElectrified();
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
