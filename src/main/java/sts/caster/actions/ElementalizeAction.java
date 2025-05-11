package sts.caster.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import sts.caster.cards.CasterCard;
import sts.caster.core.MagicElement;
import sts.caster.patches.spellCardType.CasterCardType;

import java.util.ArrayList;

public class ElementalizeAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ElementalizeAction");
    public static final String[] TEXT = ElementalizeAction.uiStrings.TEXT;
    private AbstractPlayer p;
    
    private ArrayList<AbstractCard> nonSpellCards;
    
    public ElementalizeAction() {
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.ENERGY;
        nonSpellCards = new ArrayList<AbstractCard>();
    }
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p.hand.size() == 0) {
                this.isDone = true;
                return;
            }
            for (AbstractCard card : p.hand.group) {
            	if (card.type != CasterCardType.SPELL) {
            		nonSpellCards.add(card);
            	}
            }
            if (p.hand.group.size() == nonSpellCards.size()) {
            	isDone = true;
            	return;
            }
            p.hand.group.removeAll(nonSpellCards);
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false);
            this.tickDuration();
            return;
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
        	MagicElement chosenElement = null;
            for (final AbstractCard c2 : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
            	chosenElement = ((CasterCard)c2).cardElement;
            	p.hand.addToTop(c2);
            }
            int elementCards = 0;
            for (AbstractCard card : p.hand.group) {
            	if (card instanceof CasterCard) {
            		if (((CasterCard)card).cardElement == chosenElement) elementCards++;
            	}
            }
	        for (final AbstractCard c : nonSpellCards) {
	            p.hand.addToTop(c);
	        }
	        this.p.hand.refreshHandLayout();
            p.gainEnergy(elementCards);
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        this.tickDuration();
    }
}
