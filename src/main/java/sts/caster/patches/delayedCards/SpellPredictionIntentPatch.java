package sts.caster.patches.delayedCards;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import sts.caster.delayedCards.SpellIntentsManager;
import sts.caster.delayedCards.SpellPredictionIntent;


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
