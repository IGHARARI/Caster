package sts.caster.actions;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import basemod.BaseMod;
import sts.caster.core.frozenpile.FrozenPileManager;

public class ThawCardAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ExhaustAction");
    public static final String[] TEXT = ThawCardAction.uiStrings.TEXT;
    private AbstractPlayer p;
    private boolean isRandom;
    private boolean anyNumber;
    public static int numExhausted;
    
    public ThawCardAction(final int amount, final boolean isRandom) {
        this(amount, isRandom, false, false);
    }
    
    public ThawCardAction(final int amount, final boolean isRandom, final boolean anyNumber, final boolean canPickZero) {
        this.anyNumber = anyNumber;
        this.p = AbstractDungeon.player;
        this.isRandom = isRandom;
        this.amount = amount;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
    }
    
    public ThawCardAction(final int amount, final boolean isRandom, final boolean anyNumber) {
        this(amount, isRandom, anyNumber, false);
    }
    
    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            if (FrozenPileManager.frozenPile.size() == 0) {
                isDone = true;
                return;
            }
            if (!anyNumber && FrozenPileManager.frozenPile.size() <= amount) {
                //I copy the list to avoid CME or any visual bugs 
                ArrayList<AbstractCard> cardsCopy = new ArrayList<AbstractCard>(FrozenPileManager.frozenPile.group);
                for (AbstractCard card : cardsCopy) {
                    moveToHand(card);
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                p.hand.refreshHandLayout();
                isDone = true;
                return;
            }
            if (!isRandom) {
                AbstractDungeon.gridSelectScreen.open(FrozenPileManager.frozenPile, amount, anyNumber, "Thaw cards"); 
                tickDuration();
                return;
            }
            for (int j = 0; j < amount; ++j) {
            	moveToHand(FrozenPileManager.frozenPile.getRandomCard(AbstractDungeon.cardRandomRng));
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            p.hand.refreshHandLayout();
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (final AbstractCard c2 : AbstractDungeon.gridSelectScreen.selectedCards) {
            	moveToHand(c2);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            p.hand.refreshHandLayout();
        }
        this.tickDuration();
    }
    
    private void moveToHand(AbstractCard c) {
        c.stopGlowing();
        if (p.hand.size() < BaseMod.MAX_HAND_SIZE) {
        	p.hand.addToHand(c);
        } else {
        	AbstractDungeon.player.createHandIsFullDialog();
        	c.untip();
        	p.discardPile.addToRandomSpot(c);
        }
        FrozenPileManager.frozenPile.removeCard(c);
        c.unhover();
        c.fadingOut = false;
    }
}
