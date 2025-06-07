package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import sts.caster.core.freeze.FreezeHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FreezeRandomCardsAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("FreezeCardAction");
    public static final String[] TEXT = FreezeRandomCardsAction.uiStrings.TEXT;
    private AbstractPlayer p;
    private AbstractCard exception;
    private List<AbstractCard> alreadyFrozen = new ArrayList();

    public FreezeRandomCardsAction(final int amount) {
        this(amount, true);
    }

    public FreezeRandomCardsAction(final int amount, final AbstractCard exception) {
        this(amount, true);
        this.exception = exception;
    }
    
    private FreezeRandomCardsAction(final int amount, final boolean isRandom) {
        this.p = AbstractDungeon.player;
        this.amount = amount;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.p.hand.size() == 0) {
            this.isDone = true;
            return;
        }

        alreadyFrozen = FreezeHelper.getFrozenCardsForPile(p.hand);
        if (alreadyFrozen.size() == this.p.hand.size()) {
            this.isDone = true;
            return;
        }

        List<AbstractCard> handCopy = new ArrayList<AbstractCard>(p.hand.group);
        handCopy.removeAll(alreadyFrozen);
        if (exception != null) handCopy.remove(exception);

        if (handCopy.size() <= this.amount) {
            this.amount = handCopy.size();
            for(AbstractCard c : handCopy) {
                addToBot(new FreezeSpecificCardAction(c));
            }
            this.isDone = true;
            return;
        }

        // It's random, shuffle (to randomize) and freeze the bottom amount
        Collections.shuffle(handCopy, new Random(AbstractDungeon.shuffleRng.randomLong()));
        for (int j = 0; j < this.amount; ++j) {
            addToBot(new FreezeSpecificCardAction(handCopy.get(j)));
        }
        this.isDone = true;
    }
}
