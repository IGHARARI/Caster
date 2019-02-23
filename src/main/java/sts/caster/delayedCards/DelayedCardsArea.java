package sts.caster.delayedCards;

import java.util.ArrayList;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.actions.QueueRedrawMiniCardsAction;

public class DelayedCardsArea {
	public static ArrayList<DelayedCardEffect> delayedCards;

	public static final float WAIT_TIME_BETWEEN_DELAYED_EFFECTS = 1.1f;
	public static final float CARD_AREA_X_RIGHT_OFFSET = 200f * Settings.scale;
	public static final float VERT_SPACE_BTWN_CARDS = 65f * Settings.scale;
	public static final float CARD_AREA_COLUMN_WIDTH = 100f * Settings.scale;
	public static final float CARD_AREA_COLUMN_SPACER = 10f * Settings.scale;
	public static final float CARD_AREA_COLUMN_HEIGH = 280f * Settings.scale;
	private static final int MAX_CARDS_PER_COLUMN = 5;
	
	public static void initializeCardArea() {
		delayedCards = new ArrayList<DelayedCardEffect>();
	}
	
	public static void repositionMiniCards() {
		for (int turnsRemaining = 1 ; turnsRemaining < 4; turnsRemaining++) {
			int columnIndex = 0;
			for (DelayedCardEffect card : delayedCards) {
				if (card.turnsUntilFire == turnsRemaining) {
					positionCardInArea(turnsRemaining, columnIndex, card);
					columnIndex++;
				}
			}
		}
		int columnIndex = 0;
		for (DelayedCardEffect card : delayedCards) {
			if (card.turnsUntilFire > 3) {
				positionCardInArea(4, columnIndex, card);
				columnIndex++;
			}
		}
	}
	
	public static void removeCardFromArea(DelayedCardEffect card) {
		delayedCards.remove(card);
		AbstractDungeon.actionManager.addToBottom(new QueueRedrawMiniCardsAction());
	}
	
	public static void addCardToArea(DelayedCardEffect card) {
		delayedCards.add(card);
		if (card.turnsUntilFire == 0) {
			card.evokeCardEffect();
			removeCardFromArea(card);
		}
		AbstractDungeon.actionManager.addToBottom(new QueueRedrawMiniCardsAction());
	}
	
	public static void positionCardInArea(int columnNumber, int indexInColumn, DelayedCardEffect card) {
        final float rightBorder = AbstractDungeon.player.drawX + CARD_AREA_X_RIGHT_OFFSET;
        float columnXOffset = 0;
        if (indexInColumn >= MAX_CARDS_PER_COLUMN) {
        	columnXOffset = CARD_AREA_COLUMN_WIDTH/2f;
        	indexInColumn %= MAX_CARDS_PER_COLUMN;
        }
        card.tX = rightBorder - (CARD_AREA_COLUMN_WIDTH + CARD_AREA_COLUMN_SPACER) * (columnNumber-1) - columnXOffset;
        card.tY = AbstractDungeon.player.drawY + AbstractDungeon.player.hb_h + 20f*Settings.scale + VERT_SPACE_BTWN_CARDS * indexInColumn;
        card.hb.move(card.tX, card.tY);
    }
}
