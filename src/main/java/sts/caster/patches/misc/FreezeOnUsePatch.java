package sts.caster.patches.misc;

import basemod.helpers.SuperclassFinder;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;
import sts.caster.core.frozenpile.FrozenPileManager;
import sts.caster.patches.relics.FreezeOnUseCardField;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FreezeOnUsePatch
{
	@SpirePatch(clz = UseCardAction.class, method = "update", paramtypez = { })
	public static class ImplementFreezeOnUsePatch {
		@SpireInsertPatch(localvars = {  }, locator = LocatorPre.class)
		public static SpireReturn  InsertPre(final UseCardAction __instance, AbstractCard ___targetCard) {
			try {
				Boolean isFreezeOnUse = FreezeOnUseCardField.freezeOnuse.get(___targetCard);
				if (isFreezeOnUse){
					FrozenPileManager.moveToFrozenPile(AbstractDungeon.player.hand, ___targetCard);
					___targetCard.exhaustOnUseOnce = false;
					___targetCard.dontTriggerOnUseCard = false;
					AbstractDungeon.actionManager.addToBottom(new HandCheckAction());

					final Method tickMethod = SuperclassFinder.getSuperClassMethod(__instance.getClass(), "tickDuration");
					tickMethod.setAccessible(true);
					tickMethod.invoke(__instance);
					return SpireReturn.Return(null);
				}
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			return SpireReturn.Continue();
		}
		
		private static class LocatorPre extends SpireInsertLocator
		{
			public int[] Locate(final CtBehavior ctBehavior) throws Exception {
				final Matcher matcher = (Matcher)new Matcher.FieldAccessMatcher(UseCardAction.class, "exhaustCard");
				return LineFinder.findInOrder(ctBehavior, matcher);
			}
		}
	}
}