package sts.caster.patches.frozenpile;

@Deprecated
public class DeprecatedFrozenPileRenderingPatch {
//
//	@SpirePatch(clz=AbstractDungeon.class, method="update")
//	public static class UpdatePatch {
//
//		@SpireInsertPatch(locator=Locator.class, localvars={})
//		public static void Insert(AbstractDungeon __instance) {
//	        if (AbstractDungeon.screen.equals(FrozenPileEnums.FROZEN_VIEW)) {
//	        	DeprecatedFrozenPileManager.frozenPileViewScreen.update();
//	        }
//		}
//
//		private static class Locator extends SpireInsertLocator {
//
//			@Override
//			public int[] Locate(CtBehavior ctMethod) throws CannotCompileException, PatchingException {
//				Matcher finalMatcher = new Matcher.FieldAccessMatcher(com.megacrit.cardcrawl.dungeons.AbstractDungeon.class, "turnPhaseEffectActive");
//				return LineFinder.findInOrder(ctMethod, new ArrayList<Matcher>(), finalMatcher);
//			}
//
//		}
//	}
//
//	@SpirePatch(clz=AbstractDungeon.class, method="openPreviousScreen", paramtypez = {CurrentScreen.class})
//	public static class OpenPreviousScreenPatch {
//
//		public static void Postfix(CurrentScreen __s) {
//			if (__s.equals(FrozenPileEnums.FROZEN_VIEW)) {
//				DeprecatedFrozenPileManager.frozenPileViewScreen.reopen();
//			}
//		}
//	}
//
//	@SpirePatch(clz=AbstractDungeon.class, method="render", paramtypez = {SpriteBatch.class})
//	public static class RenderPatch {
//
//		public static void Postfix(AbstractDungeon __instance, SpriteBatch __sb) {
//			if (AbstractDungeon.screen.equals(FrozenPileEnums.FROZEN_VIEW)) {
//				DeprecatedFrozenPileManager.frozenPileViewScreen.render(__sb);
//			}
//		}
//	}
//
//	@SpirePatch(clz=OverlayMenu.class, method=SpirePatch.CONSTRUCTOR, paramtypez = {AbstractPlayer.class})
//	public static class FrozenPanelCreatePatch {
//
//		public static void Postfix(OverlayMenu __instance, AbstractPlayer __player) {
//			DeprecatedFrozenPileManager.frozenPanel = new DeprecatedFrozenCardsPanel();
//		}
//	}
//
//	@SpirePatch(clz=OverlayMenu.class, method="update", paramtypez = {})
//	public static class FrozenPanelUpdatePositionsPatch {
//
//		public static void Postfix(OverlayMenu __instance) {
//			DeprecatedFrozenPileManager.frozenPanel.updatePositions();
//		}
//	}
//
//	@SpirePatch(clz=OverlayMenu.class, method="render", paramtypez = {SpriteBatch.class})
//	public static class FrozenPanelRenderPatch {
//
//		public static void Postfix(OverlayMenu __instance, SpriteBatch __sb) {
//			DeprecatedFrozenPileManager.frozenPanel.render(__sb);
//		}
//	}
//
//	@SpirePatch(clz=AbstractPlayer.class, method=SpirePatch.CONSTRUCTOR, paramtypez = {String.class, PlayerClass.class})
//	public static class FrozenPileCreatePatch {
//
//		public static void Postfix(AbstractPlayer __instance, String __name, PlayerClass __class) {
//			DeprecatedFrozenPileManager.frozenPile = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
//		}
//	}
//
//	@SpirePatch(clz=AbstractPlayer.class, method="combatUpdate", paramtypez = {})
//	public static class FrozenPileCombatUpdatePatch {
//
//		public static void Postfix(AbstractPlayer __instance) {
//			DeprecatedFrozenPileManager.frozenPile.update();
//		}
//	}
//
//	@SpirePatch(clz=AbstractPlayer.class, method="preBattlePrep", paramtypez = {})
//	public static class FrozenPilePreBattlePrepPatch {
//
//		public static void Prefix(AbstractPlayer __instance) {
//			DeprecatedFrozenPileManager.frozenPile.clear();
//		}
//	}
//
//	@SpirePatch(clz=AbstractDungeon.class, method="resetPlayer", paramtypez = {})
//	public static class FrozenPileResetPlayerPatch {
//
//		public static void Prefix() {
//			DeprecatedFrozenPileManager.frozenPile.clear();
//		}
//	}
//
//	@SpirePatch(clz=AbstractDungeon.class, method="closeCurrentScreen", paramtypez = {})
//	public static class FrozenPileClosePilePatch {
//
//		public static void Prefix() {
//			if (AbstractDungeon.screen.equals(FrozenPileEnums.FROZEN_VIEW)) {
//                AbstractDungeon.overlayMenu.cancelButton.hide();
//                if (AbstractDungeon.previousScreen == null) {
//                    if (AbstractDungeon.player.isDead) {
//                        AbstractDungeon.previousScreen = CurrentScreen.DEATH;
//                    }
//                    else {
//                        AbstractDungeon.isScreenUp = false;
//                        AbstractDungeon.overlayMenu.hideBlackScreen();
//                    }
//                }
//                if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.player.isDead) {
//                    AbstractDungeon.overlayMenu.showCombatPanels();
//                }
//			}
//		}
//	}
//
//	@SpirePatch(clz=GetAllInBattleInstances.class, method="get")
//	public static class FrozenPileGetInstancesPatch {
//
//		@SpireInsertPatch(locator=Locator.class, localvars={"cards"})
//		public static void Insert(UUID __uuid, @ByRef HashSet<AbstractCard>[] __cards) {
//	        for (final AbstractCard c : DeprecatedFrozenPileManager.frozenPile.group) {
//	            if (!c.uuid.equals(__uuid)) {
//	                continue;
//	            }
//	            __cards[0].add(c);
//	        }
//		}
//
//		private static class Locator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctMethod) throws CannotCompileException, PatchingException {
//				Matcher finalMatcher = new Matcher.FieldAccessMatcher(com.megacrit.cardcrawl.characters.AbstractPlayer.class, "drawPile");
//				return LineFinder.findInOrder(ctMethod, new ArrayList<Matcher>(), finalMatcher);
//			}
//		}
//	}
	
}
