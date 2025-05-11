package sts.caster.patches.frozenpile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import sts.caster.core.frozenpile.FrozenCardsPanel;
import sts.caster.core.frozenpile.FrozenPileManager;

@Deprecated
public class FrozenPileRenderingPatch {

	@SpirePatch(clz=AbstractDungeon.class, method="update")
	public static class UpdatePatch {
		
		@SpireInsertPatch(locator=Locator.class, localvars={})
		public static void Insert(AbstractDungeon __instance) {
	        if (AbstractDungeon.screen.equals(FrozenPileEnums.FROZEN_VIEW)) {
	        	FrozenPileManager.frozenPileViewScreen.update();
	        }
		}
		
		private static class Locator extends SpireInsertLocator {

			@Override
			public int[] Locate(CtBehavior ctMethod) throws CannotCompileException, PatchingException {
				Matcher finalMatcher = new Matcher.FieldAccessMatcher(com.megacrit.cardcrawl.dungeons.AbstractDungeon.class, "turnPhaseEffectActive");
				return LineFinder.findInOrder(ctMethod, new ArrayList<Matcher>(), finalMatcher);
			}
			
		}
	}
	
	@SpirePatch(clz=AbstractDungeon.class, method="openPreviousScreen", paramtypez = {CurrentScreen.class})
	public static class OpenPreviousScreenPatch {

		public static void Postfix(CurrentScreen __s) {
			if (__s.equals(FrozenPileEnums.FROZEN_VIEW)) {
				FrozenPileManager.frozenPileViewScreen.reopen();
			}
		}
	}
	
	@SpirePatch(clz=AbstractDungeon.class, method="render", paramtypez = {SpriteBatch.class})
	public static class RenderPatch {
		
		public static void Postfix(AbstractDungeon __instance, SpriteBatch __sb) {
			if (AbstractDungeon.screen.equals(FrozenPileEnums.FROZEN_VIEW)) {
				FrozenPileManager.frozenPileViewScreen.render(__sb);
			}
		}
	}
	
	@SpirePatch(clz=OverlayMenu.class, method=SpirePatch.CONSTRUCTOR, paramtypez = {AbstractPlayer.class})
	public static class FrozenPanelCreatePatch {
		
		public static void Postfix(OverlayMenu __instance, AbstractPlayer __player) {
			FrozenPileManager.frozenPanel = new FrozenCardsPanel();
		}
	}
	
	@SpirePatch(clz=OverlayMenu.class, method="update", paramtypez = {})
	public static class FrozenPanelUpdatePositionsPatch {
		
		public static void Postfix(OverlayMenu __instance) {
			FrozenPileManager.frozenPanel.updatePositions();
		}
	}
	
	@SpirePatch(clz=OverlayMenu.class, method="render", paramtypez = {SpriteBatch.class})
	public static class FrozenPanelRenderPatch {
		
		public static void Postfix(OverlayMenu __instance, SpriteBatch __sb) {
			FrozenPileManager.frozenPanel.render(__sb);
		}
	}
	
	@SpirePatch(clz=AbstractPlayer.class, method=SpirePatch.CONSTRUCTOR, paramtypez = {String.class, PlayerClass.class})
	public static class FrozenPileCreatePatch {
		
		public static void Postfix(AbstractPlayer __instance, String __name, PlayerClass __class) {
			FrozenPileManager.frozenPile = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
		}
	}
	
	@SpirePatch(clz=AbstractPlayer.class, method="combatUpdate", paramtypez = {})
	public static class FrozenPileCombatUpdatePatch {
		
		public static void Postfix(AbstractPlayer __instance) {
			FrozenPileManager.frozenPile.update();
		}
	}
	
	@SpirePatch(clz=AbstractPlayer.class, method="preBattlePrep", paramtypez = {})
	public static class FrozenPilePreBattlePrepPatch {
		
		public static void Prefix(AbstractPlayer __instance) {
			FrozenPileManager.frozenPile.clear();
		}
	}
	
	@SpirePatch(clz=AbstractDungeon.class, method="resetPlayer", paramtypez = {})
	public static class FrozenPileResetPlayerPatch {
		
		public static void Prefix() {
			FrozenPileManager.frozenPile.clear();
		}
	}
	
	@SpirePatch(clz=AbstractDungeon.class, method="closeCurrentScreen", paramtypez = {})
	public static class FrozenPileClosePilePatch {
		
		public static void Prefix() {
			if (AbstractDungeon.screen.equals(FrozenPileEnums.FROZEN_VIEW)) {
                AbstractDungeon.overlayMenu.cancelButton.hide();
                if (AbstractDungeon.previousScreen == null) {
                    if (AbstractDungeon.player.isDead) {
                        AbstractDungeon.previousScreen = CurrentScreen.DEATH;
                    }
                    else {
                        AbstractDungeon.isScreenUp = false;
                        AbstractDungeon.overlayMenu.hideBlackScreen();
                    }
                }
                if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.player.isDead) {
                    AbstractDungeon.overlayMenu.showCombatPanels();
                }
			}
		}
	}
	
	@SpirePatch(clz=GetAllInBattleInstances.class, method="get")
	public static class FrozenPileGetInstancesPatch {
		
		@SpireInsertPatch(locator=Locator.class, localvars={"cards"})
		public static void Insert(UUID __uuid, @ByRef HashSet<AbstractCard>[] __cards) {
	        for (final AbstractCard c : FrozenPileManager.frozenPile.group) {
	            if (!c.uuid.equals(__uuid)) {
	                continue;
	            }
	            __cards[0].add(c);
	        }
		}
		
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethod) throws CannotCompileException, PatchingException {
				Matcher finalMatcher = new Matcher.FieldAccessMatcher(com.megacrit.cardcrawl.characters.AbstractPlayer.class, "drawPile");
				return LineFinder.findInOrder(ctMethod, new ArrayList<Matcher>(), finalMatcher);
			}
		}
	}
	
}
