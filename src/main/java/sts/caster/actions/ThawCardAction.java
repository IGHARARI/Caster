package sts.caster.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import sts.caster.core.frozenpile.FrozenPileManager;
import sts.caster.powers.ThermodynamicsPower;

import java.util.ArrayList;

public class ThawCardAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ThawCardAction");
    public static final String[] TEXT = ThawCardAction.uiStrings.TEXT;
    private AbstractPlayer p;
    private boolean isRandom;
    private boolean anyNumber;
    private AbstractCard specificCard;

    public ThawCardAction(final AbstractCard card) {
        this.specificCard = card;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
    }

    public ThawCardAction(final int amount, final boolean isRandom) {
        this(amount, isRandom, false);
    }
    
    public ThawCardAction(final int amount, final boolean isRandom, final boolean anyNumber) {
        this.anyNumber = anyNumber;
        this.p = AbstractDungeon.player;
        this.isRandom = isRandom;
        this.amount = amount;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
    }

    public ThawCardAction(final int amount, final boolean isRandom, final boolean anyNumber, final boolean canPickZero) {
        this.anyNumber = anyNumber;
        this.p = AbstractDungeon.player;
        this.isRandom = isRandom;
        this.amount = amount;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
    }

    @Override
    public void update() {
        if (specificCard != null) {
            if (FrozenPileManager.frozenPile.contains((specificCard))){
                thawSpecificCard(specificCard);
            }
            p.hand.refreshHandLayout();
            isDone = true;
            return;
        }
        if (duration == Settings.ACTION_DUR_FAST) {
            if (FrozenPileManager.frozenPile.size() == 0) {
                isDone = true;
                return;
            }
            if (!anyNumber && FrozenPileManager.frozenPile.size() <= amount) {
                //I copy the list to avoid CME or any visual bugs 
                ArrayList<AbstractCard> cardsCopy = new ArrayList<AbstractCard>(FrozenPileManager.frozenPile.group);
                for (AbstractCard card : cardsCopy) {
                    thawSpecificCard(card);
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                p.hand.refreshHandLayout();
                isDone = true;
                return;
            }
            if (!isRandom) {        // Thaw  +        ?(up to)           +   #N   +           (card./cards.)
                String thawMessage = TEXT[0] + (anyNumber? TEXT[2] : "") + amount + (amount>1? TEXT[3] : TEXT[1]);
                AbstractDungeon.gridSelectScreen.open(FrozenPileManager.frozenPile, amount, anyNumber, thawMessage);
                AbstractDungeon.gridSelectScreen.forClarity = false;
                tickDuration();
                return;
            }
            for (int j = 0; j < amount; ++j) {
            	thawSpecificCard(FrozenPileManager.frozenPile.getRandomCard(AbstractDungeon.cardRandomRng));
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            p.hand.refreshHandLayout();
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (final AbstractCard c2 : AbstractDungeon.gridSelectScreen.selectedCards) {
            	thawSpecificCard(c2);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            p.hand.refreshHandLayout();
        }
        this.tickDuration();
    }
    
    private void thawSpecificCard(AbstractCard c) {
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
        if (AbstractDungeon.player.hasPower(ThermodynamicsPower.POWER_ID)) {
            ((ThermodynamicsPower)AbstractDungeon.player.getPower(ThermodynamicsPower.POWER_ID)).onThawCard();
        }
    }
}
