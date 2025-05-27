package sts.caster.actions;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import sts.caster.cards.mods.FrozenCardMod;
import sts.caster.core.freeze.FreezeHelper;

import java.util.ArrayList;
import java.util.List;

public class ThawCardAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ThawCardAction");
    public static final String[] TEXT = ThawCardAction.uiStrings.TEXT;
    private AbstractPlayer p;
    private boolean isRandom;
    private boolean anyNumber;
    private AbstractCard specificCard;
    private String thawSourceText;

    public ThawCardAction(final AbstractCard card) {
        this.specificCard = card;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
    }

    public ThawCardAction(final int amount, final boolean isRandom, final String thawSourceText) {
        this(amount, isRandom, false, thawSourceText);
    }

    public ThawCardAction(final int amount, final boolean isRandom, final boolean anyNumber, final String thawSourceText) {
        this.anyNumber = anyNumber;
        this.p = AbstractDungeon.player;
        this.isRandom = isRandom;
        this.amount = amount;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
        this.thawSourceText = thawSourceText;
    }

    @Override
    public void update() {
        if (specificCard != null) {
            if (CardModifierManager.hasModifier(specificCard, FrozenCardMod.ID)){
                thawSpecificCard(specificCard);
            }
            p.hand.refreshHandLayout();
            isDone = true;
            return;
        }
        if (duration == Settings.ACTION_DUR_FAST) {
            List<AbstractCard> frozenInHand = FreezeHelper.getFrozenCardsForPile(AbstractDungeon.player.hand);
            CardGroup frozenHandGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            frozenHandGroup.group.addAll(frozenInHand);
            if (frozenInHand.size() == 0) {
                isDone = true;
                return;
            }
            if (!anyNumber && frozenInHand.size() <= amount) {
                //I copy the list to avoid CME or any visual bugs
                // EDIT: Revision, shouldn't this just addToBot a bunch of ThawCardActions for each card?
                ArrayList<AbstractCard> cardsCopy = new ArrayList<AbstractCard>(frozenInHand);
                for (AbstractCard card : cardsCopy) {
                    thawSpecificCard(card);
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                p.hand.refreshHandLayout();
                isDone = true;
                return;
            }
            String cardName = thawSourceText != null ? thawSourceText + ": " : "";
            if (!isRandom) {       //[Card Name]: Thaw  +        ?(up to)           +   #N   +           (card./cards.)
                String thawMessage = cardName + TEXT[0] + (anyNumber? TEXT[2] : "") + amount + (amount>1? TEXT[3] : TEXT[1]);
                AbstractDungeon.gridSelectScreen.open(frozenHandGroup, amount, anyNumber, thawMessage);
                AbstractDungeon.gridSelectScreen.forClarity = false;
                tickDuration();
                return;
            }
            for (int j = 0; j < amount; ++j) {
                thawSpecificCard(frozenHandGroup.getRandomCard(AbstractDungeon.cardRandomRng));
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
        CardModifierManager.removeModifiersById(c, FrozenCardMod.ID, true);
        c.unhover();
        c.fadingOut = false;
    }
}
