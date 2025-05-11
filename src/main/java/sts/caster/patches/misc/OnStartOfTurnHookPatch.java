package sts.caster.patches.misc;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.EnableEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import sts.caster.cards.skills.Embers;
import sts.caster.core.frozenpile.FrozenPileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OnStartOfTurnHookPatch {

	@SpirePatch(clz= GameActionManager.class, method="getNextAction")
	public static class OnStartOfTurnHook {
		@SpireInsertPatch(locator=Locator.class, localvars={} )
		public static void Insert(GameActionManager __instance) throws SecurityException, IllegalArgumentException {
			List<AbstractCard> frozenEmbers = FrozenPileManager.frozenPile.group.stream().
					filter(c -> c instanceof Embers).collect(Collectors.toList());

			for (AbstractCard ember : frozenEmbers) {
				((Embers)ember).triggerWhenOnFrozenPile();
			}
		}

		private static class Locator extends SpireInsertLocator {
			public int[] Locate(CtBehavior ctMethod) throws CannotCompileException, PatchingException {
				Matcher finalMatcher = new Matcher.NewExprMatcher(EnableEndTurnButtonAction.class);
				return LineFinder.findInOrder(ctMethod, new ArrayList<Matcher>(), finalMatcher);
			}
		}
	}
	
	
}
