package sts.caster.core.freeze;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import sts.caster.actions.ShowCardVeryBrieflyAction;
import sts.caster.delayedCards.CastingSpellCard;
import sts.caster.delayedCards.SpellCardsArea;
import sts.caster.patches.delayedCards.CastingQueueGroupEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

;

public class ElementalModsHelper {
    public static <T> void triggerOnElementalModAppliedForAllGroups(
            Class<T> subscriberType,
            BiConsumer<T, CardGroup.CardGroupType> methodInvoker
    ) {
        getAllPiles().forEach(g -> triggerForGroup(g, subscriberType, methodInvoker));
        triggerForCastingArea(subscriberType, methodInvoker);
    }

    private static <T> void triggerForGroup(
            CardGroup cardGroup,
            Class<T> subscriberType,
            BiConsumer<T, CardGroup.CardGroupType> methodInvoker
    ) {
        for (AbstractCard c : cardGroup.group) {
            if (subscriberType.isInstance(c)) {
                methodInvoker.accept(subscriberType.cast(c), cardGroup.type);
                AbstractDungeon.actionManager.addToBottom(new ShowCardVeryBrieflyAction(c, 0.75f, 0.6f, false));
            }
        }
    }

    private static <T> void triggerForCastingArea(
            Class<T> subscriberType,
            BiConsumer<T, CardGroup.CardGroupType> methodInvoker
    ) {
        for (CastingSpellCard c : SpellCardsArea.spellCardsBeingCasted) {
            boolean showEffect = false;
            for (AbstractCard cardCopy : c.getAllCardCopies()) {
                if (subscriberType.isInstance(cardCopy)) {
                    methodInvoker.accept(subscriberType.cast(cardCopy), CastingQueueGroupEnum.CASTER_CASTING_QUEUE);
                    showEffect = true;
                }
            }
            if (showEffect) {
                AbstractDungeon.actionManager.addToBottom(new ShowCardVeryBrieflyAction(c.cardMiniCopy, 0.75f, 0.6f, true));
            }
        }
    }

    public static Collection<CardGroup> getAllPiles() {
        AbstractPlayer p = AbstractDungeon.player;
        List<CardGroup> allPiles = new ArrayList<>();
        allPiles.add(p.drawPile);
        allPiles.add(p.hand);
        allPiles.add(p.discardPile);
        allPiles.add(p.exhaustPile);
        return allPiles;
    }
}
