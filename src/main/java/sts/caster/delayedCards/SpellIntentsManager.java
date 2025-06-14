package sts.caster.delayedCards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.LogManager;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpellIntentsManager {
	public static ArrayList<SpellPredictionIntent> spellIntents;

	public static final float CARD_AREA_X_RIGHT_OFFSET = 200f * Settings.scale;
	public static final float INTENT_COLUMN_HEIGHT = 65f * Settings.scale;
	public static final float INTENT_COLUMN_WIDTH = 85f * Settings.scale;

	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(CasterMod.class.getName());

	private static CasterCard makePowerAppliedCopy(CastingSpellCard card) {
		CasterCard cardCopy = card.spellCard.makeStatIdenticalCopy();
		cardCopy.calculateCardDamage(card.target);
		cardCopy.applyPowers();
		return cardCopy;
	}

	public static void initializeIntents() {
		spellIntents = new ArrayList<>();
	}

	public static void refreshSpellIntents() {
		if (SpellCardsArea.spellCardsBeingCasted != null) {
			List<CastingSpellCard> cardsToCastNext = SpellCardsArea.spellCardsBeingCasted.stream()
					.filter(castingSpellCard -> castingSpellCard.turnsUntilFire == 1)
					.collect(Collectors.toList());

			// Group spells that target ENEMY by monster
			Map<AbstractMonster, List<CastingSpellCard>> enemyTargetCardsMap = cardsToCastNext.stream()
					.filter(card -> card.spellCard.target == AbstractCard.CardTarget.ENEMY)
					.collect(Collectors.groupingBy(card -> card.target));

			// Spells that target ALL_ENEMY
			List<CastingSpellCard> allEnemyTargetCards = cardsToCastNext.stream()
					.filter(card -> card.spellCard.target == AbstractCard.CardTarget.ALL_ENEMY)
					.collect(Collectors.toList());

			// Spells that target SELF
			List<CastingSpellCard> selfTargetCards = cardsToCastNext.stream()
					.filter(card -> card.spellCard.target == AbstractCard.CardTarget.SELF)
					.collect(Collectors.toList());

			ArrayList<SpellPredictionIntent> intents = new ArrayList<SpellPredictionIntent>();
			boolean needsUpdate = false;
			int intentRowIndex = 0;
			int monsterTargetIndex = 1;

			List<AbstractMonster> enemiesOrderedByX = enemyTargetCardsMap.keySet().stream()
					.sorted(Comparator.comparing(monster -> monster.hb.x))
					.collect(Collectors.toList());
			for (AbstractMonster targetMonster : enemiesOrderedByX) {
				List<CastingSpellCard> cards = enemyTargetCardsMap.get(targetMonster);
				int damageAmount = cards.stream().mapToInt(card -> {
					CasterCard cardCopy = makePowerAppliedCopy(card);
					return cardCopy.getIntentNumber();
				}).sum();
				if (!needsUpdate) {
					needsUpdate = isSpellIntentChanged(damageAmount, targetMonster, SpellPredictionIntent.SpellIntentType.ATTACK);
				}
				if (damageAmount > 0) {
					SpellPredictionIntent damageIntent = new SpellPredictionIntent(damageAmount, targetMonster, SpellPredictionIntent.SpellIntentType.ATTACK);
					positionSpellIntent(damageIntent, monsterTargetIndex, intentRowIndex);
					monsterTargetIndex++;
					intents.add(damageIntent);
				}
			}
			if (!intents.isEmpty()) {
				intentRowIndex++;
			}

			int allEnemyDamageAmount = allEnemyTargetCards.stream().mapToInt(card -> {
				CasterCard cardCopy = makePowerAppliedCopy(card);
				return cardCopy.getIntentNumber();
			}).sum();
			if (!needsUpdate) {
				needsUpdate = isSpellIntentChanged(allEnemyDamageAmount, SpellPredictionIntent.SpellIntentType.ATTACK_ALL);
			}
			if (allEnemyDamageAmount > 0) {
				SpellPredictionIntent allEnemyPredictionIntent = new SpellPredictionIntent(allEnemyDamageAmount, SpellPredictionIntent.SpellIntentType.ATTACK_ALL);
				positionSpellIntent(allEnemyPredictionIntent, 1, intentRowIndex);
				intents.add(allEnemyPredictionIntent);
				intentRowIndex++;
			}

			int selfBlockAmount = selfTargetCards.stream().mapToInt(card -> {
				CasterCard cardCopy = makePowerAppliedCopy(card);
				return cardCopy.getIntentNumber();
			}).sum();
			if (!needsUpdate) {
				needsUpdate = isSpellIntentChanged(selfBlockAmount, SpellPredictionIntent.SpellIntentType.BLOCK);
			}
			if (selfBlockAmount > 0) {
				SpellPredictionIntent blockPredictionIntent = new SpellPredictionIntent(selfBlockAmount, SpellPredictionIntent.SpellIntentType.BLOCK);
				positionSpellIntent(blockPredictionIntent, 1, intentRowIndex);
				intents.add(blockPredictionIntent);
			}

//			SpellPredictionIntent mockIntent = new SpellPredictionIntent(damageMock);
//			positionSpellIntent(mockIntent);
//			intents.add(mockIntent);
//			logger.info("adding intent with mock dmg " + damageMock);
			if (needsUpdate) {
				spellIntents = intents;
			}
		}
	}

	private static boolean isSpellIntentChanged(int newAmount, SpellPredictionIntent.SpellIntentType type) {
		List<SpellPredictionIntent> priorIntents = spellIntents.stream().filter(
				intent -> intent.spellIntentType == type
		).collect(Collectors.toList());
		if ((priorIntents.size() == 0 && newAmount != 0) || (priorIntents.size() != 0 && newAmount == 0)) {
			return true;
		}
		if (priorIntents.size() != 0 && priorIntents.get(0).intentAmount != newAmount) {
			return true;
		}
		return false;
	}

	private static boolean isSpellIntentChanged(int newAmount, AbstractCreature target, SpellPredictionIntent.SpellIntentType type) {
		List<SpellPredictionIntent> priorIntents = spellIntents.stream().filter(
				intent -> intent.spellIntentType == type && intent.hoverTarget == target
		).collect(Collectors.toList());
		if ((priorIntents.size() == 0 && newAmount != 0) || (priorIntents.size() != 0 && newAmount == 0)) {
			return true;
		}
		if (priorIntents.size() > 1 || priorIntents.get(0).intentAmount != newAmount) {
			return true;
		}
		return false;
	}

	public static void positionSpellIntent(SpellPredictionIntent spellIntent, int col, int row) {

		spellIntent.cX = AbstractDungeon.player.drawX + CARD_AREA_X_RIGHT_OFFSET + INTENT_COLUMN_WIDTH * col;
		spellIntent.cY = AbstractDungeon.player.drawY + AbstractDungeon.player.hb_h + INTENT_COLUMN_HEIGHT * row;
		spellIntent.hb.move(spellIntent.cX, spellIntent.cY);
		spellIntent.tX = spellIntent.cX;
		spellIntent.tY = spellIntent.cY;
	}
}
