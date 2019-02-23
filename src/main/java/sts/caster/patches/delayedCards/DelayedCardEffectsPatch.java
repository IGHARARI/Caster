package sts.caster.patches.delayedCards;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
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
import sts.caster.delayedCards.DelayedCardEffect;
import sts.caster.delayedCards.DelayedCardsArea;


public class DelayedCardEffectsPatch {

	@SpirePatch(clz=AbstractPlayer.class, method="combatUpdate", paramtypez = {})
	public static class inCombatUpdatePatch {

		public static void Postfix(AbstractPlayer __instance) {
			if (DelayedCardsArea.delayedCards != null) {
				for (DelayedCardEffect card : DelayedCardsArea.delayedCards) {
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
				if (DelayedCardsArea.delayedCards != null) {
					for (final DelayedCardEffect orbCard : DelayedCardsArea.delayedCards) {
						orbCard.render(sb);
					}
					DelayedCardEffect hoveredCard = null;
					for (final DelayedCardEffect orbCard : DelayedCardsArea.delayedCards) {
						if (orbCard.renderPreviewIfHovered(sb)) hoveredCard = orbCard;
					}
					float hovXLeft = 0f, hovXRight = 0f, hovYTop = 0f, hovYBot = 0f;
					if (hoveredCard != null) {
						hovXLeft = hoveredCard.hb.cX - AbstractCard.IMG_WIDTH_S*Settings.scale/2f;
						hovXRight = hoveredCard.hb.cX + AbstractCard.IMG_WIDTH_S*Settings.scale/2f;
						hovYTop = hoveredCard.hb.cY + AbstractCard.IMG_HEIGHT_S*Settings.scale/2f;
						hovYBot = hoveredCard.hb.cY - AbstractCard.IMG_HEIGHT_S*Settings.scale/2f;
					}
					for (final DelayedCardEffect orbCard : DelayedCardsArea.delayedCards) {
						if (hoveredCard != null && 
								(hovXLeft < orbCard.hb.cX  && orbCard.hb.cX < hovXRight) && 
									(hovYBot < orbCard.hb.cY  && orbCard.hb.cY < hovYTop)) {
							continue;
						}
						//^avoid rendering the effects for cards being obscured by the pop up
						orbCard.updateAnimation();
					}
				}
			}
		}
	}
	
	@SpirePatch(clz=AbstractPlayer.class, method="preBattlePrep", paramtypez = {})
	public static class preBattlePrepPatch {
		
		public static void Prefix(AbstractPlayer __instance) {
			DelayedCardsArea.initializeCardArea();
		}
	}
	
	@SpirePatch(clz=GameActionManager.class, method="getNextAction")
	public static class onStartOfTurnTriggerPatch {
		
		@SpireInsertPatch(locator=Locator.class, localvars={})
		public static void Insert(GameActionManager __instance) {
			for (final DelayedCardEffect orbCard : DelayedCardsArea.delayedCards) {
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
