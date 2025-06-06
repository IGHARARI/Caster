package sts.caster.core.freeze;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import sts.caster.actions.IgniteSpecificCardAction;
import sts.caster.interfaces.ICardWasIgnitedSubscriber;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static sts.caster.core.freeze.ElementalModsHelper.triggerOnElementalModAppliedForAllGroups;

public class IgnitedHelper {
    public static void triggerCardWasIgnitedForAllGroups() {
        triggerOnElementalModAppliedForAllGroups(
            ICardWasIgnitedSubscriber.class,
            (card, fromPile) -> card.cardWasIgnited(fromPile)
        );
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
}
