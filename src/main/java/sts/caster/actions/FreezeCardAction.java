package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import sts.caster.core.frozenpile.FrozenPileManager;

public class FreezeCardAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ExhaustAction");
    public static final String[] TEXT = FreezeCardAction.uiStrings.TEXT;
    private AbstractPlayer p;
    private boolean isRandom;
    private boolean anyNumber;
    private boolean canPickZero;
    public static int numExhausted;
    
    public FreezeCardAction(final int amount, final boolean isRandom) {
        this(amount, isRandom, false, false);
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
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p.hand.size() == 0) {
                this.isDone = true;
                return;
            }
            if (!this.anyNumber && this.p.hand.size() <= this.amount) {
                this.amount = this.p.hand.size();
                ExhaustAction.numExhausted = this.amount;
                for (int tmp = this.p.hand.size(), i = 0; i < tmp; ++i) {
                    final AbstractCard c = this.p.hand.getTopCard();
                    FrozenPileManager.moveToFrozenPile(p.hand, c);
                }
                this.isDone = true;
                return;
            }
            if (!this.isRandom) {
                ExhaustAction.numExhausted = this.amount;
                AbstractDungeon.handCardSelectScreen.open(ExhaustAction.TEXT[0], this.amount, this.anyNumber, this.canPickZero);
                this.tickDuration();
                return;
            }
            for (int j = 0; j < this.amount; ++j) {
            	FrozenPileManager.moveToFrozenPile(p.hand, p.hand.getRandomCard(AbstractDungeon.cardRandomRng));
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (final AbstractCard c2 : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                FrozenPileManager.moveToFrozenPile(p.hand, c2);
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        this.tickDuration();
    }
}
