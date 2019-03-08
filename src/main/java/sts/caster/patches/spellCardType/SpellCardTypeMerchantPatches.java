package sts.caster.patches.spellCardType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.shop.Merchant;
import com.megacrit.cardcrawl.shop.ShopScreen;

import javassist.CannotCompileException;
import javassist.CtBehavior;

public class SpellCardTypeMerchantPatches {
	
	@SpirePatch(clz=Merchant.class, method=SpirePatch.CONSTRUCTOR, paramtypez= {float.class, float.class, int.class})
	public static class constructorPatch {
		
		@SpireInsertPatch(locator=Locator.class)
		public static void Insert(Merchant __instance, float x, float y, int newShopScreen) 
				throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
			Field __cardsListField = __instance.getClass().getDeclaredField("cards1");
			__cardsListField.setAccessible(true);
			Object uncheckedList = __cardsListField.get(__instance);
			if (uncheckedList instanceof ArrayList<?>) {
				@SuppressWarnings("unchecked")
				ArrayList<AbstractCard> __cardsList = (ArrayList<AbstractCard>) uncheckedList;
				if (__cardsList != null) {
					__cardsList.clear();
					AbstractCard c;
					for (c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), CardType.ATTACK, true).makeCopy(); c.color == CardColor.COLORLESS; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), CardType.ATTACK, true).makeCopy()) {}
					__cardsList.add(c);
					for (c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), CasterCardType.SPELL, true).makeCopy(); c.color == CardColor.COLORLESS; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), CasterCardType.SPELL, true).makeCopy()) {}
					__cardsList.add(c);
					for (c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), CasterCardType.SPELL, true).makeCopy(); Objects.equals(c.cardID, __cardsList.get(__cardsList.size() - 1).cardID) || c.color == CardColor.COLORLESS; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), CasterCardType.SPELL, true).makeCopy()) {}
					__cardsList.add(c);
					for (c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), CardType.SKILL, true).makeCopy(); c.color == CardColor.COLORLESS; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), CardType.SKILL, true).makeCopy()) {}
					__cardsList.add(c);
					for (c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), CardType.POWER, true).makeCopy(); c.color == CardColor.COLORLESS; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), CardType.POWER, true).makeCopy()) {}
					__cardsList.add(c);
				}
			}
		}
		
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethod) throws CannotCompileException, PatchingException {
				Matcher finalMatcher = new Matcher.MethodCallMatcher(ShopScreen.class, "init");
				return LineFinder.findInOrder(ctMethod, new ArrayList<Matcher>(), finalMatcher);
			}
		}
	}
	
}
