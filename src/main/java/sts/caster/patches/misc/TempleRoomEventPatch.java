package sts.caster.patches.misc;

public class TempleRoomEventPatch {

//	@SpirePatch(clz=MapGenerator.class, method="generateDungeon")
//	public static class AddTempleEventRoomPatch {
//		@SpireInsertPatch(locator=Locator.class, localvars={"map"} )
//		public static void Insert(int height, int width, int pathDensity, Random rng, ArrayList<ArrayList<MapRoomNode>> map) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//			if (AbstractDungeon.actNum ==1 && AbstractDungeon.player instanceof TheCaster){
//				for (MapRoomNode roomNode : map.get(7)) {
//					TempleEventRoom ev = new TempleEventRoom();
//					roomNode.setRoom(ev);
//				}
//			}
//		}
//
//		private static class Locator extends SpireInsertLocator {
//			public int[] Locate(CtBehavior ctMethod) throws CannotCompileException, PatchingException {
//				Matcher finalMatcher = new Matcher.MethodCallMatcher(ModHelper.class, "isModEnabled");
//				return LineFinder.findInOrder(ctMethod, new ArrayList<Matcher>(), finalMatcher);
//			}
//		}
//	}
	
	
}
