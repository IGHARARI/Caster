package sts.caster.patches.delayedCards;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import sts.caster.actions.DelayedEffectOnStartOfTurnTriggerAction;
import sts.caster.actions.QueueRedrawMiniCardsAction;
import sts.caster.delayedCards.CastingSpellCard;
import sts.caster.delayedCards.SpellCardsArea;

import java.util.ArrayList;


public class DelayedCardEffectsPatch {

	@SpirePatch(clz=AbstractPlayer.class, method="combatUpdate", paramtypez = {})
	public static class inCombatUpdatePatch {

		public static void Postfix(AbstractPlayer __instance) {
			if (SpellCardsArea.spellCardsBeingCasted != null) {
				for (CastingSpellCard card : SpellCardsArea.spellCardsBeingCasted) {
					card.update();
				}
			}
		}
	}
	
//	@SpirePatch(clz=AbstractPlayer.class, method="update", paramtypez = {})
//	public static class inCombatUpdateAnimationPatch {
//		
//		public static void Postfix(AbstractPlayer __instance) {
//			if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.EVENT) {
//				if (AbstractDungeon.player instanceof TheCaster) {
//					for (DelayedCard card : ((TheCaster)AbstractDungeon.player).delayedCards) {
//						card.updateAnimation();
//					}
//				}
//			}
//		}
//	}
	
	@SpirePatch(clz=AbstractPlayer.class, method="render", paramtypez = {SpriteBatch.class})
	public static class playerRenderPatch {
		
		public static void Postfix(AbstractPlayer __instance, SpriteBatch sb) {
			
			if ((AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT || AbstractDungeon.getCurrRoom() instanceof MonsterRoom) && !__instance.isDead) {
				if (SpellCardsArea.spellCardsBeingCasted != null) {
					for (final CastingSpellCard spellCard : SpellCardsArea.spellCardsBeingCasted) {
						spellCard.render(sb);
					}
					for (final CastingSpellCard spellCard : SpellCardsArea.cardsBeingEvoked) {
						spellCard.render(sb);
					}
					CastingSpellCard hoveredCard = null;
					for (final CastingSpellCard spellCard : SpellCardsArea.spellCardsBeingCasted) {
						if (spellCard.renderPreviewIfHovered(sb)) hoveredCard = spellCard;
					}
					float hovXLeft = 0f, hovXRight = 0f, hovYTop = 0f, hovYBot = 0f;
					if (hoveredCard != null) {
						hovXLeft = hoveredCard.hb.cX - AbstractCard.IMG_WIDTH_S*Settings.scale/2f;
						hovXRight = hoveredCard.hb.cX + AbstractCard.IMG_WIDTH_S*Settings.scale/2f;
						hovYTop = hoveredCard.hb.cY + AbstractCard.IMG_HEIGHT_S*Settings.scale/2f;
						hovYBot = hoveredCard.hb.cY - AbstractCard.IMG_HEIGHT_S*Settings.scale/2f;
					}
					for (final CastingSpellCard orbCard : SpellCardsArea.spellCardsBeingCasted) {
						if (hoveredCard != null && 
								(hovXLeft < orbCard.hb.cX  && orbCard.hb.cX < hovXRight) && 
									(hovYBot < orbCard.hb.cY  && orbCard.hb.cY < hovYTop)) {
							continue;
						}
						//^avoid rendering the effects for cards being obscured by the pop up
						orbCard.updateAnimation();
					}
					for (final CastingSpellCard orbCard : SpellCardsArea.cardsBeingEvoked) {
						orbCard.updateAnimation();
					}
				}
			}
		}
	}
	
	@SpirePatch(clz=AbstractPlayer.class, method="preBattlePrep", paramtypez = {})
	public static class preBattlePrepPatch {
		
		public static void Prefix(AbstractPlayer __instance) {
			SpellCardsArea.initializeCardArea();
		}
	}
	
	@SpirePatch(clz=GameActionManager.class, method="getNextAction")
	public static class onStartOfTurnTriggerPatch {
		
		@SpireInsertPatch(locator=Locator.class, localvars={})
		public static void Insert(GameActionManager __instance) {
			for (final CastingSpellCard orbCard : SpellCardsArea.spellCardsBeingCasted) {
				AbstractDungeon.actionManager.addToBottom(new DelayedEffectOnStartOfTurnTriggerAction(orbCard));
			}
			AbstractDungeon.actionManager.addToBottom(new QueueRedrawMiniCardsAction());
		}
		
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethod) throws CannotCompileException, PatchingException {
				Matcher finalMatcher = new Matcher.MethodCallMatcher("com.megacrit.cardcrawl.characters.AbstractPlayer", "applyStartOfTurnPostDrawPowers");
				return LineFinder.findInOrder(ctMethod, new ArrayList<Matcher>(), finalMatcher);
			}
		}
	}
}
