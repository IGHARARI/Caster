package sts.caster.core.freeze;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import sts.caster.actions.IgniteSpecificCardAction;
import sts.caster.delayedCards.CastingSpellCard;
import sts.caster.delayedCards.SpellCardsArea;
import sts.caster.interfaces.ICardWasIgnitedSubscriber;
import sts.caster.patches.delayedCards.CastingQueuePileEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class IgnitedHelper {
    public static void triggerCardWasIgnitedForAllGroups() {
        getAllPiles().forEach(g -> triggerCardWasIgnitedForGroup(g));
        triggerCardWasIgnitedForCastingArea();
    }

    private static void triggerCardWasIgnitedForGroup(CardGroup cardGroup) {
        for (AbstractCard c : cardGroup.group){
            if (c instanceof ICardWasIgnitedSubscriber) ((ICardWasIgnitedSubscriber)c).cardWasIgnited(cardGroup.type);
        }
    }

    private static void triggerCardWasIgnitedForCastingArea() {
        for (CastingSpellCard c : SpellCardsArea.spellCardsBeingCasted){
            for (AbstractCard cardCopy : c.getAllCardCopies()) {
                if (cardCopy instanceof ICardWasIgnitedSubscriber) {
                    ((ICardWasIgnitedSubscriber)cardCopy).cardWasIgnited(CastingQueuePileEnum.CASTER_CASTING_QUEUE);
                }
            }
        }
    }

    private static final Consumer<List<AbstractCard>> addIgniteToCardsConsumer = list -> {
        list.forEach(c -> {
            c.flash(Color.RED.cpy());
            AbstractDungeon.actionManager.addToTop(new IgniteSpecificCardAction(c));
        });
    };
    public static SelectCardsInHandAction buildSelectCardsToIgniteAction(int amount) {
        return new SelectCardsInHandAction(amount, "Ignite", addIgniteToCardsConsumer);
    }
    public static SelectCardsInHandAction buildSelectCardsToIgniteAction(int amount, Predicate<AbstractCard> cardFilter) {
        return new SelectCardsInHandAction(amount, "Ignite ", cardFilter, addIgniteToCardsConsumer);
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
