package sts.caster.patches.misc;

@Deprecated
public class DeprecatedEmbersOnStartOfTurnHookPatch {
//
//	@SpirePatch(clz= GameActionManager.class, method="getNextAction")
//	public static class OnStartOfTurnHook {
//		@SpireInsertPatch(locator=Locator.class, localvars={} )
//		public static void Insert(GameActionManager __instance) throws SecurityException, IllegalArgumentException {
//			List<AbstractCard> frozenEmbers = DeprecatedFrozenPileManager.frozenPile.group.stream().
//					filter(c -> c instanceof Embers).collect(Collectors.toList());
//
//			for (AbstractCard ember : frozenEmbers) {
//				((Embers)ember).triggerWhenFrozen();
//			}
//		}
//
//		private static class Locator extends SpireInsertLocator {
//			public int[] Locate(CtBehavior ctMethod) throws CannotCompileException, PatchingException {
//				Matcher finalMatcher = new Matcher.NewExprMatcher(EnableEndTurnButtonAction.class);
//				return LineFinder.findInOrder(ctMethod, new ArrayList<Matcher>(), finalMatcher);
//			}
//		}
//	}
}
