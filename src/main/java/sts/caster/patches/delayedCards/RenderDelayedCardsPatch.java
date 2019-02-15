package sts.caster.patches.delayedCards;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;

import sts.caster.characters.TheCaster;
import sts.caster.delayedCards.DelayedCard;


public class RenderDelayedCardsPatch {

	@SpirePatch(clz=AbstractPlayer.class, method="combatUpdate", paramtypez = {})
	public static class inCombatUpdatePatch {

	    public static void Postfix(AbstractPlayer __instance) {
	    	if (__instance instanceof TheCaster) {
	    		for (DelayedCard card : ((TheCaster)__instance).delayedCards) {
	    			card.update();
	    		}
	    	}
		}

	}
	
//	@SpirePatch(clz=AbstractPlayer.class, method="update", paramtypez = {})
//	public static class inCombatUpdateAnimationPatch {
//		
//		public static void Postfix(AbstractPlayer __instance) {
//	        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.EVENT) {
//				if (AbstractDungeon.player instanceof TheCaster) {
//					for (DelayedCard card : ((TheCaster)AbstractDungeon.player).delayedCards) {
//						card.updateAnimation();
//					}
//				}
//	        }
//		}
//	}
	
	@SpirePatch(clz=AbstractPlayer.class, method="render", paramtypez = {SpriteBatch.class})
	public static class playerRenderPatch {
		
		public static void Postfix(AbstractPlayer __instance, SpriteBatch sb) {
			
	    	if (__instance instanceof TheCaster) {
	    		TheCaster caster = (TheCaster) __instance;
		        if ((AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT || AbstractDungeon.getCurrRoom() instanceof MonsterRoom) && !caster.isDead) {
		            if (!caster.delayedCards.isEmpty()) {
		                for (final DelayedCard orbCard : caster.delayedCards) {
		                	orbCard.render(sb);
		                }
		                DelayedCard hoveredCard = null;
		                for (final DelayedCard orbCard : caster.delayedCards) {
		                	if (orbCard.renderIfHovered(sb)) hoveredCard = orbCard;
		                }
		                float hovXLeft = 0f, hovXRight = 0f, hovYTop = 0f, hovYBot = 0f;
		                if (hoveredCard != null) {
		                	hovXLeft = hoveredCard.hb.cX - AbstractCard.IMG_WIDTH_S*Settings.scale/2f;
		                	hovXRight = hoveredCard.hb.cX + AbstractCard.IMG_WIDTH_S*Settings.scale/2f;
		                	hovYTop = hoveredCard.hb.cY + AbstractCard.IMG_HEIGHT_S*Settings.scale/2f;
		                	hovYBot = hoveredCard.hb.cY - AbstractCard.IMG_HEIGHT_S*Settings.scale/2f;
		                }
		                for (final DelayedCard orbCard : caster.delayedCards) {
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
	}
	
	@SpirePatch(clz=AbstractPlayer.class, method="preBattlePrep", paramtypez = {})
	public static class preBattlePrepPatch {
		
		public static void Prefix(AbstractPlayer __instance) {
			if (__instance instanceof TheCaster) {
				TheCaster caster = (TheCaster) __instance;
				caster.delayedCards = new ArrayList<DelayedCard>();
			}
		}
	}
}
