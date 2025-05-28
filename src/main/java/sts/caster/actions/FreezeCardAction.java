package sts.caster.actions;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import sts.caster.cards.mods.FrozenCardMod;
import sts.caster.core.freeze.FreezeHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FreezeCardAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("FreezeCardAction");
    public static final String[] TEXT = FreezeCardAction.uiStrings.TEXT;
    private AbstractPlayer p;
    private boolean isRandom;
    private boolean anyNumber;
    private boolean canPickZero;
    private AbstractCard exception;
    private List<AbstractCard> alreadyFrozen = new ArrayList();
    private List<AbstractCard> sortedCopyOfInitialHand = new ArrayList();

    public FreezeCardAction(final int amount, final boolean isRandom) {
        this(amount, isRandom, false, false);
    }

    public FreezeCardAction(final int amount, final boolean isRandom, final AbstractCard exception) {
        this(amount, isRandom, false, false);
        this.exception = exception;
    }
    
    public FreezeCardAction(final int amount, final boolean isRandom, final boolean anyNumber, final boolean canPickZero) {
        this.canPickZero = false;
        this.anyNumber = anyNumber;
        this.canPickZero = canPickZero;
        this.p = AbstractDungeon.player;
        this.isRandom = isRandom;
        this.amount = amount;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
    }
    
    public FreezeCardAction(final int amount, final boolean isRandom, final boolean anyNumber) {
        this(amount, isRandom, anyNumber, false);
    }

    // Refactor for the new freeze.
//    @Override
//    public void update() {
//        if (this.duration == Settings.ACTION_DUR_FAST) {
//            if (this.p.hand.size() == 0) {
//                this.isDone = true;
//                return;
//            }
//            if (!this.anyNumber && this.p.hand.size() <= this.amount) {
//                this.amount = this.p.hand.size();
//                for (int tmp = this.p.hand.size(), i = 0; i < tmp; ++i) {
//                    final AbstractCard c = this.p.hand.getTopCard();
//                    DeprecatedFrozenPileManager.moveToFrozenPile(p.hand, c);
//                }
//                this.isDone = true;
//                return;
//            }
//            if (!this.isRandom) {
//                AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, this.anyNumber, this.canPickZero);
//                this.tickDuration();
//                return;
//            }
//            for (int j = 0; j < this.amount; ++j) {
//            	DeprecatedFrozenPileManager.moveToFrozenPile(p.hand, p.hand.getRandomCard(AbstractDungeon.cardRandomRng));
//            }
//        }
//        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
//            for (final AbstractCard c2 : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
//                DeprecatedFrozenPileManager.moveToFrozenPile(p.hand, c2);
//            }
//            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
//        }
//        this.tickDuration();
//    }
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            alreadyFrozen = FreezeHelper.getFrozenCardsForPile(p.hand);
            sortedCopyOfInitialHand = new ArrayList<>(p.hand.group);
            if (this.p.hand.size() == 0) {
                this.isDone = true;
                return;
            }
            if (alreadyFrozen.size() == this.p.hand.size()) {
                this.isDone = true;
                return;
            }

            if ((this.p.hand.group.size() - alreadyFrozen.size()) == 1) {
                for(AbstractCard c : this.p.hand.group) {
                    if (!CardModifierManager.hasModifier(c, FrozenCardMod.ID)) {
                        addToBot(new FreezeSpecificCardAction(c));
                        this.isDone = true;
                        return;
                    }
                }
            }

            this.p.hand.group.removeAll(alreadyFrozen);
            if (!this.anyNumber && this.p.hand.size() <= this.amount) {
                this.amount = this.p.hand.size();
                for(AbstractCard c : this.p.hand.group) {
                    addToBot(new FreezeSpecificCardAction(c));
                }
                this.isDone = true;
                this.returnCards();
                return;
            }

            if (!this.isRandom) {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, this.anyNumber, this.canPickZero);
                this.tickDuration();
                return;
            }
            // It's random, shuffle (to randomize) and freeze the bottom amount
            List<AbstractCard> handCopy = new ArrayList<AbstractCard>(p.hand.group);
            if (exception != null) handCopy.remove(exception);
            Collections.shuffle(handCopy, new Random(AbstractDungeon.shuffleRng.randomLong()));
            for (int j = 0; j < this.amount; ++j) {
                addToBot(new FreezeSpecificCardAction(handCopy.get(j)));
            }
            this.returnCards();
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (final AbstractCard c2 : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                addToBot(new FreezeSpecificCardAction(c2));
                c2.superFlash();
                p.hand.addToTop(c2);
            }
            this.returnCards();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            this.isDone = true;
        }
        this.tickDuration();
    }

    private void returnCards() {
        this.p.hand.clear();
        for(AbstractCard c : this.sortedCopyOfInitialHand) {
            this.p.hand.addToTop(c);
        }
        this.p.hand.refreshHandLayout();
    }
}
