package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.cards.CasterCardTags;

public class ElectrifyCardAction extends AbstractGameAction {
    private AbstractPlayer p;
    private boolean isRandom = false;
    private int amount;
    
    public ElectrifyCardAction(int amount, boolean isRandom) {
        this.p = (AbstractPlayer)AbstractDungeon.player;
        this.isRandom = isRandom;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.amount = amount;
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
                    c.tags.add(CasterCardTags.ELECTRIFIED);
                }
                return;
            }
            if (!this.isRandom) { //if it's not random, open select screen
                AbstractDungeon.handCardSelectScreen.open(ExhaustAction.TEXT[0], this.amount, false, false);
                this.tickDuration();
                return;
            }
            for (int i = 0; i < this.amount; i++) { //else select targets randomly
            	AbstractCard c = p.hand.getRandomCard(AbstractDungeon.cardRandomRng);
            	c.tags.add(CasterCardTags.ELECTRIFIED);
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c2 : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
            	c2.tags.add(CasterCardTags.ELECTRIFIED);
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        this.tickDuration();
    }
}
