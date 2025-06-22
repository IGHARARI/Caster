package sts.caster.patches.elements;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;
import sts.caster.cards.CasterCardTags;
import sts.caster.cards.mods.IgnitedCardMod;
import sts.caster.util.TextureHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RenderElementalModBordersPatch {
	@SpirePatch(clz=AbstractCard.class, method="renderCard", paramtypez = {SpriteBatch.class, boolean.class, boolean.class})
	public static class cardRenderPatch {

		@SpireInsertPatch(localvars = {}, locator = LocatorPre.class)
		public static void InsertPre(AbstractCard __card, SpriteBatch __sb, boolean __hovered, boolean __selected) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
	        if (!Settings.hideCards) {

	            if (AbstractDungeon.player != null) {
					float drawX = __card.current_x;
					float drawY = __card.current_y;
					Method renderHelperMethod = AbstractCard.class.getDeclaredMethod("renderHelper", SpriteBatch.class, Color.class, AtlasRegion.class, float.class, float.class);
					renderHelperMethod.setAccessible(true);
					Color drawColor = new Color(1, 1, 1, __card.transparency);
					if (CardModifierManager.hasModifier(__card, IgnitedCardMod.ID)) {
						Texture ignitedTexture = TextureHelper.getTexture("caster/images/512/ignited_border.png");
						AtlasRegion frozenRegion = new AtlasRegion(ignitedTexture, 0, 0, ignitedTexture.getWidth(), ignitedTexture.getHeight());
						renderHelperMethod.invoke(__card, __sb, drawColor, frozenRegion, drawX, drawY);
					}
					if (__card.hasTag(CasterCardTags.ELECTRIFIED)) {
						Texture electrifiedTexture = TextureHelper.getTexture("caster/images/512/electrified_border.png");
						AtlasRegion frozenRegion = new AtlasRegion(electrifiedTexture, 0, 0, electrifiedTexture.getWidth(), electrifiedTexture.getHeight());
						renderHelperMethod.invoke(__card, __sb, drawColor, frozenRegion, drawX, drawY);
					}
	                if (__card.hasTag(CasterCardTags.FROZEN)) {
						Texture frozenTexture = TextureHelper.getTexture("caster/images/512/frozen_border_t2.png");
						AtlasRegion frozenRegion = new AtlasRegion(frozenTexture, 0, 0, frozenTexture.getWidth(), frozenTexture.getHeight());
	                	renderHelperMethod.invoke(__card, __sb, drawColor, frozenRegion, drawX, drawY);
	                }

	            }
	        }
		}

		private static class LocatorPre extends SpireInsertLocator
		{
			public int[] Locate(final CtBehavior ctBehavior) throws Exception {
				final Matcher matcher = (Matcher)new Matcher.MethodCallMatcher(AbstractCard.class, "renderEnergy");
				return LineFinder.findInOrder(ctBehavior, matcher);
			}
		}
	}
}
