package sts.caster.actions;


import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class StaticFieldAction extends AbstractGameAction {
	public static final String[] TEXT = (CardCrawlGame.languagePack.getUIString("BetterToHandAction")).TEXT;

	private AbstractPlayer player;

	private int numberOfCards;

	private boolean optional;

	private boolean setCost;

	public StaticFieldAction(int numberOfCards, boolean optional) {
		this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.player = AbstractDungeon.player;
		this.numberOfCards = numberOfCards;
		this.optional = optional;
		this.setCost = false;
	}

	public StaticFieldAction(int numberOfCards) {
		this(numberOfCards, false);
	}

	public void update() {
		if (this.duration == this.startDuration) {
			if (this.player.discardPile.isEmpty() || this.numberOfCards <= 0) {
				this.isDone = true;
				return;
			}
			if (this.player.discardPile.size() <= this.numberOfCards && !this.optional) {
				ArrayList<AbstractCard> cardsToMove = new ArrayList<>();
				for (AbstractCard c : this.player.discardPile.group)
					cardsToMove.add(c);
				for (AbstractCard c : cardsToMove) {
					if (this.player.hand.size() < 10) {
						this.player.hand.addToHand(c);
						this.player.discardPile.removeCard(c);
						addToTop(new ElectrifySpecificCardAction(c));
					}
					c.lighten(false);
					c.applyPowers();
				}
				this.isDone = true;
				return;
			}
			if (this.numberOfCards == 1) {
				if (this.optional) {
					AbstractDungeon.gridSelectScreen.open(this.player.discardPile, this.numberOfCards, true, TEXT[0]);
				} else {
					AbstractDungeon.gridSelectScreen.open(this.player.discardPile, this.numberOfCards, TEXT[0], false);
				}
			} else if (this.optional) {
				AbstractDungeon.gridSelectScreen.open(this.player.discardPile, this.numberOfCards, true, TEXT[1] + this.numberOfCards + TEXT[2]);
			} else {
				AbstractDungeon.gridSelectScreen.open(this.player.discardPile, this.numberOfCards, TEXT[1] + this.numberOfCards + TEXT[2], false);
			}
			tickDuration();
			return;
		}
		if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
			for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
				if (this.player.hand.size() < 10) {
					this.player.hand.addToHand(c);
					this.player.discardPile.removeCard(c);
					addToTop(new ElectrifySpecificCardAction(c));
				}
				c.lighten(false);
				c.unhover();
				c.applyPowers();
			}
			for (AbstractCard c : this.player.discardPile.group) {
				c.unhover();
				c.target_x = CardGroup.DISCARD_PILE_X;
				c.target_y = 0.0F;
			}
			AbstractDungeon.gridSelectScreen.selectedCards.clear();
			AbstractDungeon.player.hand.refreshHandLayout();
		}
		tickDuration();
		if (this.isDone)
			for (AbstractCard c : this.player.hand.group)
				c.applyPowers();
	}
}
