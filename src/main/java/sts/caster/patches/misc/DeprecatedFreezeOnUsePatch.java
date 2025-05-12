package sts.caster.patches.misc;

@Deprecated
public class DeprecatedFreezeOnUsePatch {
//	@SpirePatch(clz = UseCardAction.class, method = "update", paramtypez = { })
//	public static class ImplementFreezeOnUsePatch {
//		@SpireInsertPatch(localvars = {  }, locator = LocatorPre.class)
//		public static SpireReturn  InsertPre(final UseCardAction __instance, AbstractCard ___targetCard) {
//			try {
//				Boolean isFreezeOnUse = DeprecatedFreezeOnUseCardField.freezeOnuse.get(___targetCard);
//				if (isFreezeOnUse){
//					DeprecatedFrozenPileManager.moveToFrozenPile(AbstractDungeon.player.hand, ___targetCard);
//					___targetCard.exhaustOnUseOnce = false;
//					___targetCard.dontTriggerOnUseCard = false;
//					AbstractDungeon.actionManager.addToBottom(new HandCheckAction());
//
//					final Method tickMethod = SuperclassFinder.getSuperClassMethod(__instance.getClass(), "tickDuration");
//					tickMethod.setAccessible(true);
//					tickMethod.invoke(__instance);
//					return SpireReturn.Return(null);
//				}
//			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//				e.printStackTrace();
//			}
//			return SpireReturn.Continue();
//		}
//
//		private static class LocatorPre extends SpireInsertLocator
//		{
//			public int[] Locate(final CtBehavior ctBehavior) throws Exception {
//				final Matcher matcher = (Matcher)new Matcher.FieldAccessMatcher(UseCardAction.class, "exhaustCard");
//				return LineFinder.findInOrder(ctBehavior, matcher);
//			}
//		}
//	}
}