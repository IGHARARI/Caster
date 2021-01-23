package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class ElectrifyCardsAction extends AbstractGameAction {
    private AbstractPlayer p;
    private boolean isRandom = false;
    private int amount;
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ElectrifiedStrings");
    public static final String[] TEXT = uiStrings.TEXT;

    private final int electrifyAmount;

    public ElectrifyCardsAction(int cardsAmount, boolean isRandom) {
        this(cardsAmount, 1 , isRandom);
    }

    public ElectrifyCardsAction(int cardsAmount, int electrifyAmount, boolean isRandom) {
        this.p = AbstractDungeon.player;
        this.isRandom = isRandom;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.amount = cardsAmount;
        this.electrifyAmount = electrifyAmount;
    }
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p.hand.size() == 0) { //if hand is empty nothing to do
                this.isDone = true;
                return;
            }
            if (this.p.hand.size() <= this.amount) { //if hand has less cards than needed affect all cards
                this.amount = this.p.hand.size();
                for (int i = 0; i < this.p.hand.size(); ++i) {
                    AbstractCard c = this.p.hand.getTopCard();
                    electrifyCard(c, electrifyAmount);
                }
                this.isDone = true;
                return;
            }
            if (!this.isRandom) { //if it's not random, open select screen
                AbstractDungeon.handCardSelectScreen.open(TEXT[1], this.amount, false, false);
                this.tickDuration();
                return;
            }
            for (int i = 0; i < this.amount; i++) { //else select targets randomly
            	AbstractCard c = p.hand.getRandomCard(AbstractDungeon.cardRandomRng);
            	electrifyCard(c, electrifyAmount);
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c2 : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
            	electrifyCard(c2, electrifyAmount);
            	p.hand.addToBottom(c2);
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        this.tickDuration();
    }
    
    private void electrifyCard(AbstractCard card, int times) {
        for (int i = 0; i < times; i++) {
        	addToBot(new ElectrifySpecificCardAction(card));
        }
    }
}
