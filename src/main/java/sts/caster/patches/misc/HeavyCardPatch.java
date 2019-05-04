package sts.caster.patches.misc;

import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.EnableEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import sts.caster.core.CasterMod;
import sts.caster.powers.AshenWallPower;

public class HeavyCardPatch
{
	@SpirePatch(clz = AbstractPlayer.class, method = "damage", paramtypez = { DamageInfo.class })
	public static class BlockCountPatch{
		@SpireInsertPatch(localvars = { "damageAmount" }, locator = LocatorPre.class)
		public static void InsertPre(final AbstractPlayer __instance, final DamageInfo info, @ByRef final int[] damageAmount) {
			System.out.println("Enter on block loss patch");
			if (info.type != DamageType.HP_LOSS || __instance.hasPower(AshenWallPower.POWER_ID)) {
				int blockLost = Math.min(__instance.currentBlock, damageAmount[0]);
				CasterMod.blockLostLastRound += blockLost;
				System.out.println("Increment lost block for " + blockLost + " total " + CasterMod.blockLostLastRound);
			}
		}
		
		private static class LocatorPre extends SpireInsertLocator
		{
			public int[] Locate(final CtBehavior ctBehavior) throws Exception {
				final Matcher matcher = (Matcher)new Matcher.MethodCallMatcher(AbstractPlayer.class, "decrementBlock");
				return LineFinder.findInOrder(ctBehavior, matcher);
			}
		}
	}

	
	
	
	@SpirePatch(clz=GameActionManager.class, method="getNextAction")
	public static class onStartOfTurnResetBlockLostPatch {
		
		
		@SpireInsertPatch(locator=Locator.class, localvars={})
		public static void Insert(GameActionManager __instance) {
			System.out.println("Resetting block " + CasterMod.blockLostLastRound);
			CasterMod.blockLostLastRound = 0;
		}
		
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethod) throws CannotCompileException, PatchingException {
				Matcher finalMatcher = new Matcher.MethodCallMatcher(EnableEndTurnButtonAction.class, SpirePatch.CONSTRUCTOR);
				return LineFinder.findInOrder(ctMethod, new ArrayList<Matcher>(), finalMatcher);
			}
		}
	}
}