package sts.caster.patches.misc;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

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
				
				Integer currentBlockLost = CasterMod.blockLostPerTurn.get(GameActionManager.turn);
				if (currentBlockLost != null) {
					CasterMod.blockLostPerTurn.put(GameActionManager.turn, currentBlockLost + blockLost);
				} else {
					CasterMod.blockLostPerTurn.put(GameActionManager.turn, blockLost);
				}
				System.out.println("Increment lost block for " + blockLost + " total " + CasterMod.blockLostPerTurn.get(GameActionManager.turn));
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
}