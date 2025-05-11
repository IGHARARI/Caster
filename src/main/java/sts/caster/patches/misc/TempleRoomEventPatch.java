package sts.caster.patches.misc;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.map.MapGenerator;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.random.Random;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import sts.caster.core.TheCaster;
import sts.caster.rooms.TempleEventRoom;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class TempleRoomEventPatch {

	@SpirePatch(clz=MapGenerator.class, method="generateDungeon")
	public static class AddTempleEventRoomPatch {
		@SpireInsertPatch(locator=Locator.class, localvars={"map"} )
		public static void Insert(int height, int width, int pathDensity, Random rng, ArrayList<ArrayList<MapRoomNode>> map) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			if (AbstractDungeon.actNum ==1 && AbstractDungeon.player instanceof TheCaster){
				for (MapRoomNode roomNode : map.get(7)) {
					TempleEventRoom ev = new TempleEventRoom();
					roomNode.setRoom(ev);
				}
			}
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethod) throws CannotCompileException, PatchingException {
				Matcher finalMatcher = new Matcher.MethodCallMatcher(ModHelper.class, "isModEnabled");
				return LineFinder.findInOrder(ctMethod, new ArrayList<Matcher>(), finalMatcher);
			}
		}
	}
	
	
}
