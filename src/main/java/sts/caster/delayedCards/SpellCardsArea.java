package sts.caster.delayedCards;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import sts.caster.actions.QueueRedrawMiniCardsAction;
import sts.caster.cards.CasterCard;

public class SpellCardsArea {
	public static ArrayList<CastingSpellCard> spellCardsBeingCasted;
	public static ArrayList<CastingSpellCard> cardsBeingEvoked;

	public static final float CARD_AREA_X_RIGHT_OFFSET = 200f * Settings.scale;
	public static final float VERT_SPACE_BTWN_CARDS = 65f * Settings.scale;
	public static final float CARD_AREA_COLUMN_WIDTH = 100f * Settings.scale;
	public static final float CARD_AREA_COLUMN_SPACER = 10f * Settings.scale;
	public static final float CARD_AREA_COLUMN_HEIGH = 280f * Settings.scale;
	private static final int MAX_CARDS_PER_COLUMN = 5;

	public static final float EVOKE_AREA_RIGHT_BORDER_OFFSET = 700f * Settings.scale;
	public static final float EVOKE_AREA_COLUMN_SPACER = 60f * Settings.scale;
	public static final float EVOKE_AREA_STACK_OFFSET = 5f * Settings.scale;
	public static final float EVOKE_BIGCARD_SPACER = 100f * Settings.scale;
	public static final float EVOKE_CARD_TARGET_SCALE = 0.5f;

	public static void initializeCardArea() {
		spellCardsBeingCasted = new ArrayList<CastingSpellCard>();
		cardsBeingEvoked = new ArrayList<CastingSpellCard>();
	}

	public static void repositionMiniCards() {
		for (int turnsRemaining = 1 ; turnsRemaining < 4; turnsRemaining++) {
			int indexInColumn = 0;
			for (CastingSpellCard card : spellCardsBeingCasted) {
				if (card.turnsUntilFire == turnsRemaining) {
					positionCardInCardArea(turnsRemaining, indexInColumn, card);
					indexInColumn++;
				}
			}
		}
		int columnIndex = 0;
		for (CastingSpellCard card : spellCardsBeingCasted) {
			if (card.turnsUntilFire > 3) {
				positionCardInCardArea(4, columnIndex, card);
				columnIndex++;
			}
		}
	}

	public static void redrawEvokeCards(){
		for (int i = 0; i < cardsBeingEvoked.size(); i++) {
			positionCardInEvokeArea(i, cardsBeingEvoked.get(i));
		}
	}
	
	public static void removeCardFromArea(CastingSpellCard card) {
		spellCardsBeingCasted.remove(card);
		AbstractDungeon.actionManager.addToBottom(new QueueRedrawMiniCardsAction());
		SpellIntentsManager.refreshSpellIntents();
	}
	
	public static void addCardToArea(CastingSpellCard card) {
		spellCardsBeingCasted.add(card);
		if (card.turnsUntilFire == 0) {
			card.evokeCardEffect();
			removeCardFromArea(card);
		}
		AbstractDungeon.actionManager.addToBottom(new QueueRedrawMiniCardsAction());
		SpellIntentsManager.refreshSpellIntents();
	}
	
	public static void positionCardInCardArea(int columnNumber, int indexInColumn, CastingSpellCard card) {
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

	public static void positionCardInEvokeArea(int index, CastingSpellCard card) {
        float rightBorder = AbstractDungeon.player.drawX + EVOKE_AREA_RIGHT_BORDER_OFFSET;
        if (index == 0) {
			card.cardMiniCopy.targetDrawScale = EVOKE_CARD_TARGET_SCALE;
			rightBorder += EVOKE_BIGCARD_SPACER;
		}
		card.tY = AbstractDungeon.player.drawY + AbstractDungeon.player.hb_h + 20f*Settings.scale;

		if (index < 3) {
			card.tX = rightBorder - EVOKE_AREA_COLUMN_SPACER * index;
		} else {
			card.tX = rightBorder - (EVOKE_AREA_COLUMN_SPACER * 3 + EVOKE_AREA_STACK_OFFSET * (index-3));
		}

		card.hb.move(card.tX, card.tY);
	}

	public static CasterCard getLastSpellForDelay(int delayTurns) {
		CasterCard lastDelayedCard = null;
		for (CastingSpellCard card : spellCardsBeingCasted) {
			if (card.turnsUntilFire == delayTurns) {
				lastDelayedCard = card.spellCard;
			}
		}
		return lastDelayedCard;
	}
}
