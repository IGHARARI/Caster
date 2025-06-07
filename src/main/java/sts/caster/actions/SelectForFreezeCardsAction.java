package sts.caster.actions;

import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.UIStrings;
import sts.caster.cards.mods.FrozenCardMod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SelectForFreezeCardsAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("FreezeCardAction");
    public static final String[] TEXT = SelectForFreezeCardsAction.uiStrings.TEXT;
    private Consumer<List<AbstractCard>> callback;
    private boolean anyNumber;
    private ArrayList<AbstractCard> cardPool;
    private Predicate<AbstractCard> cardFilter;

    public SelectForFreezeCardsAction(int amount, boolean anyNumber, Predicate<AbstractCard> cardFilter, Consumer<List<AbstractCard>> callback){
        this.amount = amount;
        this.duration = this.startDuration = Settings.ACTION_DUR_XFAST;
        this.anyNumber = anyNumber;
        this.callback = callback;
        this.cardFilter = cardFilter;
    }

    public SelectForFreezeCardsAction(int amount, boolean anyNumber){
        this.amount = amount;
        this.duration = this.startDuration = Settings.ACTION_DUR_XFAST;
        this.anyNumber = anyNumber;
        this.callback = list -> {/*DoNothing*/};
        this.cardFilter = card -> true;
    }

    @Override
    public void update() {
        String selectString = TEXT[0] + (anyNumber? TEXT[1] : " ") + amount + (amount==1 ? TEXT[2] : TEXT[3]);

        addToTop(new SelectCardsInHandAction(
                        amount,
                        selectString,
                        anyNumber,
                        anyNumber,
                        c -> cardFilter.test(c) && !CardModifierManager.hasModifier(c, FrozenCardMod.ID),
                        selected -> {
                            selected.forEach(card -> addToBot(new FreezeSpecificCardAction(card)));
                            callback.accept(selected);
                        }
                )
        );
        // unglow them first cause Select Cards doesn't do it. wrap in an action just in case..
//        addToTop(new AbstractGameAction() {
//            @Override
//            public void update() {
//                cardPool.forEach(c -> c.stopGlowing());
//                isDone = true;
//            }
//        });
        isDone = true;
    }
}
