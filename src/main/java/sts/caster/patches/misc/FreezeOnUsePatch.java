package sts.caster.patches.misc;

import basemod.helpers.SuperclassFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;
import org.clapper.util.classutil.ClassFinder;
import sts.caster.actions.FreezeSpecificCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.frozenpile.FrozenPileManager;
import sts.caster.powers.AshenWallPower;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FreezeOnUsePatch
{
	@SpirePatch(clz = UseCardAction.class, method = "update", paramtypez = { })
	public static class ImplementFreezeOnUsePatch {
		@SpireInsertPatch(localvars = {  }, locator = LocatorPre.class)
		public static SpireReturn  InsertPre(final UseCardAction __instance, AbstractCard ___targetCard) {
			try {
				if (___targetCard instanceof CasterCard && ((CasterCard) ___targetCard).freezeOnUse){
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