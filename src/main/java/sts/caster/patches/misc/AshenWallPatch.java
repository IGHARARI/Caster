package sts.caster.patches.misc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import sts.caster.powers.AshenWallPower;

public class AshenWallPatch {

	@SpirePatch(clz=AbstractPlayer.class, method="damage")
	public static class PreventHPLossPatch {
		@SpireInsertPatch(locator=Locator.class, localvars={"damageAmount", "hadBlock"} )
		public static void Insert(AbstractPlayer __instance, DamageInfo info, @ByRef int[] damageAmount, @ByRef boolean[] hadBlock) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			if (damageAmount[0] <= 0) {
				return;
			}
			if (__instance.hasPower(AshenWallPower.POWER_ID)) {
				int curBlock = __instance.currentBlock;
				if (curBlock > 0 && info.type == DamageType.HP_LOSS) {
					hadBlock[0] = true;
					__instance.getPower(AshenWallPower.POWER_ID).flash();
					Method decrementBlockMethod = AbstractCreature.class.getDeclaredMethod("decrementBlock", DamageInfo.class, int.class);
					decrementBlockMethod.setAccessible(true);
					DamageInfo tempInfo = new DamageInfo(info.owner, info.base);
					int remaining = (int) decrementBlockMethod.invoke(__instance, tempInfo, damageAmount[0]);
					damageAmount[0] = remaining;
				}
			}
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethod) throws CannotCompileException, PatchingException {
				Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "decrementBlock");
				return LineFinder.findInOrder(ctMethod, new ArrayList<Matcher>(), finalMatcher);
			}
		}
	}
	
	
}
