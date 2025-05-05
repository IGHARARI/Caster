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
import sts.caster.delayedCards.SpellIntentsManager;
import sts.caster.delayedCards.SpellPredictionIntent;

import java.util.ArrayList;


public class SpellPredictionIntentPatch {

	
	@SpirePatch(clz=AbstractPlayer.class, method="render", paramtypez = {SpriteBatch.class})
	public static class playerRenderPatch {
		
		public static void Postfix(AbstractPlayer __instance, SpriteBatch sb) {
			
			if ((AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT || AbstractDungeon.getCurrRoom() instanceof MonsterRoom) && !__instance.isDead) {
				if (SpellIntentsManager.spellIntents != null) {
					for (final SpellPredictionIntent spellIntent : SpellIntentsManager.spellIntents) {
						spellIntent.updateAnimation();
						spellIntent.render(sb);
						spellIntent.renderReticleIfHovered(sb);
					}
				}
			}
		}
	}

	@SpirePatch(clz=AbstractPlayer.class, method="preBattlePrep", paramtypez = {})
	public static class preBattlePrepPatch {

		public static void Prefix(AbstractPlayer __instance) {
			SpellIntentsManager.initializeIntents();
		}
	}
}
